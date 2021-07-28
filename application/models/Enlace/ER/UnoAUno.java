package application.models.Enlace.ER;

import application.Constants;
import application.models.Enlace.EnlaceAbs;

/**
 * Clase UnoAUno, representa un enlace concreto que se utiliza entre entidades
 */
public class UnoAUno extends EnlaceAbs {
    public UnoAUno() {
        this.tipo=  Constants.LINK_ONETOONE;
        this.llenarEstilos();
        this.puertoFuente = -1;
        this.puertoDestino = -1;
    }

    public UnoAUno(String id) {
        this.tipo=  Constants.LINK_ONETOONE;
        this.id= id;
        this.puertoFuente = -1;
        this.puertoDestino = -1;
    }

    public void llenarEstilos(){
        this.estiloFinal = Constants.ARROW_ONE;//Uno
        this.estiloInicio = Constants.ARROW_ONE;
        this.isDashed=false;
        this.rellenoInicio = "-fx-background-color: black;";
        this.rellenoFinal = "-fx-background-color: black;";
    }
}
