package application.models.Nodo.ER;

import application.Constants;
import application.models.Enlace.EnlaceAbs;
import application.models.Nodo.NodoAbs;

import java.util.*;

/**
 * Clase entidad, representa un nodo concreto
 * Tiene dos campos de texto, para el titulo, y para el contenido.
 * No necesita segmentacion expecial para la conexion de enlaces.
 */
public class Entity extends NodoAbs {
    public Entity() {
        this.tipo = Constants.NODE_ENTITY;
        this.titulo = "";
        this.estilos = new Vector<String>();
        this.contenido = new Vector<String>();
        this.contenido2 = new Vector<String>();
        this.conexionesHacia = new Vector<String>();
        this.conexionesHacia.add(Constants.NODE_ENTITY);
        this.conexionesHacia.add(Constants.NODE_NOTE);
        this.conexionesDesde = new Vector<String>();
        this.conexionesDesde.add(Constants.NODE_ENTITY);
        this.conexionesDesde.add(Constants.NODE_NOTE);
        this.conexionesDesde.add(Constants.NODE_TRIGGER);
        this.conexionesDesde.add(Constants.NODE_SP);
        this.conexionesDesde.add(Constants.NODE_VIEW);
        this.enlaces = new LinkedHashMap<String, EnlaceAbs>();
        this.llenarEstilos();
        this.tieneTitulo = true;
        this.tieneTexto = true;
        this.tieneTexto2 = false;
        this.tieneDivisionCampos = false;
    }

    public Entity(String id, double coordX, double coordY) {
        this.tipo = Constants.NODE_ENTITY;
        this.id = id;
        this.titulo = "";
        this.coordX = coordX;
        this.coordY = coordY;
        this.estilos = new Vector<String>();
        this.contenido = new Vector<String>();
        this.contenido2 = new Vector<String>();
        this.conexionesHacia = new Vector<String>();
        this.conexionesHacia.add(Constants.NODE_ENTITY);
        this.conexionesHacia.add(Constants.NODE_NOTE);
        this.conexionesDesde = new Vector<String>();
        this.conexionesDesde.add(Constants.NODE_ENTITY);
        this.conexionesDesde.add(Constants.NODE_NOTE);
        this.conexionesDesde.add(Constants.NODE_TRIGGER);
        this.conexionesDesde.add(Constants.NODE_SP);
        this.conexionesDesde.add(Constants.NODE_VIEW);
        this.enlaces = new LinkedHashMap<String, EnlaceAbs>();
        this.llenarEstilos();
        this.tieneTitulo = true;
        this.tieneTexto = true;
        this.tieneTexto2 = false;
        this.tieneDivisionCampos = false;
    }

    public void llenarEstilos() {
        this.estilos.add("-fx-background-color: #f5b67a;");
        this.estilos.add("-fx-border-style: none none dotted none;");
        this.estilos.add("-fx-border-color: black;");
        this.estilos.add("-fx-border-radius: 10;");
        this.estilos.add("-fx-background-radius: 10;");
    }

    public boolean validarConexion(NodoAbs nodoExt, EnlaceAbs enlaceExt) {
        if(this.conexionesDesde.contains(nodoExt.getTipo()) ){
            //Veamos si previamente, ya habian conexiones desde alla, o hacia mi
            //Solo permitimos 1 conexion hacia dado nodo
            if(nodoExt == this){
                //Queremos intentar hacer una relacion recursiva con esta entity, vemos si ya habia una
                for (Map.Entry<String, EnlaceAbs> entry : this.enlaces.entrySet()) {
                    if(entry.getValue().getDestino() == nodoExt && entry.getValue().getFuente() == nodoExt){
                        return false; //Ya hay un enlace entre yo mismo. Ya hay una relacion recursiva
                    }
                }
            }else{
                //Veo si en mi mapa de enlaces, el nodo esta sea en destino o source.
                //Solo permitimos 1 conexion entre nodos, a menos que sea entre entities.
                if(nodoExt.getTipo().equals(Constants.NODE_ENTITY) == false){
                    for (Map.Entry<String, EnlaceAbs> entry : this.enlaces.entrySet()) {
                        if(entry.getValue().getDestino() == nodoExt || entry.getValue().getFuente() == nodoExt){
                            //Ya hay un enlace entre el y yo.
                            return false;
                        }
                    }
                }else{
                    //Caso entity a entity. Pueden haber maximo 2 relaciones entre 2 entities.
                    int count= 0;
                    for (Map.Entry<String, EnlaceAbs> entry : this.enlaces.entrySet()) {
                        if (entry.getValue().getDestino() == nodoExt || entry.getValue().getFuente() == nodoExt) {
                            count++;
                            if (count >= 2) { //Si ya hay dos conexoines entre el y yo, no permita mas
                                return false;
                            }
                        }
                    }
                }
            }
            //Ahora solo queda ver si con esta flecha, es permitida hacer cierta conexion
            String tipoEnlace = enlaceExt.getTipo();
            String tipoNodo = nodoExt.getTipo();
            if(tipoNodo.equals(Constants.NODE_ENTITY) ){ //Solo flechas con cardinalidad permitidas
                if(tipoEnlace.equals(Constants.LINK_MANYTOMANY) ||
                        tipoEnlace.equals(Constants.LINK_ONETOMANY) ||tipoEnlace.equals(Constants.LINK_ONETOONE) ){
                    return true;
                }
            }else if(tipoNodo.equals(Constants.NODE_NOTE) ){
                if(tipoEnlace.equals(Constants.LINK_ANCHOR)){
                    return true;
                }
            }else if (tipoEnlace.equals(Constants.LINK_GENERIC)) {
                    //Caso Trigger, SP, View
                    return true;
                }
        }
        return false;
    }
}
