package application.models.Nodo.Redes;

import application.Constants;
import application.models.Enlace.EnlaceAbs;
import application.models.Nodo.NodoAbs;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
/**
 * Clase Laptop, representa un nodo concreto
 * NO tiene dos campos de texto, para el titulo, y para el contenido.
 * No necesita segmentacion expecial para la conexion de enlaces.
 */
public class Laptop extends NodoAbs {

    public Laptop(){
        this.tipo = Constants.NODE_LAPTOP;
        this.titulo = "";
        this.estilos = new Vector<String>();
        this.contenido = new Vector<String>();
        this.contenido2 = new Vector<String>();
        this.conexionesHacia = new Vector<String>();
        this.conexionesHacia.add(Constants.NODE_LAPTOP);
        this.conexionesHacia.add(Constants.NODE_TELEPHONE);
        this.conexionesHacia.add(Constants.NODE_WLAN);
        this.conexionesHacia.add(Constants.NODE_NOTE);
        this.conexionesDesde = new Vector<String>();
        this.conexionesDesde.add(Constants.NODE_NOTE);
        this.conexionesDesde.add(Constants.NODE_LAPTOP);
        this.conexionesDesde.add(Constants.NODE_TELEPHONE);
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
            if(enlaceExt.getTipo().equals(Constants.LINK_WIRELESS)){
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
        this.estilos.add("-fx-shape: \"M2,54h60c1.104,0,2-0.896,2-2v-6c0-1.104-0.896-2-2-2h-4V12c0-1.104-0.896-2-2-2H8c-1.104,0-2,0.896-2,2v32H2\n" +
                "\t\tc-1.104,0-2,0.896-2,2v6C0,53.104,0.896,54,2,54z M10,14h44v30H10V14z M4,48h4h48h4v2H4V48z\";");
    }
}
