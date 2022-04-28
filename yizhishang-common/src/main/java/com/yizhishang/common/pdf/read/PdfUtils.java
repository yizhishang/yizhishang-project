package com.yizhishang.common.pdf.read;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author
 */
public class PdfUtils {

    public static final String SEPARATE_PAGE_TEXT = "\nPDF解析分页\n";

    /**
     * 按行提取文本
     *
     * @param file
     * @return List<String>
     */
    public static String extractTXTbyLine(String file) {
        StringBuilder sb = new StringBuilder();
        try {
            PdfReader reader = new PdfReader(file);
            // 获得页数
            int pageNum = reader.getNumberOfPages();
            // 只能从第1页开始读
            for (int i = 1; i <= 2; i++) {
                String pageText = PdfTextExtractor.getTextFromPage(reader, i).trim();
                sb.append(pageText).append(SEPARATE_PAGE_TEXT);
            }
        } catch (IOException ex) {
            Logger.getLogger(PdfUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }


    public static void main(String args[]) {
//        String file = "F:\\gitee\\yizhishang-project\\docs\\铁路大票.pdf";
        String file = "E:\\GiteeRepository\\yizhishang-project\\docs\\pdf\\大票3.pdf";
//        String file = "F:\\gitee\\yizhishang-project\\docs\\大票.pdf";
        long startTime = System.currentTimeMillis();
        String result = extractTXTbyLine(file);

        AnalysisUtil.analysis(result, SEPARATE_PAGE_TEXT);

        long endTime = System.currentTimeMillis();
        System.out.println("读写所用时间为：" + (endTime - startTime) + "ms");
    }
}