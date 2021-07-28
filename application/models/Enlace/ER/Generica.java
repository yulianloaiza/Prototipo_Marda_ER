package application.models.Enlace.ER;

import application.Constants;
import application.models.Enlace.EnlaceAbs;

/**
 * Clase Generica, representa un enlace concreto
 * Se utiliza para enlazar las entidades con distintos componentes como View, Trigger y StoredProcedure. También entre
 * StoredProcedure y StoredProcedureRS.
 */
public class Generica extends EnlaceAbs {
    public Generica() {
        this.tipo= Constants.LINK_GENERIC;
        this.llenarEstilos();
        this.puertoFuente = -1;
        this.puertoDestino = -1;
    }

    public Generica(String id) {
        this.tipo= Constants.LINK_GENERIC;
        this.id= id;
        this.llenarEstilos();
        this.puertoFuente = -1;
        this.puertoDestino = -1;
    }

    public void llenarEstilos(){
        this.estiloFinal= Constants.ARROW_GENERIC;
        this.estiloInicio= Constants.ARROW_ANCHOR;
        this.isDashed=false;
        this.rellenoFinal = "-fx-background-color: black;";
    }
}
