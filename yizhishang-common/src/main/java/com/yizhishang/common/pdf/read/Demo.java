package com.yizhishang.common.pdf.read;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.utilities.PdfTable;
import com.spire.pdf.utilities.PdfTableExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

public class Demo {

    public static void main(String[] args) {
        String fileName = "F:\\gitee\\yizhishang-project\\docs\\铁路大票.pdf";
        String outputPath = "F:\\gitee\\yizhishang-project\\docs\\铁路大票.txt";
        String outputExcelPath = "F:\\gitee\\yizhishang-project\\docs\\铁路大票.xlsx";


        String result = "";
        File file = new File(fileName);
        try (FileInputStream in = new FileInputStream(fileName)) {
            PdfDocument document = new PdfDocument();
            document.loadFromStream(in);

            //保存为Excel文档
//            document.saveToFile(outputExcelPath, FileFormat.XLSX);
//            document.dispose();

            StringBuilder builder = new StringBuilder();

            //抽取表格
            PdfTableExtractor extractor = new PdfTableExtractor(document);
            PdfTable[] tableLists;
            int k = 0;
            for (int page = 0; page < document.getPages().getCount(); page++) {
                tableLists = extractor.extractTable(page);
                k++;
                if (tableLists != null && tableLists.length > 0) {
                    for (PdfTable table : tableLists) {
                        int row = table.getRowCount();
                        int column = table.getColumnCount();
                        for (int i = 0; i < row; i++) {
                            for (int j = 0; j < column; j++) {
                                String text = table.getText(i, j);
                                builder.append(text + " ");
                            }
                            builder.append("\r\n");
                        }
                    }
                }
            }
            System.out.println(k);
            System.out.println(builder);

            //将提取的表格内容写入txt文档
            FileWriter fileWriter = new FileWriter(outputPath);
            fileWriter.write(builder.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("读取PDF文件" + file.getAbsolutePath() + "生失败！" + e);
            e.printStackTrace();
        }
    }

}
