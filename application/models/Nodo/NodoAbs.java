package application.models.Nodo;

import application.models.Enlace.EnlaceAbs;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.*;

/**
 * Representa lo que pueden tener nodos en diagramas.
 * tieneTitulo, tieneTexto, tieneTexto2 se utilizan como booleanos en caso de que este nodo en especifico solo sea un icono,
 * que sea una nota por ejemplo, o como en el caso de uml , que tenga tres espacios dedicados: titulo, atributos y metodos
 *
 * tieneDivisiondeCampos es utilizada para dividir aun mas los segmentos en los que se puede conectar con un enlace. Esto
 * permite tener flexibilidad y usar un diagrama ER, o bien un diagrama flow o compuertas, en donde solo se pueden hacer
 * conexiones en lugares especificos del nodo. Para ello utilizamos las variables cambosHabilitados y camposOcupados, el nodo
 * se divide en 12 segmentos alrededor de el, y los vectores mencionados indican en donde es posible conectarse, y en donde ya
 * esta ocupado el enlace que estaba habilitado
 */
public abstract class NodoAbs implements Serializable {
    //Nos dice de que tipo es dado Nodo.
    protected String tipo = "";
    //Identifica a este objeto de manera unica
    protected String id = "";
    protected String titulo = "";
    //Posiciones en el canvas
    protected double coordX = -1.0;
    protected double coordY = -1.0;
    protected boolean tieneTitulo;
    protected boolean tieneTexto;
    protected boolean tieneTexto2;
    protected boolean tieneDivisionCampos;
    protected Vector<Integer> camposHabilitados;
    protected Vector<Integer> camposOcupados;
    protected Vector<String> estilos;
    public Map<String, EnlaceAbs> enlaces; //id, EnlaceAbs
    protected Vector<String> contenido; //Las filas de dado contenedor
    protected Vector<String> contenido2; //Las filas de dado contenedor
    protected Vector<String> conexionesHacia;
    protected Vector<String> conexionesDesde;

    public NodoAbs() {
        this.estilos = new Vector<>();
        this.contenido = new Vector<>();
        this.contenido2 = new Vector<>();
        this.conexionesHacia = new Vector<>();
        this.conexionesDesde = new Vector<>();
        this.enlaces = new LinkedHashMap<String, EnlaceAbs>();
        this.camposHabilitados = new Vector<>(12);
        this.camposOcupados = new Vector<>(12);
    }

    public NodoAbs(String id, double coordX, double coordY) {
        this.id = id;
        this.titulo = "";
        this.coordX = coordX;
        this.coordY = coordY;
        this.estilos = new Vector<String>();
        this.contenido = new Vector<String>();
        this.contenido2 = new Vector<>();
        this.conexionesHacia = new Vector<String>();
        this.conexionesDesde = new Vector<String>();
        this.enlaces = new LinkedHashMap<String, EnlaceAbs>();
        this.camposHabilitados = new Vector<>(12);
        this.camposOcupados = new Vector<>(12);
    }

    /**
     * Conseguir el tipo de Nodo
     * @return Tipo de Nodo
     */
    public String getTipo () {
        return this.tipo;
    }

    /**
     * Cambiar el titulo del Nodo
     * @param titulo Nuevo titulo
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Conseguir el titulo del nodo
     * @return Titulo del nodo
     */
    public String getTitulo() {
        return this.titulo;
    }

    /**
     * Conseguir identificador unico del nodo
     * @return Identificador unico del nodo
     */
    public String getId() {
        return this.id;
    }

    /**
     * Conseguir la coordenada X del nodo
     * @return Coordenada X del nodo
     */
    public double getCoordX() {
        return this.coordX;
    }

    /**
     * Conseguir la coordenada Y del nodo
     * @return Coordenada Y del nodo
     */
    public double getCoordY() {
        return this.coordY;
    }

    /**
     * Cambiar la coordenada X del nodo
     * @param x Nueva coordenada X del nodo
     */
    public void setCoordX(double x){
        this.coordX = x;
    }

    /**
     * Cambiar la coordenada Y del nodo
     * @param y Nueva coordenada Y del nodo
     */
    public void setCoordY(double y){
        this.coordY = y;
    }

    /**
     * Cambiar el contenido (campos 1) del nodo
     * @param contenido Nuevo contenido 1
     */
    public void setContenido(Vector<String> contenido){
        this.contenido = contenido;
    }

    /**
     * Cambiar el contenido (campos 2) del nodo
     * @param contenido2 Nuevo contenido 2
     */
    public void setContenido2(Vector<String> contenido2){
        this.contenido2 = contenido2;
    }

    /**
     * Los campos habilitados, son los campos en donde es posible hacer una conexion con determinado nodo.
     * Esto se utiliza en diagramas como flow o compuertas logicas, donde el lugar donde se hace la conexion es
     * imperativo para la visualizacion y funcionamiento del diagrama
     * @return Vector que representa los campos donde este nodo puede conectarse
     */
    public Vector<Integer> getCamposHabilitados() { return camposHabilitados;}

    /**
     * Modificar los campos que representan a que campos se pueden realizar una conexión.
     * @param cA Nuevos campos.
     */
    public void setCamposHabilitados(Vector<Integer> cA) {camposHabilitados=cA;}

    /**
     * Los campos ocupados, son los campos en donde es posible hacer una conexion con determinado nodo, pero en este
     * momento esa posicion esta ocupada por algun enlace.
     * Esto se utiliza en diagramas como flow o compuertas logicas, donde el lugar donde se hace la conexion es
     * imperativo para la visualizacion y funcionamiento del diagrama
     * @return Vector que representa los campos ocupados de este nodo en particular
     */
    public Vector<Integer> getCamposOcupados() { return camposOcupados; }
    public void setCamposOcupados(Vector<Integer> cO) {camposHabilitados=cO;}

    /**
     * Nos indica si este nodo es sensitivo de donde se conectan los nodos. Por ejemplo, en diagramas flow o
     * en diagramas de compuertas logicas. Si es un diagrama ER,o Redes por ejemplo, este booleano esta en falso.
     * @return Si el diagrama es sensible al lugar en donde se pueden conectar enlaces.
     */
    public boolean getTieneDivisionCampos() {return tieneDivisionCampos;}

    /**
     * Cambiar si el nodo es sensitivo a donde se conectan los nodos.
     * @param r true = es sensitivo, false = caso contrario
     */
    public void setTieneDivisionCampos(boolean r) {tieneDivisionCampos=r; }

    /**
     * Verificar si el nodo tiene titulo.
     * @return true tiene titulo, false caso contrario.
     */
    public boolean getTieneTitulo() {return tieneTitulo; }

    /**
     * Modificar si el nodo tiene titulo o no.
     * @param r true = tiene titullo, false caso contrario
     */
    public void setTieneTitulo(boolean r) {tieneTitulo=r; }

    /**
     * Verificar si el nodo tiene texto 1 (Label de arriba).
     * @return true tiene tiene texto 1, false caso contrario.
     */
    public boolean getTieneTexto() { return tieneTexto; }

    /**
     * Modificar si el nodo tiene texto 1 (Label de arriba).
     * @param r true tiene tiene texto 1, false caso contrario.
     */
    public void setTieneTexto(boolean r) {tieneTexto=r; }

    /**
     * Verificar si el nodo tiene texto 2 (Label de abajo).
     * @return true tiene tiene texto 2, false caso contrario.
     */
    public boolean getTieneTexto2() {return tieneTexto2; }

    /**
     * Modificar si el nodo tiene texto 2 (Label de abajo).
     * @param r true tiene tiene texto 2, false caso contrario.
     */
    public void setTieneTexto2(boolean r) {tieneTexto2=r; }

    /**
     *Inserta un enlace entre dos nodos. Es decir, cuando hay una conexion entre dos nodos, se guarda esta tanto
     * en el nodo que origina la conexion, como en el nodo que recibe la conexion.
     * @param id Id del enlace a insertar
     * @param enlace El objeto enlace que representa la conexion entre este y otro nodo
     */
    public void insertarEnlace(String id, EnlaceAbs enlace) { 
        this.enlaces.put(id, enlace);
    }

    /**
     * Del vector de enlaces, removemos un enlace en especifico.
     * @param id Id unico del enlace a eliminar
     */
    public void eliminarEnlace(String id){
        this.enlaces.remove(id);
    }

    /**
     * Devuelve un vector con los id's de todos los enlaces de determindo nodo
     * @return Vector con los id's de los enlaces del nodo.
     */
    public Vector<String> getEnlaces() {
        Vector<String> enlacesId = new Vector<String>();
        for (Map.Entry<String, EnlaceAbs> entry : this.enlaces.entrySet()) {
            enlacesId.add(entry.getKey());
        }
        return enlacesId;
    }

    /**
     * Devuelve un map con los id's de todos los enlaces de determindo nodo y
     * las referencias a los mismos
     * @return Maps con los id's de los enlaces del nodo y sus respectivas referencias.
     */
    public Map<String, EnlaceAbs> getEnlacesMap(){
        return this.enlaces;
    }

    /**
     * Agrega una string que representa contenido a este nodo en particular
     * @param nuevoContenido String a agregar al vector de contenido de este nodo
     */
    public void agregarContenido(String nuevoContenido) {
        this.contenido.add(nuevoContenido);
    }

    /**
     * Elimina una entrada de contenido en el vector, dictada por el parametro pasado.
     * @param index Remueve esta entrada en especifco del vector de contenidos de este nodo.
     */
    public void eliminarContenido(int index){
        this.contenido.remove(index);
    }
    /**
     * Agrega una string que representa contenido a este nodo en particular
     * @param nuevoContenido String a agregar al vector de contenido de este nodo
     */
    public void agregarContenido2(String nuevoContenido) {
        this.contenido2.add(nuevoContenido);
    }
    /**
     * Elimina una entrada de contenido en el vector, dictada por el parametro pasado.
     * @param index Remueve esta entrada en especifco del vector de contenidos de este nodo.
     */
    public void eliminarContenido2(int index){
        this.contenido2.remove(index);
    }

    /**
     * Conseguir los estilos del nodo que permitiran dibujarlo en el canvas (colores, forma, ...)
     * @return Estilos del nodo (fx-css).
     */
    public Vector<String> getEstilos() {
        return this.estilos;
    }

    /**
     * Valida si este nodo puede recibir una conexion de otro nodo, con determinado tipo de enlace.
     * Util cuando hay restricciones en los nodos, y en los tipos de enlace que puede recibir.
     * @param nodoExt El nodo externo, que quiere hacer un enlace con este nodo
     * @param enlaceExt EL tipo de flecha con el que se quiere hacer la conexion
     * @return Si la conexion es permitida o no.
     */
    public abstract boolean validarConexion(NodoAbs nodoExt, EnlaceAbs enlaceExt);

    /**
     * Se encarga de agregar los estilos de este nodo, es llamado al construir una instancia concreta
     * de nodo. Esto permite que hayan nodos de distintas formas, color, etc.
     */
    public abstract void llenarEstilos();

    /**
     * Conseguir los campos del label 1 (arriba)
     * @return Campos del Label 1 (arriba)
     */
    public Vector<String> getContenido() {
        return contenido;
    }

    /**
     * Conseguir los campos del Label 2 (abajo)
     * @return Campos del Label 2 (abajo)
     */
    public Vector<String> getContenido2() {
        return contenido2;
    }

    /**
     *  Cambiar el identificador unico del nodo
     * @param id Nuevo identificador unico del nodo.
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * Modifica lo que dice una entrada del vector de contenidos. O bien, una fila de un nodo, por otra String
     * que el usuario quiera colocar en su lugar.
     * @param id el id de la 'fila' de contenido en este nodo
     * @param newValue la nueva string que estara en vez de la anterior
     */
    public void modificarContenido(int id, String newValue){
        contenido.set(id, newValue);
    }

    /**
     * Modifica lo que dice una entrada del segundo vector de contenidos. O bien, una fila de un nodo, por otra String
     * que el usuario quiera colocar en su lugar.
     * @param id el id de la 'fila' de contenido en este nodo
     * @param newValue la nueva string que estara en vez de la anterior
     */
    public void modificarContenido2(int id, String newValue){
        contenido2.set(id, newValue);
    }
}