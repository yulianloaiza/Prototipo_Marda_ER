package application.models.Nodo.Redes;

import application.Constants;
import application.models.Enlace.EnlaceAbs;
import application.models.Nodo.NodoAbs;

import java.util.LinkedHashMap;
import java.util.Vector;

/**
 * Clase Wlan Controller, representa un nodo concreto
 * NO tiene dos campos de texto, para el titulo, y para el contenido.
 * No necesita segmentacion expecial para la conexion de enlaces.
 */
public class WLAN extends NodoAbs{
    public WLAN(){
        this.tipo = Constants.NODE_WLAN;
        this.titulo = "";
        this.estilos = new Vector<String>();
        this.contenido = new Vector<String>();
        this.contenido2 = new Vector<String>();
        this.conexionesHacia = new Vector<String>();
        this.conexionesDesde = new Vector<String>();
        this.enlaces = new LinkedHashMap<String, EnlaceAbs>();
        this.llenarEstilos();
        this.tieneTitulo = false;
        this.tieneTexto = false;
        this.tieneTexto2 = false;
        this.tieneDivisionCampos = false;
    }

    @Override
    public boolean validarConexion(NodoAbs nodoExt, EnlaceAbs enlaceExt) {
        if(nodoExt.getTipo().equals(Constants.NODE_LAPTOP) || nodoExt.getTipo().equals(Constants.NODE_TELEPHONE)){
            return enlaceExt.getTipo().equals(Constants.LINK_WIRELESS);
        }
        return true;
    }

    @Override
    public void llenarEstilos() {
        this.estilos.add("-fx-background-color: black;");
        this.estilos.add("-fx-border-style: none none dotted none;");
        this.estilos.add("-fx-border-color: black;");
        this.estilos.add("-fx-shape: \"M451.713,275.621V99.53h-15.312v176.091H53.593V99.53H38.281v176.091H0v91.874h489.994v-91.874H451.713z\n" +
                "\t\t\t\t M474.681,313.904H222.028v15.312h252.653v22.969H15.312v-0.001v-61.249h459.369V313.904z\";");
    }
}
