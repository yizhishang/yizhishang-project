package com.yizhishang.common.pdf.read;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author
 */
public class PdfUtils {


    /**
     * 按行提取文本
     *
     * @param file
     * @return List<String>
     */
    public static List<String> extractTXTbyLine(String file) {
        List<String> listArr = new ArrayList<String>();
        try {
            PdfReader reader = new PdfReader(file);
            // 获得页数
            int pageNum = reader.getNumberOfPages();
            // 只能从第1页开始读
            for (int i = 1; i <= 1; i++) {
                PdfReaderContentParser parser = new PdfReaderContentParser(reader);
                String textFromPageContent = parser.processContent(i, new MyTextExtractionStrategy()).getResultantText();
                String[] splitArray = textFromPageContent.split("\n");
                if (splitArray.length > 0) {
                    listArr.addAll(Arrays.asList(splitArray));
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PdfUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listArr;
    }


    public static void main(String args[]) {
        String file = "F:\\gitee\\yizhishang-project\\docs\\铁路大票.pdf";
        long startTime = System.currentTimeMillis();
        List<String> strings = extractTXTbyLine(file);
        for (String s : strings) {
            System.out.println(s);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("读写所用时间为：" + (endTime - startTime) + "ms");
    }
}