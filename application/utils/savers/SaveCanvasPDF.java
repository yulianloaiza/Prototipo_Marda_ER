package application.utils.savers;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Clase concreta que se utiliza para exportar el canvas a PDF
 */
public class SaveCanvasPDF extends SaveCanvasAbs {
    public SaveCanvasPDF(){

    }
    /**
     * Exportar el canvas a formato PNG.
     */
    void save(){
        float POINTS_PER_INCH = 72;
        float POINTS_PER_MM = 1 / (10 * 2.54f) * POINTS_PER_INCH;
        try {
            PDDocument doc = new PDDocument();
            PDPage page = new PDPage(new PDRectangle(297 * POINTS_PER_MM, 210 * POINTS_PER_MM));
            PDImageXObject pdimage;
            PDPageContentStream content;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(canvas, "png", out);
            pdimage = PDImageXObject.createFromByteArray(doc, out.toByteArray() , "png");
            content = new PDPageContentStream(doc, page);

            PDRectangle box = page.getMediaBox();
            double factor = Math.min(box.getWidth() / canvas.getWidth(), box.getHeight() / canvas.getHeight());
            float height = (float) (canvas.getHeight() * factor);

            content.drawImage(pdimage, 0, box.getHeight() - height, (float) (canvas.getWidth() * factor), height);
            content.close();
            doc.addPage(page);
            doc.save(file);
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
