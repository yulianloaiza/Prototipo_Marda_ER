package application.utils.savers;

import java.awt.image.RenderedImage;
import java.io.File;

/**
 * Clase abstracta que se utiliza para exportar el canvas
 */
public abstract class SaveCanvasAbs {

    protected RenderedImage canvas;
    protected File file;

    public SaveCanvasAbs(){
    }

    /**
     * Método plantilla que se encarga de exportar un diagrama.
     * @param file Directorio donde se guardará el archivo
     * @param canvas Canvas donde estan los elementos del diagrama.
     */
    public void saveCanvas(File file, RenderedImage canvas){
        this.canvas = canvas;
        this.file = file;
        this.save();
    }

    /**
     * Método abstracto que varía según el tipo de export.
     */
    abstract void save();
}
