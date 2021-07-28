package application.models.Nodo.ER;

import application.Constants;
import application.models.Enlace.EnlaceAbs;
import application.models.Nodo.NodoAbs;

import java.util.*;

/**
 * Clase StoredProcedure, es un nodo concreto que representa el componente Stored Procedure de los diagramas ER
 * No necesita segmentacion expecial para la conexion de enlaces.
 * Tiene un campo para título y otro para contenido.
 */
public class StoredProcedure extends NodoAbs {
    public StoredProcedure() {
        this.tipo = Constants.NODE_SP;
        this.titulo = "";
        this.estilos = new Vector<String>();
        this.contenido = new Vector<String>();
        this.contenido2 = new Vector<String>();
        this.conexionesHacia = new Vector<String>();
        this.conexionesHacia.add(Constants.NODE_NOTE);
        this.conexionesHacia.add(Constants.NODE_SPRS);
        this.conexionesDesde = new Vector<String>();
        this.conexionesDesde.add(Constants.NODE_ENTITY);
        this.conexionesDesde.add(Constants.NODE_TRIGGER);
        this.conexionesDesde.add(Constants.NODE_NOTE);
        this.enlaces = new LinkedHashMap<String, EnlaceAbs>();
        this.llenarEstilos();
        this.tieneTitulo = true;
        this.tieneTexto = true;
        this.tieneTexto2 = false;
        this.tieneDivisionCampos = false;
    }

    public StoredProcedure(String id, double coordX, double coordY) {
        this.tipo = Constants.NODE_SP;
        this.id = id;
        this.titulo = "";
        this.coordX = coordX;
        this.coordY = coordY;
        this.estilos = new Vector<String>();
        this.contenido = new Vector<String>();
        this.contenido2 = new Vector<String>();
        this.conexionesHacia = new Vector<String>();
        this.conexionesHacia.add(Constants.NODE_NOTE);
        this.conexionesHacia.add(Constants.NODE_SPRS);
        this.conexionesDesde = new Vector<String>();
        this.conexionesDesde.add(Constants.NODE_ENTITY);
        this.conexionesDesde.add(Constants.NODE_TRIGGER);
        this.conexionesDesde.add(Constants.NODE_NOTE);
        this.enlaces = new LinkedHashMap<String, EnlaceAbs>();
        this.llenarEstilos();
        this.tieneTitulo = true;
        this.tieneTexto = true;
        this.tieneTexto2 = false;
        this.tieneDivisionCampos = false;
    }

    public void llenarEstilos() {
        this.estilos.add("-fx-background-color: yellow;");
        this.estilos.add("-fx-border-style: none none dotted none;");
        this.estilos.add("-fx-border-color: black;");
        this.estilos.add("-fx-border-radius: 10 10 0 0;");
        this.estilos.add("-fx-background-radius: 10 10 0 0;");
    }

    public boolean validarConexion(NodoAbs nodoExt, EnlaceAbs enlaceExt) {
        // revisa si esta en las conexiones que puede recibir
        if (this.conexionesDesde.contains(nodoExt.getTipo())) {
            if(enlaceExt.getTipo().equals(Constants.LINK_GENERIC) || enlaceExt.getTipo().equals(Constants.LINK_ANCHOR)) {
                if (enlaceExt.getTipo().equals(Constants.LINK_GENERIC) && nodoExt.getTipo().compareTo(Constants.NODE_NOTE) == 0) {
                    return false;
                }else if(enlaceExt.getTipo().equals(Constants.LINK_ANCHOR) && nodoExt.getTipo().compareTo(Constants.NODE_NOTE) != 0){
                    return false;
                }
                for (Map.Entry<String, EnlaceAbs> entry : this.enlaces.entrySet()) {
                    // si ya habia un enlace con ese nodo retorna falso
                    if (entry.getValue().getDestino() == nodoExt || entry.getValue().getFuente() == nodoExt) {
                        return false;
                    }
                }
            }else {
                return false;
            }
        }else{
            return false;
        }
        return true;
    }
}
