package application.models.Enlace.ER;

import application.Constants;
import application.models.Enlace.EnlaceAbs;

/**
 * Clase UnoAMuchos, representa un enlace concreto que se utiliza entre entidades
 */
public class UnoAMuchos extends EnlaceAbs {
    public UnoAMuchos() {
        this.tipo=  Constants.LINK_ONETOMANY;
        this.llenarEstilos();
        this.puertoFuente = -1;
        this.puertoDestino = -1;
    }

    public UnoAMuchos(String id) {
        this.tipo=  Constants.LINK_ONETOMANY;
        this.id= id;
        this.llenarEstilos();
        this.puertoFuente = -1;
        this.puertoDestino = -1;
    }

    public void llenarEstilos(){
        this.estiloFinal=Constants.ARROW_MANY;//Uno
        this.estiloInicio=Constants.ARROW_ONE;//Muchos
        this.isDashed=false;
        this.rellenoInicio = "-fx-background-color: black;";
    }
}
