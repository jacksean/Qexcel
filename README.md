# Qexcel
基于apache POI实现excel与bean的转换,支持一般业务的excel导入导出,并支持模板导入导出

1.编译环境

  java8及其以上
  spring-expression-4.3.18及其以上

2.已支持的模板

  Singleline(单行)  注：单行多单元格模板
  
  ExcelTable(数据列表) 注：带表头的多行数据表格
  
  Transformers(变形金刚) 注：不规则带合并单元格矩阵
  
3.简单使用

  3.1 定义模板
      
      @SingleLine(
            body = {
                    @AnnoCell(text="总结1",fontBold=true),//第1列单元格,写入单元格数据为"总结1"
                    @AnnoCell(property="sum1",writeSpel="#{getVal('sum1')>10 ? 999:getVal('sum1')}", type=CellDataType.NUMBER),//第2列单元格,调用javaObj中getSum1()写入单元格数据,读取单元格数据时调用javaObj的setSum1()方法
                    @AnnoCell(text="#{#param ?: '未定义'}",fontBold=true),//第3列单元格,写入单元格数据为SpEL表达式返回值
                    @AnnoCell(property="sum2")//第4列单元格,调用javaObj的getSum2()写入单元格数据,读取单元格数据时调用javaObj的setSum1()方法
            }
        )
        public static class SingleLineTplClass{}
        
   3.2 使用模板
   
      ExcelAdapter wa = ExcelHelper.writeBuilder().version(ExcelVersion.XLSX).build();
      //创建sheet
      wa.createSheet("第一页");
      //从第1行开始根据模板写入数据
      wa.writeToWorkBook(AnnoTemplateEngine.build(SingleLineTplClass.class),params, "第一页",0, javaObj);
      wa.flushTo("D://SingleLine.xlsx");
      
4.详细使用
  更多使用：com.qexcel.test.ExampleTest
  
5.联系
  email: qexcel@163.com
