package application.utils.loaders;

import application.models.Nodo.NodoAbs;
import application.utils.factories.FactoryAbs;

import java.util.Vector;

/**
 * Clase abstracta de la cual heredan los deserializadores (Loaders) a implementar
 * 'enlacesNuevos', y 'nodosAbs' son vectores que se pueden accesar desde RootLayout, para la insercion
 * de los mismos al canvas de la aplicacion.
 * 'factory' nos permite especificar el tipo de fabrica a utilizar, ya que depende de los objetos que estemos
 * cargando
 */
public abstract class LoaderAbs {

    protected Vector<Vector<String>> enlacesNuevos;
    protected Vector<NodoAbs> nodosAbs;
    FactoryAbs factory = null;

    public LoaderAbs(){}

    /**
     * Con una ruta de archivo especificada, crea objetos a partir del archivo donde estan guardados.
     * Dependiendo del contenido del archivo, escoge cual tipo de fabrica de diagramas usar para
     * crear los objetos
     * @param path Ruta del archivo a cargar
     * @return factory Tipo de fabrica que según el tipo de diagrama.
     */
    public abstract FactoryAbs cargar(String path);

    /**
     * Conseguir todos lo enlaces generados cuando se cargó el archivo
     * @return Todos los enlaces que se crearán en el canvas
     */
    public Vector<Vector<String>> getEnlacesNuevos(){ return this.enlacesNuevos;}

    /**
     * Conseguir todos lo nodos generados cuando se cargó el archivo
     * @return Todos los nodos que se crearán en el canvas
     */
    public Vector<NodoAbs> getNodosAbs(){ return this.nodosAbs;}
}
