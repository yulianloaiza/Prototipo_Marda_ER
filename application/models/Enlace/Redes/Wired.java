package application.models.Enlace.Redes;

import application.Constants;
import application.models.Enlace.EnlaceAbs;

/**
 * Clase Wired, representa un enlace concreto
 */
public class Wired extends EnlaceAbs {
    public Wired() {
        this.tipo= Constants.LINK_WIRED;
        this.llenarEstilos();
        this.puertoFuente = -1;
        this.puertoDestino = -1;
    }
    public void llenarEstilos(){
        this.estiloFinal= Constants.ARROW_ANCHOR;
        this.estiloInicio= Constants.ARROW_ANCHOR;
        this.isDashed=false;
    }
}
