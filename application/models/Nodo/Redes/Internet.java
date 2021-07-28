package application.models.Nodo.Redes;

import application.Constants;
import application.models.Enlace.EnlaceAbs;
import application.models.Nodo.NodoAbs;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Clase Internet, representa un nodo concreto
 * NO tiene dos campos de texto, para el titulo, y para el contenido.
 * No necesita segmentacion expecial para la conexion de enlaces.
 */
public class Internet extends NodoAbs {
    public Internet(){
        this.tipo = Constants.NODE_INTERNET;
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
        this.estilos.add("-fx-shape: \"M50.688,48.222C55.232,43.101,58,36.369,58,29c0-7.667-2.996-14.643-7.872-19.834c0,0,0-0.001,0-0.001\n" +
                "\tc-0.004-0.006-0.01-0.008-0.013-0.013c-5.079-5.399-12.195-8.855-20.11-9.126l-0.001-0.001L29.439,0.01C29.293,0.005,29.147,0,29,0\n" +
                "\ts-0.293,0.005-0.439,0.01l-0.563,0.015l-0.001,0.001c-7.915,0.271-15.031,3.727-20.11,9.126c-0.004,0.005-0.01,0.007-0.013,0.013\n" +
                "\tc0,0,0,0.001-0.001,0.002C2.996,14.357,0,21.333,0,29c0,7.369,2.768,14.101,7.312,19.222c0.006,0.009,0.006,0.019,0.013,0.028\n" +
                "\tc0.018,0.025,0.044,0.037,0.063,0.06c5.106,5.708,12.432,9.385,20.608,9.665l0.001,0.001l0.563,0.015C28.707,57.995,28.853,58,29,58\n" +
                "\ts0.293-0.005,0.439-0.01l0.563-0.015l0.001-0.001c8.185-0.281,15.519-3.965,20.625-9.685c0.013-0.017,0.034-0.022,0.046-0.04\n" +
                "\tC50.682,48.241,50.682,48.231,50.688,48.222z M2.025,30h12.003c0.113,4.239,0.941,8.358,2.415,12.217\n" +
                "\tc-2.844,1.029-5.563,2.409-8.111,4.131C4.585,41.891,2.253,36.21,2.025,30z M8.878,11.023c2.488,1.618,5.137,2.914,7.9,3.882\n" +
                "\tC15.086,19.012,14.15,23.44,14.028,28H2.025C2.264,21.493,4.812,15.568,8.878,11.023z M55.975,28H43.972\n" +
                "\tc-0.122-4.56-1.058-8.988-2.75-13.095c2.763-0.968,5.412-2.264,7.9-3.882C53.188,15.568,55.736,21.493,55.975,28z M28,14.963\n" +
                "\tc-2.891-0.082-5.729-0.513-8.471-1.283C21.556,9.522,24.418,5.769,28,2.644V14.963z M28,16.963V28H16.028\n" +
                "\tc0.123-4.348,1.035-8.565,2.666-12.475C21.7,16.396,24.821,16.878,28,16.963z M30,16.963c3.179-0.085,6.3-0.566,9.307-1.438\n" +
                "\tc1.631,3.91,2.543,8.127,2.666,12.475H30V16.963z M30,14.963V2.644c3.582,3.125,6.444,6.878,8.471,11.036\n" +
                "\tC35.729,14.45,32.891,14.881,30,14.963z M40.409,13.072c-1.921-4.025-4.587-7.692-7.888-10.835\n" +
                "\tc5.856,0.766,11.125,3.414,15.183,7.318C45.4,11.017,42.956,12.193,40.409,13.072z M17.591,13.072\n" +
                "\tc-2.547-0.879-4.991-2.055-7.294-3.517c4.057-3.904,9.327-6.552,15.183-7.318C22.178,5.38,19.512,9.047,17.591,13.072z M16.028,30\n" +
                "\tH28v10.038c-3.307,0.088-6.547,0.604-9.661,1.541C16.932,37.924,16.141,34.019,16.028,30z M28,42.038v13.318\n" +
                "\tc-3.834-3.345-6.84-7.409-8.884-11.917C21.983,42.594,24.961,42.124,28,42.038z M30,55.356V42.038\n" +
                "\tc3.039,0.085,6.017,0.556,8.884,1.4C36.84,47.947,33.834,52.011,30,55.356z M30,40.038V30h11.972\n" +
                "\tc-0.113,4.019-0.904,7.924-2.312,11.58C36.547,40.642,33.307,40.126,30,40.038z M43.972,30h12.003\n" +
                "\tc-0.228,6.21-2.559,11.891-6.307,16.348c-2.548-1.722-5.267-3.102-8.111-4.131C43.032,38.358,43.859,34.239,43.972,30z\n" +
                "\t M9.691,47.846c2.366-1.572,4.885-2.836,7.517-3.781c1.945,4.36,4.737,8.333,8.271,11.698C19.328,54.958,13.823,52.078,9.691,47.846\n" +
                "\tz M32.521,55.763c3.534-3.364,6.326-7.337,8.271-11.698c2.632,0.945,5.15,2.209,7.517,3.781\n" +
                "\tC44.177,52.078,38.672,54.958,32.521,55.763z\";");
    }
}
