package application.models.Nodo.Redes;

import application.Constants;
import application.models.Enlace.EnlaceAbs;
import application.models.Nodo.NodoAbs;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Clase Router, representa un nodo concreto
 * NO tiene dos campos de texto, para el titulo, y para el contenido.
 * No necesita segmentacion expecial para la conexion de enlaces.
 */
public class Router extends NodoAbs {

    public Router() {
        this.tipo = Constants.NODE_ROUTER;
        this.titulo = "";
        this.estilos = new Vector<String>();
        this.contenido = new Vector<String>();
        this.contenido2 = new Vector<String>();
        this.conexionesHacia = new Vector<String>();
        this.conexionesHacia.add(Constants.NODE_LAPTOP);
        this.conexionesHacia.add(Constants.NODE_ROUTER);
        this.conexionesHacia.add(Constants.NODE_NOTE);
        this.conexionesDesde = new Vector<String>();
        this.conexionesDesde.add(Constants.NODE_NOTE);
        this.conexionesDesde.add(Constants.NODE_ROUTER);
        this.conexionesDesde.add(Constants.NODE_LAPTOP);
        this.enlaces = new LinkedHashMap<String, EnlaceAbs>();
        this.llenarEstilos();
        this.tieneTitulo = false;
        this.tieneTexto = false;
        this.tieneTexto2 = false;
        this.tieneDivisionCampos = false;
    }


    @Override
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
            if(enlaceExt.getTipo().equals(Constants.LINK_WIRELESS)){
                return true; //Conexiones desde solo incluye nota.
            }
        }
        return false;
    }

    @Override
    public void llenarEstilos() {
        this.estilos.add("-fx-background-color: black;");
        this.estilos.add("-fx-shape: \"M383.4,175.321l-115-115c-8.3-8.3-20.8-8.3-29.1,0c-8.3,8.3-8.3,20.8,0,29.1l85.3,85.3H115.5\n" +
                "\t\t\tC52,174.821,0,226.821,0,290.321c0,51.9,34.8,96.1,82.2,110.5v12.2c0,11.4,9.4,20.8,20.8,20.8s20.8-9.4,20.8-20.8v-7.3h241.4v7.3\n" +
                "\t\t\tc0,11.4,9.4,20.8,20.8,20.8s20.8-9.4,20.8-20.8v-12.2c47.4-14.4,82-58.6,81.1-110.5C488,230.521,441.8,180.921,383.4,175.321z\n" +
                "\t\t\t M372.5,365.221h-257c-41.6,0-74.9-33.3-74.9-74.9s33.3-74.9,74.9-74.9h257c41.6,0,74.9,33.3,74.9,74.9\n" +
                "\t\t\tS414.1,365.221,372.5,365.221z\";");
        this.estilos.add("-fx-border-style: none none dotted none;");
        this.estilos.add("-fx-border-color: black;");
    }
}
