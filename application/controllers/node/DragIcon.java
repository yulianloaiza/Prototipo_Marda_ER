package application.controllers.node;

import java.io.IOException;
import java.util.Vector;

import application.models.Nodo.NodoAbs;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;


/**
 * Controlador para un nodo que se construye en el panel izquierdo del frame
 */
public class DragIcon extends AnchorPane{
	
	@FXML AnchorPane root_pane;
	@FXML Label label_entity;

	private NodoAbs mType = null;
	
	public DragIcon() {
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../views/DragIcon.fxml"));
		
		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);
		
		try { 
			fxmlLoader.load();
        
		} catch (IOException exception) {
		    throw new RuntimeException(exception);
		}

	}
	
	@FXML
	private void initialize() {}

	/**
	 * Cambiar las coordenadas del nodo
	 * @param p Coordenadas nuevas
	 */
	public void relocateToPoint (Point2D p) {

		Point2D localCoords = getParent().sceneToLocal(p);
		
		relocate ( 
				(int) (localCoords.getX() - (getBoundsInLocal().getWidth() / 2)),
				(int) (localCoords.getY() - (getBoundsInLocal().getHeight() / 2))
			);
	}

	/**
	 * Conseguir el tipo de nodo concreto que representa
	 * @return Nodo concreto
	 */
	public NodoAbs getType () { return mType; }

	/**
	 * Cambiar el tipo de nodo que este controlador maneja.
	 * @param type Nuevo nodo concreto.
	 */
	public void setType (NodoAbs type) {
		mType = type;
		if(type.getTieneTitulo()) {
			label_entity.setText(type.getTipo());
		}else{
			label_entity.setVisible(false);
		}
		String style = "";
		for(String estilo : type.getEstilos()){
			style += estilo;
		}
		setStyle(style);
	}
}
