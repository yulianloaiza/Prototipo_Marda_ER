package application.controllers;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.*;

import application.Constants;
import application.controllers.DragContainer;
import application.controllers.NodeLink;
import application.controllers.node.DragIcon;
import application.controllers.node.DraggableNode;
import application.models.Enlace.*;
import application.models.Nodo.*;
import application.utils.factories.*;
import application.utils.loaders.LoaderAbs;
import application.utils.loaders.LoaderJSON;
import application.utils.loaders.LoaderXML;
import application.utils.serializers.ConstructorJson;
import application.utils.serializers.ConstructorSerializerAbs;
import application.utils.serializers.ConstructorXml;
import application.utils.savers.SaveCanvasAbs;
import application.utils.savers.SaveCanvasJPG;
import application.utils.savers.SaveCanvasPDF;
import application.utils.savers.SaveCanvasPNG;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Clase que representa el frame del marda, forma parte de la capa de controladores
 */
public class RootLayout extends AnchorPane{

	@FXML SplitPane base_pane;
	@FXML AnchorPane right_pane;
	@FXML VBox left_pane;
	@FXML MenuItem menu_item_export;
	@FXML MenuItem menu_item_close;
	@FXML MenuItem menu_item_load;
	@FXML MenuItem menu_item_save;
	@FXML MenuItem menu_item_about;
	@FXML MenuItem menu_item_new;
	@FXML ChoiceBox menu_arrow;
	@FXML ChoiceBox menu_diagram;

	private DragIcon mDragOverIcon = null;

	private EventHandler<DragEvent> mIconDragOverRoot = null;
	private EventHandler<DragEvent> mIconDragDropped = null;
	private EventHandler<DragEvent> mIconDragOverRightPane = null;

	private FactoryAbs factoryDiagram = null;
	private FactoryFactory factoryFactory = new FactoryFactory();
	ArrayList<String> tiposEnlace;
	ArrayList<String> diagramas = new ArrayList<String>(){{
		add(Constants.DIAGRAM_ER);
		add(Constants.DIAGRAM_REDES);
	}};

	/**
	 * Constructor carga un xml que representa el frame de la apliación (menu, canvas, panel izquierdo)
	 */
	public RootLayout() {
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/RootLayout.fxml"));
		
		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);
		
		try { 
			fxmlLoader.load();
        
		} catch (IOException exception) {
		    throw new RuntimeException(exception);
		}
	}

	/**
	 * Inicializar elementos de JavaFX
	 */
	@FXML
	private void initialize() {

		mDragOverIcon = new DragIcon();
		
		mDragOverIcon.setVisible(false);
		mDragOverIcon.setOpacity(0.65);
		getChildren().add(mDragOverIcon);

		selectTypeOfDiagram(Constants.DIAGRAM_ER);

		buildDragHandlers();

		setupMenuItems();


		menu_diagram.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				if(displayNewDialog()){
					selectTypeOfDiagram(menu_diagram.getValue().toString());
				}

				menu_diagram.setValue(factoryDiagram.getTypeFactory());
			}
		});
	}

	/**
	 * Cambiar el tipo de diagrama que se utiliza dado el tipo pasado por String.
	 * @param type Tipo de diagrama a cambiar, por defecto se crea un diagrama de tipo ER.
	 */
	private void selectTypeOfDiagram(String type){
		if(!type.isEmpty()){

			factoryDiagram = factoryFactory.buildFactory(type);

			left_pane.getChildren().clear();
			menu_arrow.getItems().clear();

			//Add nodes to the left panel
			ArrayList<NodoAbs> nodosTipos = factoryDiagram.getTypesNodes();
			for(NodoAbs nodo : nodosTipos){
				DragIcon icn = new DragIcon();
				addDragDetection(icn);
				icn.setType(nodo);
				left_pane.getChildren().add(icn);
			}

			//Add type of arrows
			updateArrowMenu();
		}
	}

	/**
	 * Agregar funcionalidad de drag and drop para un DragIcon (usados en el left_pane).
	 * @param dragIcon Elemento al que se le agregará la funcionalidad.
	 */
	private void addDragDetection(DragIcon dragIcon) {
		
		dragIcon.setOnDragDetected (new EventHandler <MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {

				// set drag event handlers on their respective objects
				base_pane.setOnDragOver(mIconDragOverRoot);
				right_pane.setOnDragOver(mIconDragOverRightPane);
				right_pane.setOnDragDropped(mIconDragDropped);
				
				// get a reference to the clicked DragIcon object
				DragIcon icn = (DragIcon) event.getSource();
				
				//begin drag ops
				mDragOverIcon.setType(icn.getType());
				mDragOverIcon.relocateToPoint(new Point2D (event.getSceneX(), event.getSceneY()));
            
				ClipboardContent content = new ClipboardContent();
				DragContainer container = new DragContainer();
				
				container.addData ("type", mDragOverIcon.getType());
				content.put(DragContainer.AddNode, container);

				mDragOverIcon.startDragAndDrop (TransferMode.ANY).setContent(content);
				mDragOverIcon.setVisible(true);
				mDragOverIcon.setMouseTransparent(true);
				event.consume();					
			}
		});
	}

	/**
	 *  Dibujar y enlazar los nodos y los enlaces según sea el caso.
	 */
	private void buildDragHandlers() {
		
		//drag over transition to move widget form left pane to right pane
		mIconDragOverRoot = new EventHandler <DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				Point2D p = right_pane.sceneToLocal(event.getSceneX(), event.getSceneY());
				if (!right_pane.boundsInLocalProperty().get().contains(p)) {
					
					event.acceptTransferModes(TransferMode.ANY);
					mDragOverIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
					return;
				}

				event.consume();
			}
		};
		
		mIconDragOverRightPane = new EventHandler <DragEvent> () {

			@Override
			public void handle(DragEvent event) {
				event.acceptTransferModes(TransferMode.ANY);

				mDragOverIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
				event.consume();
			}
		};
				
		mIconDragDropped = new EventHandler <DragEvent> () {

			@Override
			public void handle(DragEvent event) {
				DragContainer container = 
						(DragContainer) event.getDragboard().getContent(DragContainer.AddNode);
				
				container.addData("scene_coords", new Point2D(event.getSceneX(), event.getSceneY()));
				
				ClipboardContent content = new ClipboardContent();
				content.put(DragContainer.AddNode, container);
				
				event.getDragboard().setContent(content);
				event.setDropCompleted(true);
			}
		};
		
		this.setOnDragDone (new EventHandler <DragEvent> (){
			@Override
			public void handle (DragEvent event) {
				right_pane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRightPane);
				right_pane.removeEventHandler(DragEvent.DRAG_DROPPED, mIconDragDropped);
				base_pane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRoot);
								
				mDragOverIcon.setVisible(false);
				
				//Create node drag operation
				DragContainer container = 
						(DragContainer) event.getDragboard().getContent(DragContainer.AddNode);
				
				if (container != null) {
					if (container.getValue("scene_coords") != null) {

						DraggableNode node = new DraggableNode();

						node.addNodoAbs(container.getValue("type"));
						right_pane.getChildren().add(node);
	
						Point2D cursorPoint = container.getValue("scene_coords");
	
						node.relocateToPoint(new Point2D(cursorPoint.getX() - 32, cursorPoint.getY() - 32));

					}
				}

				//AddLink drag operation
				container =	(DragContainer) event.getDragboard().getContent(DragContainer.AddLink);
				
				if (container != null) {
					
					//bind the ends of our link to the nodes whose id's are stored in the drag container
					String sourceId = container.getValue("source");
					String targetId = container.getValue("target");
					String idLink = container.getValue("idLink");

					if (sourceId != null && targetId != null) {
						NodeLink link = null;
						if(idLink == null) {
							String tipoEnlace = (String) menu_arrow.getValue();
							EnlaceAbs enlaceAbs = factoryDiagram.buildEnlace(tipoEnlace); //Nos devuelve el tipo apropiado usando factory
							link = new NodeLink(enlaceAbs);
							//add our link at the top of the rendering order so it's rendered first
							right_pane.getChildren().add(0, link);
						}
						DraggableNode source = null;
						DraggableNode target = null;
						
						for (Node n: right_pane.getChildren()) {
							
							if (n.getId() == null)
								continue;
							
							if (n.getId().equals(sourceId))
								source = (DraggableNode) n;
						
							if (n.getId().equals(targetId))
								target = (DraggableNode) n;

							if(n.getId().equals(idLink)){
								link = (NodeLink) n;
							}
							
						}
						
						if (source != null && target != null){
							//Si no es una conexión válida
							if(!link.bindEnds(source, target)){
								if(idLink == null) { //Conexión nueva no válida
									right_pane.getChildren().remove(0);
								}else { //Link viejo con conexión no válida
									link.redrawLink();
								}
							}
						}
					}
				}
				event.consume();
			}
		});
	}

	/**
	 * Agregar funcionalidad a los elementos del menu_bar.
	 */
	private void setupMenuItems(){
		String msg = "Diagram is empty";
		menu_item_new.setOnAction(e -> displayNewDialog());
		menu_item_export.setOnAction(e -> {
			if(right_pane.getChildren().size() > 0) {
				displayExportDialog();
			}else{
				displayInfoDialog(msg);
			}
		});
		menu_item_close.setOnAction(e -> displayCloseDialog());
		menu_item_about.setOnAction(e -> displayAboutDialog());
		menu_item_save.setOnAction(e -> {
			if(right_pane.getChildren().size() > 0) {
				displaySaveDialog();
			}else{
				displayInfoDialog(msg);
			}
		});
		menu_item_load.setOnAction(e -> displayLoadDialog());
		for(String s : diagramas){
			menu_diagram.getItems().add(s);
		}
		menu_diagram.setValue(diagramas.get(0));
	}

	/**
	 * Actualizar los tipos de enlace que se pueden hacer, dependerá según el tipo de diagrama
	 */
	private void updateArrowMenu(){
		tiposEnlace = factoryDiagram.getTypesEdges();
		for(String s : tiposEnlace) {
			menu_arrow.getItems().add(s);
		}
		menu_arrow.setValue(tiposEnlace.get(0));//Default Value
	}

	/**
	 * Mostrar menú de confirmación para crear un nuevo diagrama en blanco.
	 * @return True = el usuario quiere crear un nuevo diagrama, false en caso contrario
	 */
	private boolean displayNewDialog(){
		Alert alert = new Alert(Alert.AlertType.NONE, "Are you sure you want to create/load a new diagram?", ButtonType.YES, ButtonType.NO);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.YES) {
			right_pane.getChildren().clear();
			return true;
		}
		return false;
	}

	/**
	 * Mostrar menú de exportación del diagrama.
	 */
	private void displayExportDialog(){
		FileChooser fileChooser = new FileChooser();

		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg"),
				new FileChooser.ExtensionFilter("png files (*.png)", "*.png"),
				new FileChooser.ExtensionFilter("pdf files (*.pdf)", "*.pdf"));

		File file = fileChooser.showSaveDialog(null);

		WritableImage writableImage = new WritableImage((int) this.right_pane.getWidth() + 20,
				(int) this.right_pane.getHeight() + 20);
		BufferedImage awtImage = new BufferedImage((int)this.right_pane.getWidth() + 20, (int)this.right_pane.getHeight() + 20, BufferedImage.TYPE_INT_RGB);
		this.right_pane.snapshot(null, writableImage);
		RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, awtImage);

		if(file != null){
			String fileName = file.getName();
			String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, file.getName().length());
			if(!fileExtension.isEmpty()) {
				SaveCanvasAbs saver = null;
				if (fileExtension.equals("png")) { //png
					saver = new SaveCanvasPNG();
				}else if(fileExtension.equals("jpg")){ //jpg
					saver = new SaveCanvasJPG();
				} else if (fileExtension.equals("pdf")) { //pdf
					saver = new SaveCanvasPDF();
				}
				if(saver != null){
					saver.saveCanvas(file, renderedImage);
					displayInfoDialog("Se exportó correctamente el diagrama en formato " + fileExtension + ".");
				}
			}
		}
	}

	/**
	 * Mostrar mensaje de confirmación cuando se quiera cerrar la ventana.
	 */
	private void displayCloseDialog(){
		Alert alert = new Alert(Alert.AlertType.NONE, "Are you sure you want to close the window?", ButtonType.YES, ButtonType.NO);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.YES) {
			Stage stage = (Stage) getScene().getWindow();
			stage.close();
		}
	}

	/**
	 * Mostrar menú de información sobre la aplicación.
	 */
	private void displayAboutDialog(){
		Alert alert = new Alert(Alert.AlertType.NONE, Constants.NAME_APP, ButtonType.CLOSE);
		alert.show();
		if(alert.getResult() == ButtonType.CLOSE){
			alert.close();
		}
	}

	/**
	 * Mostrar información sobre el formato en el que se guardará el diagrama.
	 */
	private void displaySaveDialog(){
		String opciones[] = {"xml", "json"};
		ConstructorSerializerAbs constructor = null;
		FileChooser fileChooser = new FileChooser();

		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("json files (*.json)", "*.json"),
				new FileChooser.ExtensionFilter("xml files (*.xml)", "*.xml"));

		File file = fileChooser.showSaveDialog(base_pane.getScene().getWindow());
		if(file != null) {
			String fileName = file.getName();
			String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, file.getName().length());

			if(!fileExtension.isEmpty()){
				if (fileExtension.equals(opciones[0])) { //xml
					constructor = new ConstructorXml();
				} else if (fileExtension.equals(opciones[1])) { //json
					constructor = new ConstructorJson();
				}
				if(constructor != null){
					serializar(constructor, file.getAbsolutePath());
					displayInfoDialog("Se guardó el diagrama en formato " + fileExtension + ".");
				}
			}
		}
	}

	/**
	 * Método director.
	 * Recorre todos los elementos en el canvas (right_pane) y crea un archivo que representará el diagrama.
	 * @param constructor Tipo de constructor que se utilizará (xml, json, ...)
	 * @param filePath Dirección (directorio) donde se guardará el archivo.
	 */
	private void serializar(ConstructorSerializerAbs constructor, String filePath){
		constructor.iniciar(filePath, factoryDiagram.getTypeFactory());// Ruta y tipo de diagrama a serializar
		for (Node generic : right_pane.getChildren()) {
			if (generic instanceof DraggableNode) {
				DraggableNode nodo = (DraggableNode) generic;
				NodoAbs nodoAbs = nodo.getNodo();
				constructor.serialize(nodoAbs);
			}
		}
		constructor.finalizar();
	}

	/**
	 * Mostrar un menú donde se indica un diagrama que cargar en el canvas, puede ser xml o json.
	 * Se eliminan todos lo elementos del canvas, para dibujar el diagrama.
	 */
	private void displayLoadDialog(){
		String opciones[] = {"xml", "json"};
		FileChooser fileChooser = new FileChooser();

		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("json files (*.json)", "*.json"),
				new FileChooser.ExtensionFilter("xml files (*.xml)", "*.xml"));

		File file = fileChooser.showOpenDialog(base_pane.getScene().getWindow());
		LoaderAbs loader = null;
		if(file != null) {
			String fileName = file.getName();
			String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, file.getName().length());
			if(!fileExtension.isEmpty()){
				if (fileExtension.equals(opciones[0])) { //xml
					loader = new LoaderXML();
				} else if (fileExtension.equals(opciones[1])) { //json
					loader = new LoaderJSON();
				}

				if(loader !=null) {
					factoryDiagram = loader.cargar(file.getAbsolutePath());
					menu_diagram.setValue(factoryDiagram.getTypeFactory());
					buildDiagram(loader.getNodosAbs(), loader.getEnlacesNuevos());
					displayInfoDialog("Diagrama " + fileExtension + " cargado correctamente.");
				}
			}
		}
	}

	/**
	 * Mostrar un menú con información.
	 * @param msg Información que se le mostrará al usuario.
	 */
	private void displayInfoDialog(String msg){
		Alert alert = new Alert(Alert.AlertType.NONE, msg, ButtonType.CLOSE);
		alert.show();
		if(alert.getResult() == ButtonType.CLOSE){
			alert.close();
		}
	}

	/**
	 * Construir en el canvas un diagrama dados una serie de NodosAbs y Enlaces entre esos nodos.
	 * Se crean primero los NodosAbs y despues se realizan los enlaces respectivos.
	 * @param nodos NodosAbs que se dibujarán.
	 * @param enlaces Enlaces entre los nodos.
	 */
	private void buildDiagram(Vector<NodoAbs> nodos,Vector<Vector<String>> enlaces){
		//Eliminar cualquier dibujo
		right_pane.getChildren().clear();

		//Guardar referencia de cada nodo a la hora de contruir los enlaces
		Map<String, DraggableNode> mapDragNodes = new HashMap<String, DraggableNode>();

		//Crear nodos y realocarlos
		for(NodoAbs nodo : nodos){
			DraggableNode draggableNode = new DraggableNode();
			draggableNode.addNodoAbs(nodo);
			draggableNode.setLayoutX(nodo.getCoordX());
			draggableNode.setLayoutY(nodo.getCoordY());
			right_pane.getChildren().add(draggableNode);

			//Lo agregamos al mapa para futuro uso
			mapDragNodes.put(nodo.getId(), draggableNode);
		}
		//Dibujar unicamente nodos para conseguir el width y height
		right_pane.applyCss();
		right_pane.layout();

		//Crear enlaces y ligarlos con los nodos apropiados
		for(int i = 0; i < enlaces.size(); i++){
			// [0] = ID Link, [1] = Type Link, [2] = Source, [3] = Target, [4] =PuertoFuente, [5] = PuertoDestino
			Vector<String> datos = enlaces.elementAt(i);
			String tipo = datos.elementAt(1);
			EnlaceAbs enlace = factoryDiagram.buildEnlace(tipo); //Nos devuelve el tipo apropiado usando factory
			//Set ID
			enlace.setId(datos.get(0));
			//PuertosFuente/Destino
			enlace.setPuertoFuente(Integer.parseInt(datos.get(4)));
			enlace.setPuertoDestino(Integer.parseInt(datos.get(5)));
			//Crear NodeLink
			NodeLink nodeLink = new NodeLink(enlace);
			right_pane.getChildren().add(nodeLink);
			nodeLink.bindEnds(mapDragNodes.get(datos.get(2)), mapDragNodes.get(datos.get(3)));
			nodeLink.redrawLink();
		}
	}
}
