package com.yizhishang.common.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.*;

/**
 * @author yizhishang
 * @emial 285206405@qq.com
 * @time 2018/12/26 0026 下午 2:21
 */
public class HeadFootInfoPdfPageEvent extends PdfPageEventHelper {

    public PdfTemplate tpl;
    BaseFont bfChinese;

    //无参构造方法
    public HeadFootInfoPdfPageEvent(BaseFont bfChinese) {
        super();
        this.bfChinese = bfChinese;
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        tpl = writer.getDirectContent().createTemplate(100, 20);
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            PdfContentByte headAndFootPdfContent = writer.getDirectContent();
            headAndFootPdfContent.saveState();
            headAndFootPdfContent.beginText();
            //设置中文
            headAndFootPdfContent.setFontAndSize(bfChinese, 12);
            //文档页头信息设置
//            float x = document.top(-20);
//            float x1 = document.top(-5);

            //页头信息中间
//            headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_CENTER, pdfName, (document.right() + document.left()) / 2, x, 0);

            //页头信息左面
//            headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_LEFT, riqi[0] + "年" + riqi[1] + "月" + riqi[2] + "日", document.left() + 100, x1, 0);

            //页头信息中间
//            headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_CENTER, type + "库单号：" + code + "", (document.right() + document.left()) / 2, x1, 0);

            //页头信息右面
//            headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_RIGHT, " 单位：册", document.right() - 100, x1, 0);
            //文档页脚信息设置
//            float y = document.bottom(-20);
            float y1 = document.bottom(-15);

            //页脚信息左面
//            headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_LEFT, "储运部负责人：", document.left() + 100, y, 0);

            //页脚信息中间
//            headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_CENTER, " 库管员： ", (document.right() + document.left()) / 2, y, 0);

            //页脚信息右面
//            headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_RIGHT, " 经手人：", document.right() - 100, y, 0);

            //添加页码
            //页脚信息中间
            headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_CENTER, "--第" + document.getPageNumber(), (document.right() + document.left()) / 2, y1, 0);

            //在每页结束的时候把“第x页”信息写道模版指定位置
            //定位“y页” 在具体的页面调试时候需要更改这xy的坐标
            headAndFootPdfContent.addTemplate(tpl, (document.right() + document.left()) / 2 + 15, y1);
            headAndFootPdfContent.endText();
            headAndFootPdfContent.restoreState();
        } catch (Exception de) {
            de.printStackTrace();
        }
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        //关闭document的时候获取总页数，并把总页数按模版写道之前预留的位置
        tpl.beginText();
        tpl.setFontAndSize(bfChinese, 12);
        tpl.showText("页,共" + Integer.toString(writer.getPageNumber() - 1) + "页--");
        tpl.endText();
        tpl.closePath();//sanityCheck();
    }
}