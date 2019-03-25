package com.qexcel.test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import com.qexcel.core.ExcelAdapter;
import com.qexcel.core.SheetAdapter;
import com.qexcel.core.cell.resolver.IntegerResolver;
import com.qexcel.core.cell.resolver.StringResolver;
import com.qexcel.core.enums.CellDataType;
import com.qexcel.core.enums.ExcelVersion;
import com.qexcel.core.enums.HorizontalAlign;
import com.qexcel.core.enums.VerticalAlign;
import com.qexcel.core.seqaccess.SheetSeqAccess.SheetWriteAccess;
import com.qexcel.helper.ExcelHelper;
import com.qexcel.template.anno.AnnoCell;
import com.qexcel.template.anno.AnnoTemplateEngine;
import com.qexcel.template.singleline.SingleLine;
import com.qexcel.template.table.ExcelTable;
import com.qexcel.template.transformers.AnnoPositionCell;
import com.qexcel.template.transformers.Transformers;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExampleTest {
    public static class ExampleObject{
        List<ExampleBean> datas;

        public List<ExampleBean> getDatas() {
            return datas;
        }

        public void setDatas(List<ExampleBean> datas) {
            this.datas = datas;
        }
    }
    
    public static class ExampleBean{
        private String param1;
        private Date param2;
        private int param3;
        
        public String getParam1() {
            return param1;
        }
        public void setParam1(String param1) {
            this.param1 = param1;
        }
        public Date getParam2() {
            return param2;
        }
        public void setParam2(Date param2) {
            this.param2 = param2;
        }
        public int getParam3() {
            return param3;
        }
        public void setParam3(int param3) {
            this.param3 = param3;
        }
        @Override
        public String toString() {
            return "Example [param1=" + param1 + ", param2=" + param2 + ", param3=" + param3 + "]";
        }
    }
    
    public static class ExampleCombBean{
        private int sum1;
        private int sum2;
        private int sum3;
        private Integer sum4;
        private List<ExampleBean> datas;
        public int getSum1() {
            return sum1;
        }
        public void setSum1(int sum1) {
            this.sum1 = sum1;
        }
        public int getSum2() {
            return sum2;
        }
        public void setSum2(int sum2) {
            this.sum2 = sum2;
        }
        public int getSum3() {
            return sum3;
        }
        public void setSum3(int sum3) {
            this.sum3 = sum3;
        }
        public Integer getSum4() {
            return sum4;
        }
        public void setSum4(Integer sum4) {
            this.sum4 = sum4;
        }
        public List<ExampleBean> getDatas() {
            return datas;
        }
        public void setDatas(List<ExampleBean> datas) {
            this.datas = datas;
        }
    }
    
    @ExcelTable(
            dataProperty = "datas",//当传入的javaObj不是Collection的情况下调用此方法获取数据列表
            head = {//定义表格头模板
                    @AnnoCell(text="列1",fontBold=true,width=26,fontColor=HSSFColorPredefined.RED),
                    @AnnoCell(text="#{#param ?: '未定义'}"),
                    @AnnoCell(text="列3",fontBold=true,fontColor=HSSFColorPredefined.GREEN),
            },
            body = {//定义表格数据模板
                    @AnnoCell(property="param1"),
                    @AnnoCell(type=CellDataType.DATE,format="yyyy-MM-dd HH:mm:ss",property="param2",fontBold=true),
                    @AnnoCell(property="param3",writeSpel="#{getVal('param1')=='testSpel' ? 888 : getVal('param3')}")
            }
    )
    public static class ExcelTableTplClass{}
    
    @Transformers(body= {
            @AnnoPositionCell(
                    startRow=0,
                    rowOffset=1,
                    startColumn=0,
                    columnOffset=1,
                    cellData=@AnnoCell(property="sum1",halign=HorizontalAlign.CENTER,valign=VerticalAlign.CENTER
                    ,fontBold=true,fontColor=HSSFColorPredefined.RED))
            ,@AnnoPositionCell(
                    startRow=0,
                    rowOffset=3,
                    startColumn=2,
                    columnOffset=3,
                    cellData=@AnnoCell(property="sum2",halign=HorizontalAlign.CENTER,valign=VerticalAlign.CENTER
                    ,fontBold=true,fontColor=HSSFColorPredefined.RED))
    })
    public static class TransformersTpl{}
    
    @ExcelTable(
            dataProperty = "datas",//当传入的javaObj不是Collection的情况下调用此方法获取数据列表
            head = {//定义表格头模板
                    @AnnoCell(text="列1",fontBold=true,width=26,fontColor=HSSFColorPredefined.RED),
                    @AnnoCell(),
                    @AnnoCell(text="列3",fontBold=true,width=26,fontColor=HSSFColorPredefined.RED),
            },
            body = {//定义表格数据模板
                    @AnnoCell(property="param1"),
                    @AnnoCell(),
                    @AnnoCell(property="param3")
            }
    )
    public static class ExcelTableTplWithBlankColumClass{}
    
    @SingleLine(
        body = {
                @AnnoCell(text="总结1",fontBold=true),//第1列单元格,写入单元格数据为"总结1"
                @AnnoCell(property="sum1",writeSpel="#{getVal('sum1')>10 ? 999:getVal('sum1')}", type=CellDataType.NUMBER),//第2列单元格,调用javaObj中getSum1()写入单元格数据,读取单元格数据时调用javaObj的setSum1()方法
                @AnnoCell(text="#{#param ?: '未定义'}",fontBold=true),//第3列单元格,写入单元格数据为SpEL表达式返回值
                @AnnoCell(property="sum2")//第4列单元格,调用javaObj的getSum2()写入单元格数据,读取单元格数据时调用javaObj的setSum1()方法
        }
    )
    public static class SingleLineTplClass{}
    
    @SingleLine(
            order=0,//指定模板顺序
            body = {
                    @AnnoCell(text="总结1-1",fontBold=true),
                    @AnnoCell(property="sum1"),
                    @AnnoCell(text="总结1-2",fontBold=true),
                    @AnnoCell(property="sum2",type=CellDataType.NUMBER)
            }
    )
    @SingleLine(
            order=1,
            body = {
                    @AnnoCell(text="总结2-1",fontBold=true),
                    @AnnoCell(property="sum3"),
                    @AnnoCell(text="总结2-2",fontBold=true),
                    @AnnoCell(property="sum4",type=CellDataType.NUMBER)
            }
    )
    @SingleLine(//空行
            order=2
    )
    @ExcelTable(
            order=3,
            dataProperty="datas",
            head = {
                    @AnnoCell(text="列1",fontBold=true,width=26,fontColor=HSSFColorPredefined.RED),
                    @AnnoCell(text="列2"),
                    @AnnoCell(text="列3",fontBold=true,fontColor=HSSFColorPredefined.GREEN),
            },
            body = {
                    @AnnoCell(property="param1"),
                    @AnnoCell(type=CellDataType.DATE,format="yyyy-MM-dd HH:mm:ss",property="param2",fontBold=true),
                    @AnnoCell(property="param3")
            }
    )
    @SingleLine(//空行
            order=4
    )
    @Transformers(
            order=5,
            body= {
                @AnnoPositionCell(
                        startRow=0,
                        rowOffset=1,
                        startColumn=0,
                        columnOffset=1,
                        cellData=@AnnoCell(property="sum1",halign=HorizontalAlign.CENTER,valign=VerticalAlign.CENTER
                        ,fontBold=true,fontColor=HSSFColorPredefined.RED))
                ,@AnnoPositionCell(
                        startRow=0,
                        rowOffset=3,
                        startColumn=2,
                        columnOffset=3,
                        cellData=@AnnoCell(text="SUM(D1:D2)",property="sum2",type=CellDataType.FORMULA,halign=HorizontalAlign.CENTER,valign=VerticalAlign.CENTER
                        ,fontBold=true,fontColor=HSSFColorPredefined.RED))
            }
    )
    public static class ExampleMultiTpl{}
    
    @Test
    public void test001WriteSimple() throws Exception {
        //使用写构建器创建excel适配器
        ExcelAdapter wa = ExcelHelper.writeBuilder().version(ExcelVersion.XLSX).build();
        //创建sheet并开启sheet顺序写入
        SheetWriteAccess sra = wa.createSheet("第一页").openWriteSequential();
        //创建第1行,并依次创建第1列和第2列cell
        sra.createRow().createCell(CellDataType.STRING, "字符串单元格：")
                    .createCell(CellDataType.STRING, "hello world");
        //创建第2行,并依次创建第1列和第2列cell
        sra.createRow().createCell(CellDataType.STRING, "整形数字单元格")
                    .createCell(CellDataType.NUMBER, 10000);
        //创建第3行
        sra.createRow().createCell(CellDataType.STRING, "浮点数字单元格")
            .createFormatCell(CellDataType.NUMBER, 10000.1234567,"#.000");
        //创建第4行
        sra.createRow().createCell(CellDataType.STRING, "java8时间单元格:")
                    .createCell(CellDataType.DATE, LocalDateTime.now());
        //创建第5行
        sra.createRow().createCell(CellDataType.STRING, "java8日期单元格(类型强转):")
                    .createCell(CellDataType.STRING, LocalDate.now());
        //创建第6行
        sra.createRow().createCell(CellDataType.STRING, "日期单元格:")
                    .createCell(CellDataType.STRING, new Date());
        //创建第7行
        sra.createRow().createCell(CellDataType.STRING, "日期单元格:")
                    .createCell(CellDataType.DATE, "2018-01-08 23:10:32");
        //写入文件
        wa.flushTo("D://simple.xlsx");
    }
    
    @Test
    public void test002ReadSimple() throws Exception {
        //使用读构建起创建excel适配器
        ExcelAdapter wa = ExcelHelper.readBuilder()
                          .version(ExcelVersion.XLSX).withInput("D://simple.xlsx").build();
        //获取指定sheet并开启sheet顺序读取
        SheetAdapter sheet = wa.getSheet("第一页");
        //读取第1行,第2列cell
        System.out.println(StringResolver.convert(sheet.getCell(0, 1)));//hello world
        //读取第2行，第2列cell
        System.out.println(IntegerResolver.convert(sheet.getCell(1, 1)));//10000
        //读取第3行，第2列cell
        System.out.println(StringResolver.convert(sheet.getCell(2, 1),"#.000"));//10000.123
        //读取第4行，第2列cell
        System.out.println(StringResolver.convert(sheet.getCell(4, 1),"yyyyMMdd"));
    }
    
    @Test
    public void test003WriteSingleLine() throws Exception {
        //定义SpEL参数
        Map<String,Object> params = new HashMap<>();
        params.put("param", "动态参数");
        
        //模拟数据
        ExampleCombBean javaObj = new ExampleCombBean();
        javaObj.setSum1(1000);
        javaObj.setSum2(2000);
        //构建写适配器
        ExcelAdapter wa = ExcelHelper.writeBuilder().version(ExcelVersion.XLSX).build();
        //创建sheet
        wa.createSheet("第一页");
        //从第1行开始根据模板写入数据
        wa.writeToWorkBook(AnnoTemplateEngine.build(SingleLineTplClass.class),params, "第一页",0, javaObj);
        //从第4行开始根据模板写入数据,模拟未设置SpEL参数情况
        wa.writeToWorkBook(AnnoTemplateEngine.build(SingleLineTplClass.class),null, "第一页",3, javaObj);
        //保存到文件
        wa.flushTo("D://SingleLine.xlsx");
    }
    
    @Test
    public void test004ReadSingleLine() throws Exception {
        ExampleCombBean javaObj = new ExampleCombBean();
        //构建读适配器
        ExcelAdapter wa = ExcelHelper.readBuilder().withInput("D://SingleLine.xlsx").build();
        wa.readToJavaObj(AnnoTemplateEngine.build(SingleLineTplClass.class), "第一页",0,javaObj);
        System.out.println("Sum1 :"+javaObj.getSum1()); //1000
        System.out.println("Sum2 :"+javaObj.getSum2()); //2000
    }
    
    @Test
    public void test005WriteExcelTable() throws Exception {
        //组装SpEL参数
        Map<String,Object> params = new HashMap<>();
        params.put("param", "动态参数");
        
        ExampleCombBean combBean = new ExampleCombBean();
        List<ExampleBean> datas = new ArrayList<>();
        ExampleBean data1 = new ExampleBean();
        data1.setParam1("hello1");
        data1.setParam2(new Date());
        data1.setParam3(10001);
        ExampleBean data2 = new ExampleBean();
        data2.setParam1("hello2");
        data2.setParam2(new Date());
        data2.setParam3(10002);
        ExampleBean data3 = new ExampleBean();
        data3.setParam1("testSpel");
        data3.setParam2(new Date());
        data3.setParam3(10002);
        datas.add(data1);
        datas.add(data2);
        datas.add(data3);
        combBean.setDatas(datas);
        
        //直接写方式
        ExcelAdapter wa = ExcelHelper.writeBuilder().version(ExcelVersion.XLSX).build();
        wa.createSheet("第一页");
        //从第1行开始写,写入Collection
        wa.writeToWorkBook(AnnoTemplateEngine.build(ExcelTableTplClass.class),params,"第一页",0,datas);
        //从第11行开始写入,写入javaObj
        wa.writeToWorkBook(AnnoTemplateEngine.build(ExcelTableTplClass.class),null, "第一页",10,combBean);
        wa.flushTo("D://ExcelTable.xlsx");
        
        //使用顺序写方式
        ExcelAdapter wa2 = ExcelHelper.writeBuilder().version(ExcelVersion.XLSX).build();
        wa2.createSheet("第一页")
        .openWriteSequential()
            .writeToWorkBook(AnnoTemplateEngine.build(ExcelTableTplClass.class),params,datas)//从默认第1行开始写,写入Collection
            .jump(10)//顺序跳过10行
            .writeToWorkBook(AnnoTemplateEngine.build(ExcelTableTplClass.class), combBean)//从指定行开始写入,写入javaObj
        .complete()
        .flushTo("D://ExcelTable-seqAccess.xlsx");
    }
    
    @Test
    public void test006ReadExcelTable() throws Exception {
        ExcelAdapter ra = ExcelHelper.readBuilder().withInput("D://ExcelTable.xlsx").build();
        //从第1行开始到结束,读取表格数据到Collection对象
        List<ExampleBean> datas = ra.readJavaObjList(AnnoTemplateEngine.build(ExcelTableTplClass.class), "第一页",0,-1,ExampleBean.class);
        System.out.println(datas.size());
        
        //从11行开始到结束,读取表格数据到javaObj中的Collection类型属性
        ExampleCombBean javaObj = new ExampleCombBean();
        ra.readToJavaObj(AnnoTemplateEngine.build(ExcelTableTplClass.class), "第一页",10,javaObj);
        System.out.println(javaObj.getDatas().size());
    }
    
    @Test
    public void test007WriteMultiTpl() throws Exception {
        ExampleCombBean combBean = new ExampleCombBean();
        combBean.setSum1(100000);
        combBean.setSum2(200000);
        combBean.setSum3(300000);
        combBean.setSum4(400000);
        combBean.setDatas(new ArrayList<>());
        ExampleBean data1 = new ExampleBean();
        data1.setParam1("hello1");
        data1.setParam2(new Date());
        data1.setParam3(10001);
        ExampleBean data2 = new ExampleBean();
        data2.setParam1("hello2");
        data2.setParam2(new Date());
        data2.setParam3(10002);
        combBean.getDatas().add(data1);
        combBean.getDatas().add(data2);
        
        ExcelAdapter wa = ExcelHelper.writeBuilder().version(ExcelVersion.XLSX).build();
        wa.createSheet("第一页");
        wa.writeToWorkBook(AnnoTemplateEngine.build(ExampleMultiTpl.class),null,"第一页", combBean);
        wa.flushTo("D://MultiTpl.xlsx");
    }
    
    @Test
    public void test008ReadMultiTpl() throws Exception {
        ExampleCombBean combBean = new ExampleCombBean();
        ExcelAdapter ra = ExcelHelper.readBuilder().withInput("D://MultiTpl.xlsx").build();
        ra.readToJavaObj(AnnoTemplateEngine.build(ExampleMultiTpl.class), "第一页",0,combBean);
        System.out.println(combBean.getSum1());
        System.out.println(combBean.getSum2());
        System.out.println(combBean.getSum3());
        System.out.println(combBean.getSum4());
        System.out.println(combBean.getDatas().size());
    }
    
    @Test
    public void test009WriteExcelTableWithBlankColum() throws Exception {
        //组装SpEL参数
        Map<String,Object> params = new HashMap<>();
        params.put("param", "动态参数");
        
        ExampleCombBean combBean = new ExampleCombBean();
        List<ExampleBean> datas = new ArrayList<>();
        ExampleBean data1 = new ExampleBean();
        data1.setParam1("hello1");
        data1.setParam2(new Date());
        data1.setParam3(10001);
        ExampleBean data2 = new ExampleBean();
        data2.setParam1("hello2");
        data2.setParam2(new Date());
        data2.setParam3(10002);
        ExampleBean data3 = new ExampleBean();
        data3.setParam1("testSpel");
        data3.setParam2(new Date());
        data3.setParam3(10002);
        datas.add(data1);
        datas.add(data2);
        datas.add(data3);
        combBean.setDatas(datas);
        
        //直接写方式
        ExcelAdapter wa = ExcelHelper.writeBuilder().version(ExcelVersion.XLSX).build();
        wa.createSheet("第一页");
        //从第1行开始写,写入Collection
        wa.writeToWorkBook(AnnoTemplateEngine.build(ExcelTableTplWithBlankColumClass.class),params,"第一页",0,datas);
        //从第11行开始写入,写入javaObj
        wa.writeToWorkBook(AnnoTemplateEngine.build(ExcelTableTplClass.class), null,"第一页",10,combBean);
        wa.flushTo("D://ExcelTableWithBlankColum.xlsx");
    }
    
    @Test
    public void test010ReadExcelTableWithBlankColum() throws Exception {
        ExcelAdapter ra = ExcelHelper.readBuilder().withInput("D://ExcelTableWithBlankColum.xlsx").build();
        //从第1行开始到结束,读取表格数据到Collection对象
        List<ExampleBean> datas = ra.readJavaObjList(AnnoTemplateEngine.build(ExcelTableTplClass.class), "第一页",0,-1,ExampleBean.class);
        System.out.println(datas.size());
    }
    
    @Test
    public void test011WriteSingleLineAppender() throws Exception {
        //定义SpEL参数
        Map<String,Object> params = new HashMap<>();
        params.put("param", "动态参数");
        
        //模拟数据
        ExampleCombBean javaObj = new ExampleCombBean();
        javaObj.setSum1(1000);
        javaObj.setSum2(2000);
        //构建写适配器
        ExcelAdapter wa = ExcelHelper.writeBuilder().version(ExcelVersion.XLSX).build();
        //创建sheet
        wa.createSheet("第一页");
        
        wa.writeToWorkBook(AnnoTemplateEngine.build(SingleLineTplClass.class),params, 0, 1, (status,sink)->{
            if(status == 3) {
                return true;
            }
            sink.accept(Arrays.asList(javaObj));
            return false;
        });
        //保存到文件
        wa.flushTo("D://SingleLine-appender.xlsx");
    }
    
    @Test
    public void test012WriteExcelTableWithAppender() throws Exception {
        List<ExampleBean> datas = new ArrayList<>();
        ExampleBean data1 = new ExampleBean();
        data1.setParam1("hello1");
        data1.setParam2(new Date());
        data1.setParam3(10001);
        ExampleBean data2 = new ExampleBean();
        data2.setParam1("hello2");
        data2.setParam2(new Date());
        data2.setParam3(10002);
        ExampleBean data3 = new ExampleBean();
        data3.setParam1("testSpel");
        data3.setParam2(new Date());
        data3.setParam3(10002);
        datas.add(data1);
        datas.add(data2);
        datas.add(data3);
        ExcelAdapter wa = ExcelHelper.writeBuilder().version(ExcelVersion.XLSX).build();
        wa.createSheet("第一页");
        //从第1行开始到结束,读取表格数据到Collection对象
        wa.writeToWorkBook(AnnoTemplateEngine.build(ExcelTableTplClass.class), 0, 1, (status,sink)->{
            if(status == 3) {
                return true;
            }
            sink.accept(datas);
            return false;
        });
        wa.flushTo("D://ExcelTable-append.xlsx");
    }
    
    @Test
    public void test013ReadExcelTableWithSink() throws Exception {
        ExcelAdapter ra = ExcelHelper.readBuilder().withInput("D://ExcelTable-append.xlsx").build();
        //从第1行开始到结束,读取表格数据到Collection对象
        List<ExampleBean> datas = new ArrayList<>();
        ra.readJavaObjList(AnnoTemplateEngine.build(ExcelTableTplClass.class), "第一页",0,ExampleBean.class, 1, (data)->datas.addAll(data));
        System.out.println(datas.size());
    }
    
    @Test
    public void test014WriteTransformers() throws Exception {
        ExampleCombBean javaObj = new ExampleCombBean();
        javaObj.setSum1(1000);
        javaObj.setSum2(2000);
        //构建写适配器
        ExcelAdapter wa = ExcelHelper.writeBuilder().version(ExcelVersion.XLSX).build();
        //创建sheet
        wa.createSheet("第一页");
        wa.writeToWorkBook(AnnoTemplateEngine.build(TransformersTpl.class),null, 0, javaObj);
        wa.flushTo("D://Transformers.xlsx");
    }
    
    @Test
    public void test015ReadTransformers() throws Exception {
        ExcelAdapter ra = ExcelHelper.readBuilder().withInput("D://Transformers.xlsx").build();
        ExampleCombBean javaObj = null;
        javaObj = ra.readJavaObj(AnnoTemplateEngine.build(TransformersTpl.class), "第一页", 0, ExampleCombBean.class);
        System.out.println(javaObj.sum1);
        System.out.println(javaObj.sum2);
    }
}
