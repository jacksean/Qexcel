package com.qexcel.template.anno;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.qexcel.core.seqaccess.SheetSeqAccess.SheetReadAccess;
import com.qexcel.core.seqaccess.SheetSeqAccess.SheetWriteAccess;
import com.qexcel.core.template.Template;
import com.qexcel.core.template.TemplateEngine;
import com.qexcel.core.template.TemplateParser;
import com.qexcel.core.template.context.SeqaccessContext;

/**
 * 类AnnoTemplateEngine.java的实现描述：基于注解的模板处理引擎
 * 多个模板渲染顺序为垂直渲染
 * @author sean 2019年3月20日 下午1:39:34
 */
public class AnnoTemplateEngine implements TemplateEngine {

    @SuppressWarnings("rawtypes")
    private static Map<Class, AnnoTemplateMeta>   tplMetaCache = new ConcurrentHashMap<>();

    @SuppressWarnings("rawtypes")
    private static Map<Class, AnnoTemplateEngine> engineCache  = new ConcurrentHashMap<>();

    @SuppressWarnings("rawtypes")
    List<Template>                                templates;

    @SuppressWarnings("rawtypes")
    public AnnoTemplateEngine(List<Template> templates) {
        this.templates = templates;
    }

    public AnnoTemplateEngine(Class<?> tplClass) {
        this.templates = init(tplClass);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private List<Template> init(Class<?> tplClz) {
        List<Annotation> tpls = getAndCheckTplAnno(tplClz);
        return tpls.stream().map(tpl -> {
            AnnoTemplateMeta tplMeta = getTplAnnoMeta(tpl);
            return new Template(tplMeta.getResolver(), tplMeta.getFactory().getTemplateConfig(tpl));
        }).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> List<R> readObjectList(SeqaccessContext<? extends SheetReadAccess> ctx, Class<R> objClz) {
        assert (templates.size() == 1);
        return templates.get(0).getParser().readObjectList(ctx, templates.get(0).getConfig(), objClz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> void readObjectList(SeqaccessContext<? extends SheetReadAccess> ctx, Class<R> objClz, int batchSize,
                                   Consumer<List<R>> sink) {
        assert (templates.size() == 1);
        templates.get(0).getParser().readObjectList(ctx, templates.get(0).getConfig(), objClz, batchSize, sink);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void readToObject(SeqaccessContext<? extends SheetReadAccess> ctx, Object obj) {
        templates.forEach(template -> {
            template.getParser().readToObject(ctx, template.getConfig(), obj);
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void writeToWb(SeqaccessContext<? extends SheetWriteAccess> ctx, Object obj) {
        templates.forEach(template -> {
            template.getParser().writeToWb(ctx, template.getConfig(), obj);
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void writeToWb(SeqaccessContext<? extends SheetWriteAccess> ctx, int initStatus,
                          BiPredicate<Integer,Consumer<List>> appender) {
        assert (templates.size() == 1);
        templates.get(0).getParser().writeToWb(ctx, templates.get(0).getConfig(), initStatus, appender);
    }

    public static AnnoTemplateEngine build(Class<?> tplClz) {
        return engineCache.computeIfAbsent(tplClz, k -> new AnnoTemplateEngine(tplClz));
    }

    public List<Annotation> getTplAnnoCollection(Class<?> tplClz) {
        List<Annotation> tplList = new ArrayList<>();
        try {
            Annotation[] annos = tplClz.getAnnotations();
            for (Annotation anno : annos) {
                if (null != anno.annotationType().getAnnotation(AnnoTemplate.class)) {
                    tplList.add(anno);
                } else if (null != anno.annotationType().getAnnotation(AnnoTemplateList.class)) {
                    Method m = anno.getClass().getMethod("value");
                    Annotation[] tpls = (Annotation[]) m.invoke(anno);
                    for (Annotation tpl : tpls) {
                        tplList.add(tpl);
                    }
                }
            }
            tplList.sort((a, b) -> {
                return getTplAnnoOrder(a) >= getTplAnnoOrder(b) ? 1 : -1;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tplList;
    }

    public int getTplAnnoOrder(Annotation tpl) {
        try {
            if (null != tpl.annotationType().getAnnotation(AnnoTemplate.class)) {
                Method m = tpl.getClass().getMethod("order");
                return (int) m.invoke(tpl);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    @SuppressWarnings("rawtypes")
    public AnnoTemplateMeta getTplAnnoMeta(Annotation tpl) {
        Class<? extends Annotation> annoType = tpl.annotationType();
        return tplMetaCache.computeIfAbsent(annoType, k -> {
            AnnoTemplate tplAnno = annoType.getAnnotation(AnnoTemplate.class);
            try {
                if (null != tplAnno) {
                    AnnoTemplateConfigFactory tplFactory = tplAnno.factory().newInstance();
                    TemplateParser tplResolver = tplAnno.parser().newInstance();
                    AnnoTemplateMeta meta = new AnnoTemplateMeta<AnnoTemplateConfigFactory, TemplateParser>(tplFactory,
                            tplResolver);
                    return meta;
                } else {
                    throw new RuntimeException("can not find @AnnotationTemplate on " + annoType);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<Annotation> getAndCheckTplAnno(Class<?> tplClz) {
        List<Annotation> tpls = getTplAnnoCollection(tplClz);
        if (CollectionUtils.isEmpty(tpls)) {
            throw new RuntimeException("not find Template annotation on " + tplClz.getName());
        }
        return tpls;
    }
}
