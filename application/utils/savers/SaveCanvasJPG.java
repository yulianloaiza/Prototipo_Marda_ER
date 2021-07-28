package application.utils.savers;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * Clase concreta que se utiliza para exportar el canvas a JPG
 */
public class SaveCanvasJPG extends SaveCanvasAbs{
    public SaveCanvasJPG(){}

    /**
     * Exportar el canvas a formato JPG.
     */
    void save() {
        if(file != null){
            try {
                ImageIO.write(canvas, "jpg", file);
            } catch (IOException ex) { ex.printStackTrace(); }
        }
    }
}
