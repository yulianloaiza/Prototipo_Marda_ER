package application.utils.serializers;

import application.models.Nodo.NodoAbs;

/**
 * Clase abstracta de la cual heredan los serializadores (Constructores) a implementar
 * 'path' nos indica la ubicacion donde guardar el archivo
 * 'tipoDiagrama' es el tipo de diagrama a serializar, util para cuando se vayan a cargar los elementos al
 * canvas de regreso
 */
public abstract class ConstructorSerializerAbs {

    protected String path;
    protected String tipoDiagrama;

    public ConstructorSerializerAbs(){};

    /**
     * Serializa determinado nodo segun la tecnica a utilizar
     * @param nodo Nodo a serializar
     */
    public abstract void serialize(NodoAbs nodo);

    /**
     * Se encarga de inicializar las variables requeridas para la serializacion, ademas de crear
     * el escritor que se usara para guardar el documento cuando este completo el proceso de serialziar
     * @param path Ruta donde se guardara el arhivo
     * @param typeDiagram El tipo de diagrama a guardar
     */
    public abstract void iniciar(String path, String typeDiagram);

    /**
     * Cierra el escritor, guarda el archivo. Despues de esto el archivo se ha guardado exitosamente.
     */
    public abstract void finalizar();
}
