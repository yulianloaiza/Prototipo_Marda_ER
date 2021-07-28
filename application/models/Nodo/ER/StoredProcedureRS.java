package application.models.Nodo.ER;

import application.Constants;
import application.models.Enlace.EnlaceAbs;
import application.models.Nodo.NodoAbs;

import java.util.*;

/**
 * Clase StoredProcedureRS, es un nodo concreto que representa el componente Stored Procedure Result Set de los diagramas ER
 * No necesita segmentacion expecial para la conexion de enlaces.
 * Tiene un campo para título y otro para contenido.
 */
public class StoredProcedureRS extends NodoAbs {
    public StoredProcedureRS() {
        this.tipo = Constants.NODE_SPRS;
        this.titulo = "";
        this.estilos = new Vector<String>();
        this.contenido = new Vector<String>();
        this.contenido2 = new Vector<String>();
        this.conexionesHacia = new Vector<String>();
        this.conexionesHacia.add(Constants.NODE_NOTE);
        this.conexionesDesde = new Vector<String>();
        this.conexionesDesde.add(Constants.NODE_NOTE);
        this.conexionesDesde.add(Constants.NODE_SP);
        this.enlaces = new LinkedHashMap<String, EnlaceAbs>();
        this.llenarEstilos();
        this.tieneTitulo = true;
        this.tieneTexto = true;
        this.tieneTexto2 = false;
        this.tieneDivisionCampos = false;
    }

    public StoredProcedureRS(String id, double coordX, double coordY) {
        this.tipo = Constants.NODE_SPRS;
        this.id = id;
        this.titulo = "";
        this.coordX = coordX;
        this.coordY = coordY;
        this.estilos = new Vector<String>();
        this.contenido = new Vector<String>();
        this.contenido2 = new Vector<String>();
        this.conexionesHacia = new Vector<String>();
        this.conexionesHacia.add(Constants.NODE_NOTE);
        this.conexionesDesde = new Vector<String>();
        this.conexionesDesde.add(Constants.NODE_NOTE);
        this.conexionesDesde.add(Constants.NODE_SP);
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
