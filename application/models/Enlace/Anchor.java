package application.models.Enlace;


import application.Constants;

/**
 * Clase Anchor, representa un enlace concreto
 * Se utiliza cuando se quieren conectar notas con otros nodos.
 */
public class Anchor extends EnlaceAbs {
    public Anchor() {
        this.tipo=  Constants.LINK_ANCHOR;
        this.llenarEstilos();
        this.puertoFuente = -1;
        this.puertoDestino = -1;
    }

    public Anchor(String id) {
        this.tipo= Constants.LINK_ANCHOR;
        this.id= id;
        this.puertoFuente = -1;
        this.puertoDestino = -1;
    }

    public void llenarEstilos(){
        this.estiloFinal= Constants.ARROW_ANCHOR;
        this.estiloInicio= Constants.ARROW_ANCHOR;
        this.isDashed=true;
    }

}
