package application.models.Nodo.Redes;

import application.Constants;
import application.models.Enlace.EnlaceAbs;
import application.models.Nodo.NodoAbs;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Clase Server, representa un nodo concreto
 * NO tiene dos campos de texto, para el titulo, y para el contenido.
 * No necesita segmentacion expecial para la conexion de enlaces.
 */
public class Server extends NodoAbs{
    public Server(){
        this.tipo = Constants.NODE_SERVER;
        this.titulo = "";
        this.estilos = new Vector<String>();
        this.contenido = new Vector<String>();
        this.contenido2 = new Vector<String>();
        this.conexionesHacia = new Vector<String>();
        this.conexionesHacia.add(Constants.NODE_PC);
        this.conexionesHacia.add(Constants.NODE_INTERNET);
        this.conexionesHacia.add(Constants.NODE_SERVER);
        this.conexionesHacia.add(Constants.NODE_FIREWALL);
        this.conexionesHacia.add(Constants.NODE_ROUTER);
        this.conexionesHacia.add(Constants.NODE_SWITCH);
        this.conexionesHacia.add(Constants.NODE_WLAN);
        this.conexionesHacia.add(Constants.NODE_NOTE);
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
        this.estilos.add("-fx-shape: \"m445,460h-24.998v-445c0-8.284-6.716-15-15-15h-320.002c-8.284,0-15,6.716-15,15v445h-25c-8.284,0-15,6.716-15,15s6.716,15 15,15h400c8.284,0 15-6.716 15-15s-6.716-15-15-15zm-54.998,0h-290.002v-430h290.002v430zm-245-160h199.998c8.284,0 15-6.716 15-15v-210c0-8.284-6.716-15-15-15h-199.998c-8.284,0-15,6.716-15,15v210c0,8.284 6.716,15 15,15zm15-210h169.998v40h-169.998v-40zm0,70h169.998v40h-169.998v-40zm0,70h169.998v40h-169.998v-40zm84.998,107.497c-24.813,0-44.999,20.188-44.999,45.004 0,24.813 20.188,45.001 45.001,45.001 24.813,0 44.999-20.188 44.999-45.004-5.68434e-14-24.813-20.188-45.001-45.001-45.001zm0,60.005c-8.271,0-14.999-6.729-14.999-15.004 0-8.271 6.729-15.001 14.999-15.001h0.002c8.271,0 14.999,6.729 14.999,15.004-5.68434e-14,8.271-6.73,15.001-15.001,15.001z\";");
    }
}
