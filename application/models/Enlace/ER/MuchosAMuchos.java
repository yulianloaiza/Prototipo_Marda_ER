package application.models.Enlace.ER;

import application.Constants;
import application.models.Enlace.EnlaceAbs;

/**
 * Clase MuchosAMuchos, representa un enlace concreto que se utiliza entre entidades
 */
public class MuchosAMuchos extends EnlaceAbs {
    public MuchosAMuchos() {
        this.tipo= Constants.LINK_MANYTOMANY;
        this.llenarEstilos();
        this.puertoFuente = -1;
        this.puertoDestino = -1;
    }

    public MuchosAMuchos(String id) {
        this.tipo= Constants.LINK_MANYTOMANY;
        this.id= id;
        this.puertoFuente = -1;
        this.puertoDestino = -1;
    }

    public void llenarEstilos(){
        this.estiloFinal= Constants.ARROW_MANY;//Muchos
        this.estiloInicio= Constants.ARROW_MANY;
        this.isDashed=false;
    }
}
