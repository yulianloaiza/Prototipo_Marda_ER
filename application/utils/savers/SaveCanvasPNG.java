package application.utils.savers;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * Clase concreta que se utiliza para exportar el canvas a PNG
 */
public class SaveCanvasPNG extends SaveCanvasAbs {
    public SaveCanvasPNG(){
    }
    /**
     * Exportar el canvas a formato PNG.
     */
    void save() {
        if(file != null){
            try {
                ImageIO.write(canvas, "png", file);
            } catch (IOException ex) { ex.printStackTrace(); }
        }
    }
}
