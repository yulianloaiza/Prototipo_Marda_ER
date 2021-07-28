package application.models.Nodo.Redes;

import application.Constants;
import application.models.Enlace.EnlaceAbs;
import application.models.Nodo.NodoAbs;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Clase Firewall, representa un nodo concreto
 * NO tiene dos campos de texto, para el titulo, y para el contenido.
 * No necesita segmentacion expecial para la conexion de enlaces.
 */
public class Firewall extends NodoAbs {
    public Firewall(){
        this.tipo = Constants.NODE_FIREWALL;
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
        this.estilos.add("-fx-shape: \"M473.82,40.283H38.18C17.127,40.283,0,57.411,0,78.463c0,13.495,0,341.346,0,355.074c0,21.052,17.127,38.18,38.18,38.18\n" +
                "\t\t\tH473.82c21.052,0,38.18-17.127,38.18-38.18c0-13.568,0-341.517,0-355.074C512,57.411,494.873,40.283,473.82,40.283z\n" +
                "\t\t\t M367.111,78.463h106.71v92.904h-106.71V78.463z M183.068,78.463h145.863v92.904H183.068V78.463z M38.18,78.463h106.708v92.904\n" +
                "\t\t\tH38.18V78.463z M38.18,209.547h198.73v92.905H38.18V209.547z M144.888,433.536H38.18v-92.904h106.708V433.536z M328.931,433.536\n" +
                "\t\t\tH183.068v-92.904h145.863V433.536z M473.82,433.537h-106.71v-92.904h106.71V433.537z M473.82,302.452H275.09v-92.905h198.73\n" +
                "\t\t\tV302.452z\";");
    }
}
