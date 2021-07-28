package application.models.Nodo.Redes;

import application.Constants;
import application.models.Enlace.EnlaceAbs;
import application.models.Nodo.NodoAbs;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Clase PC, representa un nodo concreto
 * NO tiene dos campos de texto, para el titulo, y para el contenido.
 * No necesita segmentacion expecial para la conexion de enlaces.
 */
public class PC extends NodoAbs{
    public PC(){
        this.tipo = Constants.NODE_PC;
        this.titulo = "";
        this.estilos = new Vector<String>();
        this.contenido = new Vector<String>();
        this.contenido2 = new Vector<String>();
        this.conexionesHacia = new Vector<String>();
        this.conexionesDesde = new Vector<String>();
        this.conexionesDesde.add(Constants.NODE_NOTE);
        this.conexionesDesde.add(Constants.NODE_PC);
        this.conexionesDesde.add(Constants.NODE_INTERNET);
        this.conexionesDesde.add(Constants.NODE_SERVER);
        this.conexionesDesde.add(Constants.NODE_FIREWALL);
        this.conexionesDesde.add(Constants.NODE_ROUTER);
        this.conexionesDesde.add(Constants.NODE_SWITCH);
        this.conexionesDesde.add(Constants.NODE_WLAN);
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
            if(enlaceExt.getTipo().equals(Constants.LINK_WIRED)){
                return true; //Conexiones desde solo incluye nota.
            }
        }
        return false;
    }

    @Override
    public void llenarEstilos() {
        this.estilos.add("-fx-background-color: black;");
        this.estilos.add("-fx-border-style: none none dotted none;");
        this.estilos.add("-fx-border-color: black;");
        this.estilos.add("-fx-shape: \"M292.697,19.995H27.303C12.223,19.995,0,32.219,0,47.299v163.822c0,15.079,12.223,27.304,27.303,27.304H133.79v24.7\n" +
                "\t\th-21.544c-10.185,0-18.439,8.255-18.439,18.44c0,10.184,8.255,18.44,18.439,18.44h95.508c10.186,0,18.439-8.256,18.439-18.44\n" +
                "\t\tc0-10.185-8.254-18.44-18.439-18.44H186.21v-24.7h106.487c15.08,0,27.303-12.225,27.303-27.304V47.299\n" +
                "\t\tC320,32.219,307.777,19.995,292.697,19.995z M288.328,206.752H31.672V51.667h256.656V206.752z\";");
    }
}
