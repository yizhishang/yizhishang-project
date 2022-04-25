package com.yizhishang.common.pdf.read;

import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

/**
 * @author yizhishang
 */
public class MyTextExtractionStrategy extends SimpleTextExtractionStrategy {
    boolean empty = true;

    @Override
    public void beginTextBlock() {
        if (!empty) {
            appendTextChunk("<BLOCK>");
        }
        super.beginTextBlock();
    }

    @Override
    public void endTextBlock() {
        if (!empty) {
            appendTextChunk("</BLOCK>\n");
        }
        super.endTextBlock();
    }

    @Override
    public String getResultantText() {
        if (empty) {
            return super.getResultantText();
        } else {
            return "<BLOCK>" + super.getResultantText();
        }
    }

    @Override
    public void renderText(TextRenderInfo renderInfo) {
        empty = false;
        super.renderText(renderInfo);
    }

}