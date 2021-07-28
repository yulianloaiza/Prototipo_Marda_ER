package application.models.Enlace.Redes;


import application.Constants;
import application.models.Enlace.EnlaceAbs;

/**
 * Clase Wireless, representa un enlace concreto
 */
public class Wireless extends EnlaceAbs {
    public Wireless() {
        this.tipo=  Constants.LINK_WIRELESS;
        this.llenarEstilos();
        this.puertoFuente = -1;
        this.puertoDestino = -1;
    }

    public void llenarEstilos(){
        this.estiloFinal= Constants.ARROW_ANCHOR;
        this.estiloInicio= Constants.ARROW_ANCHOR;
        this.isDashed=true;
    }

}
