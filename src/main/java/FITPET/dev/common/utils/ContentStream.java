package FITPET.dev.common.utils;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.io.IOException;

public class ContentStream {
    private PDPageContentStream pageContentStream;
    private PDFont font;

    public ContentStream(PDPageContentStream pageContentStream, PDFont font) {
        this.pageContentStream = pageContentStream;
        this.font = font;
    }

    public void setFontSize(int fontSize){
        try {
            pageContentStream.setFont(font, fontSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setColor(float r, float g, float b){
        try {
            this.pageContentStream.setNonStrokingColor(r, g, b);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeText(float x, float y, String text){
        try {
            pageContentStream.beginText();
            pageContentStream.newLineAtOffset(x, y);
            pageContentStream.showText(text);
            pageContentStream.endText();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close(){
        try {
            this.pageContentStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
