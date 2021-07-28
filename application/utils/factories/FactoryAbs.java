package application.utils.factories;

import application.models.Enlace.EnlaceAbs;
import application.models.Nodo.NodoAbs;
import java.util.*;
import java.util.ArrayList;
/**
 * Clase FactoryAbs, representa una fábrica abstracta para la construcción de fábricas concretas para diagramas en específico.
 * Almacena los tipos de nodos y de enlaces que forman parte del diagrama al que le pertenecen.
 */
public abstract class FactoryAbs {
    private String type_factory = "";
    protected ArrayList<NodoAbs> types_nodes = new ArrayList<>();
    protected ArrayList<EnlaceAbs> types_edges = new ArrayList<>();
    protected ArrayList<String> nodes_str = new ArrayList<>();
    protected ArrayList<String> edges_str = new ArrayList<>();

    /**
     * Conseguir todos los tipos de Nodos (concretos) que la fabrica puede construir
     * @return Nodos concretos que se pueden construir
     */
    public ArrayList<NodoAbs> getTypesNodes() {
        return this.types_nodes;
    }

    /**
     * Conseguir todos los tipos de Enlaces (concretos) que la fabrica puede construir
     * @return Enlaces concretos que se pueden construir
     */
    public ArrayList<String> getTypesEdges(){
        return  this.edges_str;
    }

    /**
     * Modificar el tipo de fabrica
     * @param type Nuevo tipo de fabrica
     */
    public void setTypeFactory(String type){
        this.type_factory = type;
    }

    /**
     * Conseguir el tipo de fabrica
     * @return Tipo de fabrica
     */
    public String getTypeFactory(){
        return this.type_factory;
    }

    /**
     * Construye un objeto nodo en base al tipo que se le indica.
     * @param type que representa el tipo de nodo que debe construir.
     * @return Clase concreta de nodo construida
     */
    public abstract NodoAbs buildNodo(String type);

    /**
     * Construye un objeto enlace en base al tipo que se le indica.
     * @param type que representa el tipo de enlace que debe construir.
     * @return Clase concreta de enlace construida
     */
    public abstract EnlaceAbs buildEnlace(String type);

    /**
     * Almacena en el arreglo de Strings los tipos de nodos que existen
     * en el diagrama al que pertenece.
     */
    protected abstract void setupNodesStr();

    /**
     * Almacena en el arreglo de Strings los tipos de enlace que existen
     * en el diagrama al que pertenece.
     */
    protected abstract void setupEdgesStr();

    /**
     * Crea las meta clases para el diagrama al que pertenece en base a los
     * arreglos setupEdgesStr y setupNodesStr.
     */
    protected abstract void createMetaClasses();
}
