package application.models.Nodo.Redes;

import application.Constants;
import application.models.Enlace.EnlaceAbs;
import application.models.Nodo.NodoAbs;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Clase Switch, representa un nodo concreto
 * NO tiene dos campos de texto, para el titulo, y para el contenido.
 * No necesita segmentacion expecial para la conexion de enlaces.
 */
public class Switch extends NodoAbs {
    public Switch(){
        this.tipo = Constants.NODE_SWITCH;
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
        this.estilos.add("-fx-shape: \"M 100 -399.4 v -1122.9 h 4900 h 4900 L 9902 -4283 v 1122.9 V 723.5 H 5000 H 100 V -399.4 z M 8062.5 -93.2 v -204.2 h -306.3 H 7450 v 204.2 V 111 h 306.2 h 306.3 V -93.2 z M 9287.5 -93.2 v -204.2 h -306.3 H 8675 v 204.2 V 111 h 306.3 h 306.3 V -93.2 z M 1325 -501.5 v -204.2 h -204.2 H 916.7 v 204.2 v 204.2 h 204.2 H 1325 V -501.5 z M 2141.7 -501.5 v -204.2 h -204.2 h -204.2 v 204.2 v 204.2 h 204.2 h 204.2 V -501.5 z M 2958.3 -501.5 v -204.2 h -204.2 H 2550 v 204.2 v 204.2 h 204.2 h 204.2 V -501.5 z M 3775 -501.5 v -204.2 h -204.2 h -204.2 v 204.2 v 204.2 h 204.2 H 3775 V -501.5 z M 4591.7 -501.5 v -204.2 h -204.2 h -204.2 v 204.2 v 204.2 h 204.2 h 204.2 V -501.5 z M 5408.3 -501.5 v -204.2 h -204.2 H 5000 v 204.2 v 204.2 h 204.2 h 204.2 V -501.5 z M 6225 -501.5 v -204.2 h -204.2 h -204.2 v 204.2 v 204.2 h 204.2 H 6225 V -501.5 z M 7041.7 -501.5 v -204.2 h -204.2 h -204.2 v 204.2 v 204.2 h 204.2 h 204.2 V -501.5 z M 8062.5 -705.7 v -204.2 h -306.3 H 7450 v 204.2 v 204.2 h 306.2 h 306.3 V -705.7 z M 9287.5 -705.7 v -204.2 h -306.3 H 8675 v 204.2 v 204.2 h 306.3 h 306.3 V -705.7 z\";");
    }
}
