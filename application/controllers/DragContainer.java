package application.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.DataFormat;
import javafx.util.Pair;

/**
 * Clase encargada de almacenar eventos y elementos relacionados al drag and drop
 */
public class DragContainer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1890998765646621338L;

	public static final DataFormat AddNode = 
			new DataFormat("application.views.window.DragIcon.add");
	
	public static final DataFormat AddLink =
			new DataFormat("application.views.link.NodeLink.add");


	private final List <Pair<String, Object> > mDataPairs = new ArrayList <Pair<String, Object> > ();

	/**
	 * Enlazar un tipo de evento a un Objecto (NodoAbs o EnlaceAbs)
	 * @param key Tipo de evento
	 * @param value Elemento relacionado a ese evento
	 */
	public void addData (String key, Object value) {
		mDataPairs.add(new Pair<String, Object>(key, value));		
	}

	/**
	 * Conseguir un elemento dado un tipo de evento
	 * @param key Tipo de evento
	 * @param <T> Tipo de elemento ligado a ese evento
	 * @return Elemento ligado a ese evento, null si no existe
	 */
	public <T> T getValue (String key) {
		
		for (Pair<String, Object> data: mDataPairs) {
			
			if (data.getKey().equals(key)) return (T) data.getValue();
				
		}
		
		return null;
	}

	/**
	 * Conseguir todos los elementos y eventos del container
	 * @return Elementos y eventos en el contenedor.
	 */
	public List <Pair<String, Object> > getData () { return mDataPairs; }	
}
