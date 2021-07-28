package application.models.Enlace;

import application.models.Nodo.NodoAbs;

import java.io.Serializable;

/**
 * Representa lo que pueden enlaces en los diagramas.
 * 'estiloInicio', 'estiloFinal' se usan para cambiar el estilo del inicio o final de la flecha
 *'isDashed' nos dice si la flecha es punteada o no
 * 'fuente' es el nodo del que se origina la flecha, y 'destino' el destino de la flecha.
 * 'puertoFuente' y 'puertoDestino' se utilizan en el caso de que se necesitara la division en
 * segmentos de nodo. En ese caso, cada enlace guarda en que puerto en especifico esta conectado
 * el enlace al nodo. En casos de diagramas flow o compuertas logicas por ejemplo
 */
public abstract class EnlaceAbs implements Serializable { //Necesita ser Serializable para el drag and drop
    protected String tipo= ""; //Nos dice de que tipo es dado Enlace. Util para validarConexion
    protected String id = "";
    protected String estiloInicio = ""; //Como se ve la flecha de inicio
    protected String estiloFinal= ""; //Como se ve la flecha de final
    protected boolean isDashed = false;//Nos dice si es una linea dasheada o no
    protected NodoAbs fuente;
    protected NodoAbs destino;
    protected String rellenoInicio = "";
    protected String rellenoFinal = "";
    protected int puertoFuente;
    protected int puertoDestino;

    public EnlaceAbs() {
    }

    public EnlaceAbs(String id) {
        this.id= id;
        this.fuente = null;
        this.destino = null;
    }

    /**
     * Guardar una referencia a la fuente de enlace
     * @param fuen Fuente del enlace
     */
    public void setFuente(NodoAbs fuen){
        this.fuente= fuen;
    }

    /**
     * Guardar una referencia al destino del enlace
     * @param dest Destino del enlace
     */
    public void setDestino(NodoAbs dest){
        this.destino= dest;
    }

    /**
     * Modificar el puerto fuente
     * @param puertoFuente Valor que representa el puerto fuente
     */
    public void setPuertoFuente(int puertoFuente) { this.puertoFuente = puertoFuente; }

    /**
     * Modificar el puerto de destino
     * @param puertoDestino Valor que representa el puerto de destino
     */
    public void setPuertoDestino(int puertoDestino) { this.puertoDestino = puertoDestino; }

    /**
     * Conseguir el tipo de enlace
     * @return Tipo de enlace
     */
    public String getTipo() {
        return this.tipo;
    }

    /**
     * Conseguir identificador unico del enlace
     * @return Tipo de enlace
     */
    public String getId() {
        return this.id;
    }

    /**
     * Conseguir la fuente del enlace
     * @return Fuente de enlace
     */
    public NodoAbs getFuente() {
        return this.fuente;
    }

    /**
     * Conseguir el destino del enlace
     * @return Destino del enlace
     */
    public NodoAbs getDestino() {
        return this.destino;
    }

    /**
     * Vetificar si el enlace es dibujado con una linea punteada
     * @return true = punteada, Normal en caso contrario
     */
    public boolean isDashed(){
        return this.isDashed;
    }

    /**
     * Conseguir el dibujo de la flecha de inicio
     * @return Dibujo en formato SVG (String)
     */
    public String getStyleStart(){
        return estiloInicio;
    }

    /**
     * Conseguir el dibujo de la flecha del final
     * @return Dibujo en formato SVG (String)
     */
    public String getStyleEnd(){
        return estiloFinal;
    }

    /**
     * Conseguir el puerto de destino del enlace
     * @return Valor que representa el puerto de destino
     */
    public int getPuertoDestino() { return puertoDestino; }

    /**
     * Conseguir el puerto fuente del enlace
     * @return Valor que representa el puerto fuente
     */
    public int getPuertoFuente() { return puertoFuente; }

    /**
     * Modificar el identificador unico del enlace
     * @param id Nuevo identificador unico
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * Se encarga de agregar los estilos de este enlace, es llamado al construir una instancia concreta
     * de enlace. Esto permite que hayan enlaces con distinta apariencia, tanto al inicio como al final
     * de la flecha.
     */
    public abstract void llenarEstilos();

    /**
     * Conseguir el relleno de la flecha de inicio
     * @return Relleño (fx-css)
     */
    public String getRellenoInicio(){
        return rellenoInicio;
    }

    /**
     * Conseguir el relleno de la flecha del final
     * @return Relleño (fx-css)
     */
    public String getRellenoFinal(){
        return rellenoFinal;
    }
}