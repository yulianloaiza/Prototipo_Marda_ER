package application.models.Nodo.ER;

import application.Constants;
import application.models.Enlace.EnlaceAbs;
import application.models.Nodo.NodoAbs;

import java.util.*;

/**
 * Clase trigger, representa un nodo concreto
 * Tiene dos campos de texto, para el titulo, y para el contenido.
 * No necesita segmentacion expecial para la conexion de enlaces.
 */
public class Trigger extends NodoAbs {
    public Trigger() {
        this.tipo = Constants.NODE_TRIGGER;
        this.titulo = "";
        this.estilos = new Vector<String>();
        this.contenido = new Vector<String>();
        this.contenido2 = new Vector<String>();
        this.conexionesHacia = new Vector<String>();
        this.conexionesHacia.add(Constants.NODE_ENTITY);
        this.conexionesHacia.add(Constants.NODE_VIEW);
        this.conexionesHacia.add(Constants.NODE_NOTE);
        this.conexionesDesde = new Vector<String>();
        this.conexionesDesde.add(Constants.NODE_NOTE);
        this.enlaces = new LinkedHashMap<String, EnlaceAbs>();
        this.llenarEstilos();
        this.tieneTitulo = true;
        this.tieneTexto = true;
        this.tieneTexto2 = false;
        this.tieneDivisionCampos = false;
    }

    public Trigger(String id, double coordX, double coordY) {
        this.tipo = Constants.NODE_TRIGGER;
        this.id = id;
        this.titulo = "";
        this.coordX = coordX;
        this.coordY = coordY;
        this.estilos = new Vector<String>();
        this.contenido = new Vector<String>();
        this.contenido2 = new Vector<String>();
        this.conexionesHacia = new Vector<String>();
        this.conexionesHacia.add(Constants.NODE_ENTITY);
        this.conexionesHacia.add(Constants.NODE_VIEW);
        this.conexionesHacia.add(Constants.NODE_NOTE);
        this.conexionesDesde = new Vector<String>();
        this.conexionesDesde.add(Constants.NODE_NOTE);
        this.enlaces = new LinkedHashMap<String, EnlaceAbs>();
        this.llenarEstilos();
        this.tieneTitulo = true;
        this.tieneTexto = true;
        this.tieneTexto2 = false;
        this.tieneDivisionCampos = false;
    }

    public void llenarEstilos() {
        this.estilos.add("-fx-shape: \"m 0 0 H 11 V 6 H 0 C 1 5 1 1 0 0\";");
        this.estilos.add("-fx-background-color: #86dae5;");
        this.estilos.add("-fx-border-style: none none dotted none;");
        this.estilos.add("-fx-border-color: black;");
    }

    public boolean validarConexion(NodoAbs nodoExt, EnlaceAbs enlaceExt) {
        if(this.conexionesDesde.contains(nodoExt.getTipo()) ){
            //Veamos si previamente, ya habian conexiones desde alla, o hacia mi
            //Solo permitimos 1 conexion hacia dado nodo
            //Veo si en mi mapa de enlaces, el nodo esta sea en destino o source
            for (Map.Entry<String, EnlaceAbs> entry : this.enlaces.entrySet()) {
                if(entry.getValue().getDestino() == nodoExt || entry.getValue().getFuente() == nodoExt){
                    //Ya hay un enlace entre el y yo.
                    return false;
                }
            }
            //Ahora solo queda ver si con esta flecha, es permitida hacer la conexion
            if(enlaceExt.getTipo().equals(Constants.LINK_ANCHOR) ){
                return true; //Conexiones desde solo incluye nota.
            }
        }
        return false;
    }
}
