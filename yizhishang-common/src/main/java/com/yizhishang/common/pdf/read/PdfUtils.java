package com.yizhishang.common.pdf.read;

import com.google.common.collect.Lists;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.IOException;
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
    public static List<String> readPageContent(String file) {
        List<String> page = Lists.newArrayList();
        try {
            PdfReader reader = new PdfReader(file);
            // 获得页数
            int pageNum = reader.getNumberOfPages();
            // 只能从第1页开始读
            for (int i = 1; i <= 1; i++) {
                String pageText = PdfTextExtractor.getTextFromPage(reader, i).trim();
                page.add(pageText);
            }
        } catch (IOException ex) {
            Logger.getLogger(PdfUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return page;
    }


    public static void main(String args[]) {
//        String file = "F:\\gitee\\yizhishang-project\\docs\\铁路大票.pdf";
//        String file = "E:\\GiteeRepository\\yizhishang-project\\docs\\pdf\\大票5.pdf";
        String file = "E:\\GiteeRepository\\yizhishang-project\\docs\\pdf\\document (32).pdf";
//        String file = "F:\\gitee\\yizhishang-project\\docs\\大票.pdf";
        long startTime = System.currentTimeMillis();
//        String result = extractTXTbyLine(file);
//        readPageContent(file);
        AnalysisUtil.analysis(readPageContent(file));

        long endTime = System.currentTimeMillis();
        System.out.println("读写所用时间为：" + (endTime - startTime) + "ms");
    }
}