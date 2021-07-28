package application.controllers.node;

import java.io.IOException;
import java.util.*;

import application.controllers.DragContainer;
import application.controllers.NodeLink;
import application.models.Enlace.EnlaceAbs;
import application.models.Nodo.NodoAbs;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Controlador para los nodos que se dibujan en el canvas.
 */
public class DraggableNode extends BorderPane {
		
		@FXML private BorderPane root_pane;
		@FXML private AnchorPane port_one;
		@FXML private AnchorPane port_two;
		@FXML private AnchorPane port_three;
		@FXML private AnchorPane port_four;
		@FXML private AnchorPane port_five;
		@FXML private AnchorPane port_six;
		@FXML private AnchorPane port_seven;
		@FXML private AnchorPane port_eight;
		@FXML private AnchorPane port_nine;
		@FXML private AnchorPane port_ten;
		@FXML private AnchorPane port_eleven;
		@FXML private AnchorPane port_twelve;
		@FXML private VBox list_labels;
		@FXML private VBox contenido_2;
		@FXML private Label title_bar;

		private EventHandler <MouseEvent> mLinkHandleDragDetected;
		private EventHandler <DragEvent> mLinkHandleDragDropped;
		private EventHandler <DragEvent> mContextLinkDragOver;
		private EventHandler <DragEvent> mContextLinkDragDropped;
		
		private EventHandler <DragEvent> mContextDragOver;
		private EventHandler <DragEvent> mContextDragDropped;

		private Point2D mDragOffset = new Point2D (0.0, 0.0);
		
		private final DraggableNode self;
		
		private NodeLink mDragLink = null;
		private AnchorPane right_pane = null;

		private ContextMenu contextMenu = null;

		private final ArrayList<String> mLinkIds = new ArrayList <String> ();

		private NodoAbs nodo = null;

	/**
	 * Constructor
	 */
	public DraggableNode() {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../views/GenericNode.fxml"));
			
			fxmlLoader.setRoot(this); 
			fxmlLoader.setController(this);
			
			self = this;
			
			try { 
				fxmlLoader.load();
	        
			} catch (IOException exception) {
			    throw new RuntimeException(exception);
			}

		}
		
		@FXML
		private void initialize() {
			title_bar.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
			buildNodeDragHandlers();
			buildLinkDragHandlers();

			//Para dibujar la linea de inicio
			port_one.setOnDragDetected(mLinkHandleDragDetected);
			port_two.setOnDragDetected(mLinkHandleDragDetected);
			port_three.setOnDragDetected(mLinkHandleDragDetected);
			port_four.setOnDragDetected(mLinkHandleDragDetected);
			port_five.setOnDragDetected(mLinkHandleDragDetected);
			port_six.setOnDragDetected(mLinkHandleDragDetected);
			port_seven.setOnDragDetected(mLinkHandleDragDetected);
			port_eight.setOnDragDetected(mLinkHandleDragDetected);
			port_nine.setOnDragDetected(mLinkHandleDragDetected);
			port_ten.setOnDragDetected(mLinkHandleDragDetected);
			port_eleven.setOnDragDetected(mLinkHandleDragDetected);
			port_twelve.setOnDragDetected(mLinkHandleDragDetected);

			//Detectar cuando se a soltado algo en algunos de los panes
			port_one.setOnDragDropped(mLinkHandleDragDropped);
			port_two.setOnDragDropped(mLinkHandleDragDropped);
			port_three.setOnDragDropped(mLinkHandleDragDropped);
			port_four.setOnDragDropped(mLinkHandleDragDropped);
			port_five.setOnDragDropped(mLinkHandleDragDropped);
			port_six.setOnDragDropped(mLinkHandleDragDropped);
			port_seven.setOnDragDropped(mLinkHandleDragDropped);
			port_eight.setOnDragDropped(mLinkHandleDragDropped);
			port_nine.setOnDragDropped(mLinkHandleDragDropped);
			port_ten.setOnDragDropped(mLinkHandleDragDropped);
			port_eleven.setOnDragDropped(mLinkHandleDragDropped);
			port_twelve.setOnDragDropped(mLinkHandleDragDropped);
			root_pane.setOnDragDropped(mLinkHandleDragDropped);

			mDragLink = new NodeLink(null); //Enlace génerico
			mDragLink.setVisible(false);
			
			parentProperty().addListener(new ChangeListener() {
				@Override
				public void changed(ObservableValue observable,	Object oldValue, Object newValue) {
					right_pane = (AnchorPane) getParent();
				}
			});

			updateEditor();
		}

	/**
	 * Update the editor of the node depending in the type
	 */
	public void updateEditor(){
			//Detectar modificaciones en la lista de contenido
			list_labels.getChildren().addListener(new ListChangeListener<Node>() {
				@Override
				public void onChanged(Change<? extends Node> change) {
					while(change.next()) {
						if(change.wasRemoved()){
							//System.out.println(change.getFrom());
							//System.out.println(change.toString());
							nodo.eliminarContenido(change.getFrom()-1);
						}else if(change.wasUpdated()){
							System.out.println(change.toString());
						}
					}
				}
			});
			contextMenu = new ContextMenu();

			MenuItem menuItem = new MenuItem("Agregar campo");
			menuItem.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					Label newLabel = new Label();
					TextInputDialog dialog = new TextInputDialog();
					dialog.setTitle("Insertar nuevo campo");
					dialog.setHeaderText("Formato: <nombre_campo>:<tipo_dato>. Ejemplo id:int");
					dialog.setContentText("Ingrese un campo válido:");

					Optional<String> result = dialog.showAndWait();
					if(result.isPresent()){
						newLabel.setText(result.get());
					}
					addLabel(newLabel);
					nodo.agregarContenido(newLabel.getText());
					actionEvent.consume();
				}
			});

			MenuItem menuItem1 = new MenuItem("Modificar Titulo");
			menuItem1.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					TextInputDialog dialog = new TextInputDialog(title_bar.getText());
					dialog.setTitle("Nuevo titulo");
					dialog.setHeaderText("Ingrese el nuevo titulo de este Nodo");
					dialog.setContentText("Nuevo titulo:");

					Optional<String> result = dialog.showAndWait();
					if(result.isPresent()){
						title_bar.setText(result.get());
						nodo.setTitulo(result.get());
					}
				}
			});

			MenuItem menuItem2 = new MenuItem("Eliminar");
			menuItem2.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					deleteNode();
				}
			});

			if(this.nodo != null && this.nodo.getTieneTexto()){
				contextMenu.getItems().addAll(menuItem, menuItem1);
			}
			contextMenu.getItems().add(menuItem2);

			root_pane.setOnContextMenuRequested( e -> contextMenu.show(this, e.getScreenX(), e.getScreenY()));
		}

	/**
	 * Agregar un nuevo enlace al nodo
	 * @param linkId Identificador único del enlace a agregar
	 * @param enlaceAbs Enlace a agregar
	 * @return La posición en la lista del enlace recien agregado.
	 */
		public int registerLink(String linkId, EnlaceAbs enlaceAbs) {
			int pos = 0;
			if(!mLinkIds.contains(linkId)) {
				mLinkIds.add(linkId);
				nodo.insertarEnlace(linkId, enlaceAbs);
				pos = nodo.getEnlaces().size() - 1;
			}
			return pos;
		}

	/**
	 * Cambiar las coordenadas del nodo
	 * @param p Coordenadas nuevas
	 */
		public void relocateToPoint (Point2D p) {
			Point2D localCoords = getParent().sceneToLocal(p);
			relocate ( 
					(int) (localCoords.getX() - mDragOffset.getX()),
					(int) (localCoords.getY() - mDragOffset.getY())
				);
			nodo.setCoordX(localCoords.getX());
			nodo.setCoordY(localCoords.getY());
		}

	/**
	 * Eliminar cualquier tipo de enlace que tenga este nodo y eliminarlo.
	 */
	private void deleteNode(){
			AnchorPane parent  = (AnchorPane) self.getParent();
			parent.getChildren().remove(self);
			//iterate each link id connected to this node
			//find it's corresponding component in the right-hand
			//AnchorPane and delete it.
			for (ListIterator <String> iterId = mLinkIds.listIterator(); iterId.hasNext();) {

				String id = iterId.next();
				for (ListIterator <Node> iterNode = parent.getChildren().listIterator(); iterNode.hasNext();) {

					Node node = iterNode.next();

					if (node.getId() == null)
						continue;

					if (node.getId().equals(id)){
						//System.out.println("Eliminando: "+node.getId());
						iterNode.remove();
						NodeLink enlace = (NodeLink) node;
						enlace.notifyNodes(id, getId());
					}
				}
				iterId.remove();
			}
		}

	/**
	 * Eliminar un enlace de este nodo dado el identificador único de ese enlace.
	 * @param linkId Identificador único del enlace a eliminar
	 * @param nodeId Identificador único del nodo que llamó a eliminar ese enlace.
	 */
		public void deleteLinkReference(String linkId, String nodeId){
			//si no es el nodo que se esta eliminando
			if(!nodeId.equals(getId())){
				mLinkIds.remove(linkId);
				nodo.eliminarEnlace(linkId);
			}
		}

	/**
	 * Eliminar enlace según su id
	 * @param id Identificar único del enlace.
	 */
		public void deleteLink(String id){
			AnchorPane parent  = (AnchorPane) self.getParent();
			for (ListIterator <Node> iterNode = parent.getChildren().listIterator(); iterNode.hasNext();) {
				Node node = iterNode.next();
				if (node.getId() == null)
					continue;

				if (node.getId().equals(id)){
					iterNode.remove();
				}
			}
		}

	/**
	 * Modificar el nodo que este controlador maneja, asi se puede dibujar dependiendo
	 * de la clase concreta que se pase.
	 * @param type Clase concreta de NodoAbs.
	 */
		public void addNodoAbs(NodoAbs type) {
			this.nodo = type;

			String style = "";
			for(String estilo : type.getEstilos()){
				style += estilo;
			}
			setStyle(style);

			if(this.nodo.getTitulo().isEmpty()){
				nodo.setTitulo(nodo.getTipo());
			}

			if(nodo.getTieneTitulo()) {
				title_bar.setText(nodo.getTitulo());
			}else{
				title_bar.setVisible(false);
				list_labels.setVisible(false);
			}

			//Verificar/modificar ID
			if(this.nodo.getId().isEmpty()) {
				setId(UUID.randomUUID().toString());
				this.nodo.setId(getId());
			}else{
				setId(type.getId());
			}

			if(this.nodo.getTieneTexto()){
				//Agregar Campos
				Vector<String> campos = new Vector<>(this.nodo.getContenido());
				for(int i = 0; i < campos.size(); i++){
					Label newLabel = new Label();
					newLabel.setText(campos.elementAt(i));
					addLabel(newLabel);
				}
			}

			updateEditor();
		}

	/**
	 * Agreguar un campo al nodo
	 * @param newLabel Nuevo campo a agregar
	 */
	private void addLabel(Label newLabel){
			newLabel.setContextMenu(new LabelContextMenu(newLabel, list_labels));
			newLabel.setId(UUID.randomUUID().toString());
			newLabel.setWrapText(true);

			list_labels.getChildren().add(newLabel);

			//Notificar de cambio de contenido
			newLabel.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observableValue, String oldText, String newText) {
					nodo.modificarContenido(list_labels.getChildren().indexOf(newLabel) - 1, newText);
				}
			});
			//Actualizar tamaño del nodo
			setMinSize(newLabel.getPrefHeight(), list_labels.getMinHeight());
		}

	/**
	 * Conseguir instancia concreta de este nodo
	 * @return Clase concreta del nodo
	 */
	public NodoAbs getNodo(){
		return this.nodo;
	}

	/**
	 * Agregar funcionalidad drag and drop en el canvas.
	 */
	public void buildNodeDragHandlers() {
			//Mover del panel izquierdo al derecho
			mContextDragOver = new EventHandler <DragEvent>() {
				//dragover to handle node dragging in the right pane view
				@Override
				public void handle(DragEvent event) {		
			
					event.acceptTransferModes(TransferMode.ANY);				
					relocateToPoint(new Point2D( event.getSceneX(), event.getSceneY()));

					event.consume();
				}
			};
			
			//dragdrop for node dragging
			mContextDragDropped = new EventHandler <DragEvent> () {
		
				@Override
				public void handle(DragEvent event) {
				
					getParent().setOnDragOver(null);
					getParent().setOnDragDropped(null);
					
					event.setDropCompleted(true);
					
					event.consume();
				}
			};

			//drag detection for node dragging
			root_pane.setOnDragDetected ( new EventHandler <MouseEvent> () {
				@Override
				public void handle(MouseEvent event) {

					getParent().setOnDragOver(null);
					getParent().setOnDragDropped(null);

					getParent().setOnDragOver (mContextDragOver);
					getParent().setOnDragDropped (mContextDragDropped);

	                //begin drag ops
	                mDragOffset = new Point2D(event.getX(), event.getY());
	                
	                relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
	                
	                ClipboardContent content = new ClipboardContent();
					DragContainer container = new DragContainer();
					
					container.addData ("type", nodo);
					content.put(DragContainer.AddNode, container);

					System.out.println("Current childs: " + mLinkIds.toString());
					
	                startDragAndDrop (TransferMode.ANY).setContent(content);
	                
	                event.consume();
				}
				
			});		
		}
	/**
	 * Agregar funcionalidad de crear enlaces en el canvas.
	 */
		private void buildLinkDragHandlers() {
			
			mLinkHandleDragDetected = new EventHandler <MouseEvent> () {
				@Override
				public void handle(MouseEvent event) {
					
					getParent().setOnDragOver(null);
					getParent().setOnDragDropped(null);
					
					getParent().setOnDragOver(mContextLinkDragOver);
					getParent().setOnDragDropped(mContextLinkDragDropped);
					
					//Set up user-draggable link
					//index 0 para evitar que se dibuje sobre otros nodos existentes
					right_pane.getChildren().add(0,mDragLink);					
					
					mDragLink.setVisible(false);

					Point2D p = new Point2D(getLayoutX() + (getWidth() / 2.0),getLayoutY() + (getHeight() / 2.0));

					mDragLink.setStart(p);					
					
					//Drag content code
	                ClipboardContent content = new ClipboardContent();
	                DragContainer container = new DragContainer ();
	                
	                //pass the UUID of the source node for later lookup
	                container.addData("source", getId());

	                content.put(DragContainer.AddLink, container);
					
					startDragAndDrop (TransferMode.ANY).setContent(content);	

					event.consume();
				}
			};

			mLinkHandleDragDropped = new EventHandler <DragEvent> () {

				@Override
				public void handle(DragEvent event) {

					getParent().setOnDragOver(null);
					getParent().setOnDragDropped(null);

					DragContainer container =
							(DragContainer) event.getDragboard().getContent(DragContainer.AddLink);
								
					if (container == null)
						return;

					if(container.getValue("idLink") == null){
						mDragLink.setVisible(false);
						right_pane.getChildren().remove(0);
					}

					ClipboardContent content = new ClipboardContent();
					
					//pass the UUID of the target node for later lookup
					container.addData("target", getId());
					
					content.put(DragContainer.AddLink, container);
					
					event.getDragboard().setContent(content);
					event.setDropCompleted(true);
					event.consume();				
				}
			};

			//Seguir el cursor del mouse
			mContextLinkDragOver = new EventHandler <DragEvent> () {
				@Override
				public void handle(DragEvent event) {
					event.acceptTransferModes(TransferMode.ANY);
					//Relocate end of user-draggable link
					if (!mDragLink.isVisible())
						mDragLink.setVisible(true);

					mDragLink.setEnd(new Point2D(event.getX(), event.getY()));

					event.consume();
				}
			};

			//drop event for link creation
			mContextLinkDragDropped = new EventHandler <DragEvent> () {
				@Override
				public void handle(DragEvent event) {
					getParent().setOnDragOver(null);
					getParent().setOnDragDropped(null);
					//hide the draggable NodeLink and remove it from the right-hand AnchorPane's children
					mDragLink.setVisible(false);
					right_pane.getChildren().remove(0); //Eliminar link

					event.setDropCompleted(true);
					event.consume();
				}
				
			};
			
		}
}
