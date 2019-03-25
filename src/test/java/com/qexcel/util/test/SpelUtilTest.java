package com.qexcel.util.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.qexcel.util.SpelUtil;

public class SpelUtilTest {
    
    public static class Sample2{
        Sample sample;

        public Sample getSample() {
            return sample;
        }

        public void setSample(Sample sample) {
            this.sample = sample;
        }
    }
    
    public static class Sample{
        private String name;
        private int age;
        private Date date;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getAge() {
            return age;
        }
        public void setAge(int age) {
            this.age = age;
        }
        public Date getDate() {
            return date;
        }
        public void setDate(Date date) {
            this.date = date;
        }
        public String getExtName(String xx) {
            return xx;
        }
    }
    
    @Test
    public void testParseStringMapOfStringObject() {
        Sample param = new Sample();
        param.setAge(10);
        param.setName("张三");
        param.setDate(new Date());
        Map<String,Object> params = new HashMap<>();
        params.put("item", param);
        Sample2 root = new Sample2();
        root.setSample(param);
        Object xx = root;
        System.out.println(SpelUtil.parse("#{sample.getName()}",xx, params));
        System.out.println(SpelUtil.parse("#{T(java.lang.Math).random()*100.0}",param, params));
        System.out.println(SpelUtil.parse("#{name eq '张1' ? 1:2}",param, params));
        Assert.assertEquals(SpelUtil.parse("#{#item.getName() eq '张三'}", params),true);
        Assert.assertEquals(SpelUtil.parse("#{#item.getDate()}", params,Date.class),param.getDate());
        Assert.assertTrue(SpelUtil.parse("#{T(java.lang.Math).random()}", params) != null);
    }

}
