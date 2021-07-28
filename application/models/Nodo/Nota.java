package application.models.Nodo;

import application.Constants;
import application.models.Enlace.EnlaceAbs;

import java.util.*;


/**
 * Clase Nota, representa un nodo concreto donde es posible hacer anotaciones.
 * No necesita segmentacion expecial para la conexion de enlaces.
 * Puede conectarse a todos los nodos concretos del diagrama ER mediante enlace tipo Anchor.
 * Tiene un campo de título y otro de contenido.
 */
public class Nota extends NodoAbs {
    public Nota() {
        this.tipo = Constants.NODE_NOTE;
        this.titulo = "";
        this.estilos = new Vector<String>();
        this.contenido = new Vector<String>();
        this.contenido2 = new Vector<String>();
        this.conexionesHacia = new Vector<String>();
        this.conexionesHacia.add(Constants.NODE_ENTITY);
        this.conexionesHacia.add(Constants.NODE_TRIGGER);
        this.conexionesHacia.add(Constants.NODE_VIEW);
        this.conexionesHacia.add(Constants.NODE_SP);
        this.conexionesHacia.add(Constants.NODE_SPRS);
        this.conexionesDesde = new Vector<String>();
        this.conexionesDesde.add(Constants.NODE_ENTITY);
        this.conexionesDesde.add(Constants.NODE_TRIGGER);
        this.conexionesDesde.add(Constants.NODE_VIEW);
        this.conexionesDesde.add(Constants.NODE_SP);
        this.conexionesDesde.add(Constants.NODE_SPRS);
        this.enlaces = new LinkedHashMap<String, EnlaceAbs>();
        this.llenarEstilos();
        this.tieneTitulo = true;
        this.tieneTexto = true;
        this.tieneTexto2 = false;
        this.tieneDivisionCampos = false;
    }

    public Nota(String id, double coordX, double coordY) {
        this.tipo = Constants.NODE_NOTE;
        this.id = id;
        this.titulo = "";
        this.coordX = coordX;
        this.coordY = coordY;
        this.estilos = new Vector<String>();
        this.contenido = new Vector<String>();
        this.contenido2 = new Vector<String>();
        this.conexionesHacia = new Vector<String>();
        this.conexionesHacia.add(Constants.NODE_ENTITY);
        this.conexionesHacia.add(Constants.NODE_TRIGGER);
        this.conexionesHacia.add(Constants.NODE_VIEW);
        this.conexionesHacia.add(Constants.NODE_SP);
        this.conexionesHacia.add(Constants.NODE_SPRS);
        this.conexionesDesde = new Vector<String>();
        this.conexionesDesde.add(Constants.NODE_ENTITY);
        this.conexionesDesde.add(Constants.NODE_TRIGGER);
        this.conexionesDesde.add(Constants.NODE_VIEW);
        this.conexionesDesde.add(Constants.NODE_SP);
        this.conexionesDesde.add(Constants.NODE_SPRS);
        this.enlaces = new LinkedHashMap<String, EnlaceAbs>();
        this.llenarEstilos();
        this.tieneTitulo = true;
        this.tieneTexto = true;
        this.tieneTexto2 = false;
        this.tieneDivisionCampos = false;
    }

    public void llenarEstilos() {
        this.estilos.add("-fx-shape: \"m 0 0 V 7 H 18 V 1 C 16 0 18 1 16 0 Z\";");
        this.estilos.add("-fx-background-color: deepskyblue;");
        this.estilos.add("-fx-border-style: none none dotted none;");
        this.estilos.add("-fx-border-color: black;");
    }

    public boolean validarConexion(NodoAbs nodoExt, EnlaceAbs enlaceExt) {
        // revisa si esta en las conexiones que puede recibir
        if (this.conexionesDesde.contains(nodoExt.getTipo())) {
            // revisa si el tipo del enlace es anchor
            if (enlaceExt.getTipo().equals(Constants.LINK_ANCHOR)) {
                for (Map.Entry<String, EnlaceAbs> entry : this.enlaces.entrySet()) {
                    // si ya habia un enlace con ese nodo retorna falso
                    if (entry.getValue().getDestino() == nodoExt || entry.getValue().getFuente() == nodoExt) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
}
