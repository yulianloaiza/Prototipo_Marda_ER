package application.controllers;

import java.io.IOException;
import java.util.UUID;

import application.controllers.node.DraggableNode;
import application.models.Enlace.EnlaceAbs;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.SVGPath;

/**
 * Controlador de enlaces (EnlaceAbs)
 */
public class NodeLink extends AnchorPane {

	@FXML
	Line node_link;
	@FXML
	Label tx_label;

	private DraggableNode source = null;
	private DraggableNode target = null;
	private DraggableNode old = null;

	private final SimpleDoubleProperty sourceX = new SimpleDoubleProperty();
	private final SimpleDoubleProperty sourceY = new SimpleDoubleProperty();
	private final SimpleDoubleProperty targetX = new SimpleDoubleProperty();
	private final SimpleDoubleProperty targetY = new SimpleDoubleProperty();
	private EventHandler<MouseEvent> mLinkSourceHandleDragDetected;
	private EventHandler<MouseEvent> mLinkTargetHandleDragDetected;
	private EventHandler<DragEvent> mContextSourceLinkDragOver;
	private EventHandler<DragEvent> mContextTargetLinkDragOver;
	private EventHandler<DragEvent> mContextLinkDragDropped;

	private final double REQUIRED_WIDTH = 20.0;
	private final double REQUIRED_HEIGHT = 20.0;
	private double padding_source = 0.0;
	private double padding_target = 0.0;
	private double[] points;
	private final Region svgHeadA = new Region();
	private final Region svgHeadB = new Region();

	private String rellenoInicio = "";
	private String rellenoFinal = "";

	private EnlaceAbs enlaceAbs;
	Polyline polyline = new Polyline();

	/**
	 * Controlador de un EnlaceAbs
	 * @param link Clase concreta que representa un enlace entre dos nodos.
	 */
	public NodeLink(EnlaceAbs link) {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/NodeLink.fxml"));

		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();

		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

		String style1 = "";
		String style2 = "";
		if (link != null) {
			this.enlaceAbs = link;

			if (enlaceAbs.getId().isEmpty()) {
				enlaceAbs.setId(UUID.randomUUID().toString());
			}
			setId(enlaceAbs.getId());

			if (enlaceAbs.isDashed()) {
				node_link.getStrokeDashArray().addAll(5d);
			}

			style1 = enlaceAbs.getStyleStart();
			style2 = enlaceAbs.getStyleEnd();
			this.rellenoInicio = enlaceAbs.getRellenoInicio();
			this.rellenoFinal = enlaceAbs.getRellenoFinal();
		}

		if (!style1.isEmpty()) {
			drawHeadA(style1);
			svgHeadA.setOnDragDetected(mLinkSourceHandleDragDetected);
		}
		if (!style2.isEmpty()) {
			drawHeadB(style2);
			svgHeadB.setOnDragDetected(mLinkTargetHandleDragDetected);
		}
		points = new double[12];
		for (int i = 0; i < 12; i++)
			points[i] = 0.0;
	}

	@FXML
	private void initialize() {
		tx_label.setVisible(false);
		polyline.setVisible(false);
		getChildren().add(polyline);

		setupMenu();
		setupPropertyListeners();
		buildLinkDragHandlers();
	}

	/**
	 * Agregar un menú al enlace que permita eliminarno.
	 */
	private void setupMenu() {
		ContextMenu contextMenu = new ContextMenu();

		MenuItem menuItem1 = new MenuItem("Eliminar");
		menuItem1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				notifyNodes(getId(), "");
				if(source != null)
					source.deleteLink(getId());
			}
		});
		contextMenu.getItems().addAll(menuItem1);
		setOnContextMenuRequested(e -> contextMenu.show(this, e.getScreenX(), e.getScreenY()));
	}

	/**
	 * Agregar funcionalidad de drag and drop que permite modificar los enlaces.
	 */
	private void buildLinkDragHandlers() {

		mLinkSourceHandleDragDetected = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				svgHeadA.setVisible(false);
				polyline.setVisible(false);
				source.deleteLinkReference(getId(), "");
				old = source;
				getParent().setOnDragOver(null);
				getParent().setOnDragDropped(null);

				getParent().setOnDragOver(mContextSourceLinkDragOver);
				getParent().setOnDragDropped(mContextLinkDragDropped);

				ClipboardContent content = new ClipboardContent();
				DragContainer container = new DragContainer();

				container.addData("source", target.getId());
				container.addData("idLink", getId());

				content.put(DragContainer.AddLink, container);
				startDragAndDrop(TransferMode.ANY).setContent(content);
			}
		};

		mLinkTargetHandleDragDetected = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				svgHeadB.setVisible(false);
				polyline.setVisible(false);
				target.deleteLinkReference(getId(), "");
				old = target;
				getParent().setOnDragOver(null);
				getParent().setOnDragDropped(null);

				getParent().setOnDragOver(mContextTargetLinkDragOver);
				getParent().setOnDragDropped(mContextLinkDragDropped);

				ClipboardContent content = new ClipboardContent();
				DragContainer container = new DragContainer();

				container.addData("source", source.getId());
				container.addData("idLink", getId());

				content.put(DragContainer.AddLink, container);
				startDragAndDrop(TransferMode.ANY).setContent(content);
			}
		};

		// Seguir el cursor del mouse
		mContextTargetLinkDragOver = new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				event.acceptTransferModes(TransferMode.ANY);
				node_link.endXProperty().set(event.getX());
				node_link.endYProperty().set(event.getY());
				event.consume();
			}
		};

		mContextSourceLinkDragOver = new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				event.acceptTransferModes(TransferMode.ANY);
				node_link.startXProperty().set(event.getX());
				node_link.startYProperty().set(event.getY());

				event.consume();
			}
		};

		mContextLinkDragDropped = new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				getParent().setOnDragOver(null);
				getParent().setOnDragDropped(null);
				event.setDropCompleted(true);
				event.consume();
			}

		};
	}

	/**
	 * Dibujar la flecha A del enlace el cual corresponde a el origen.
	 * @param style SVG que representa el dibujo de la flecha
	 */
	private void drawHeadA(String style) {
		if (!style.isEmpty()) {
			SVGPath headAdraw = new SVGPath();
			headAdraw.setStroke(Color.TRANSPARENT);
			headAdraw.setContent(style);
			svgHeadA.setShape(headAdraw);
			svgHeadA.setMinSize(REQUIRED_WIDTH, REQUIRED_HEIGHT);
			svgHeadA.setPrefSize(REQUIRED_WIDTH, REQUIRED_HEIGHT);
			svgHeadA.setMaxSize(REQUIRED_WIDTH, REQUIRED_HEIGHT);
			svgHeadA.setStyle("-fx-border-color: black;" + rellenoInicio);
			getChildren().add(svgHeadA);
		}
	}

	/**
	 * Dibujar la flecha B del enlace el cual corresponde a el destino.
	 * @param style SVG que representa el dibujo de la flecha
	 */
	private void drawHeadB(String style) {
		if (!style.isEmpty()) {
			SVGPath headAdraw = new SVGPath();
			headAdraw.setStroke(Color.TRANSPARENT);
			headAdraw.setContent(style);
			svgHeadB.setShape(headAdraw);
			svgHeadB.setMinSize(REQUIRED_WIDTH, REQUIRED_HEIGHT);
			svgHeadB.setPrefSize(REQUIRED_WIDTH, REQUIRED_HEIGHT);
			svgHeadB.setMaxSize(REQUIRED_WIDTH, REQUIRED_HEIGHT);
			svgHeadB.setStyle("-fx-border-color: black;" + rellenoFinal);
			getChildren().add(svgHeadB);
		}
	}

	/**
	 * Actualizar el origen del enlace.
	 * @param x Nueva coordenada x del origen del enlace
	 * @param y Nueva coordenada y del origen del enlace
	 * @param rotation Rotar el dibujo de la flecha (en radianes)
	 */
	public void updateHeadA(double x, double y, double rotation) {
		svgHeadA.rotateProperty().set(rotation);
		double offsetX = 0.0;
		double offsetY = 0.0;
		if (rotation == 90.0) {// Derecha
			offsetX -= REQUIRED_WIDTH * 2.0;
			offsetY -= (REQUIRED_HEIGHT / 2.0) + padding_source;
			node_link.startYProperty().set(y - padding_source);
			node_link.startXProperty().set(x - REQUIRED_WIDTH);
		} else if (rotation == 180.0) {// Arriba
			offsetX -= (REQUIRED_WIDTH / 2.0) + padding_source;
			offsetY -= REQUIRED_HEIGHT * 2.0;
			node_link.startYProperty().set(y - REQUIRED_HEIGHT);
			node_link.startXProperty().set(x - padding_source);
		} else if (rotation == 270.0) {// Izquierda
			offsetX += REQUIRED_WIDTH;
			offsetY -= (REQUIRED_HEIGHT / 2.0) + padding_source;
			node_link.startXProperty().set(x + REQUIRED_WIDTH);
			node_link.startYProperty().set(y - padding_source);
		} else { // Abajo
			offsetX -= (REQUIRED_HEIGHT / 2.0) + padding_source;
			offsetY = REQUIRED_HEIGHT;
			node_link.startYProperty().set(y + REQUIRED_HEIGHT);
			node_link.startXProperty().set(x - padding_source);
		}
		svgHeadA.layoutXProperty().set(x + offsetX);
		svgHeadA.layoutYProperty().set(y + offsetY);
	}

	/**
	 * Actualizar el destino del enlace.
	 * @param x Nueva coordenada x del destino del enlace
	 * @param y Nueva coordenada y del destino del enlace
	 * @param rotation Rotar el dibujo de la flecha (en radianes)
	 */
	public void updateHeadB(double x, double y, double rotation) {
		svgHeadB.rotateProperty().set(rotation);
		double offsetX = 0.0;
		double offsetY = 0.0;
		if (rotation == 90.0) {// Derecha
			offsetX -= REQUIRED_WIDTH * 2.0;
			offsetY -= (REQUIRED_HEIGHT / 2.0) + padding_target;
			node_link.endXProperty().set(x - REQUIRED_WIDTH);
			node_link.endYProperty().set(y - padding_target);
		} else if (rotation == 180.0) {// Arriba
			offsetX -= (REQUIRED_WIDTH / 2.0) + padding_target;
			offsetY -= REQUIRED_HEIGHT * 2.0;
			node_link.endYProperty().set(y - REQUIRED_HEIGHT);
			node_link.endXProperty().set(x - padding_target);
		} else if (rotation == 270.0) {// Izquierda
			offsetX += REQUIRED_WIDTH;
			offsetY -= (REQUIRED_HEIGHT / 2.0) + padding_target;
			node_link.endXProperty().set(x + REQUIRED_WIDTH);
			node_link.endYProperty().set(y - padding_target);
		} else { // Abajo
			offsetX -= (REQUIRED_HEIGHT / 2.0) + padding_target;
			offsetY = REQUIRED_HEIGHT;
			node_link.endYProperty().set(y + REQUIRED_HEIGHT);
			node_link.endXProperty().set(x - padding_target);
		}
		svgHeadB.layoutXProperty().set(x + offsetX);
		svgHeadB.layoutYProperty().set(y + offsetY);
	}

	/**
	 * Cambiar el origen del enlace
	 * @param x Nueva coordenada x del destino del enlace
	 * @param y Nueva coordenada y del destino del enlace
	 * @param rotation Rotar el dibujo de la flecha (en radianes)
	 */
	private void changeArrowSource(double x, double y, double rotation) {
		if (node_link != null) {
			node_link.startXProperty().set(x);
			node_link.startYProperty().set(y);
			updateHeadA(x, y, rotation);
		}
	}

	/**
	 * Cambiar el destino del enlace
	 * @param x Nueva coordenada x del destino del enlace
	 * @param y Nueva coordenada y del destino del enlace
	 * @param rotation Rotar el dibujo de la flecha (en radianes)
	 */
	private void changeArrowTarget(double x, double y, double rotation) {
		if (node_link != null) {
			node_link.endXProperty().set(x);
			node_link.endYProperty().set(y);
			updateHeadB(x, y, rotation);
		}
	}

	/**
	 * Redibujar el enlace, esta funcion se llama cada vez que se realiza una operacion
	 * drag and drop en el enlace o en el nodo destino u origen.
	 */
	public void redrawLink() {
		if (source != null && target != null) {
			int padding = 17;
			double x1 = source.layoutXProperty().get();
			double y1 = source.layoutYProperty().get();
			double x2 = target.layoutXProperty().get();
			double y2 = target.layoutYProperty().get();
			double deltaX = x1 - x2;
			double deltaY = y1 - y2;
			double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));
			angle = ((angle) < 0 ? (360d + angle) : angle);
			// System.out.println("Angulo: " + angle);

			if (angle >= 0 && angle < 40 || angle >= 320) {// 320 a 360 y 0 a 40 izquierda
				// System.out.println("Izquierda Source - Derecha Target");
				changeArrowSource(x1 + padding, y1 + source.getHeight() / 2.0, 90.0);
				changeArrowTarget(x2 + target.getWidth() - padding, y2 + target.getHeight() / 2.0, 270.0);
			} else if (angle >= 40 && angle < 135) {// 40 a 135 arriba
				// System.out.println("Arriba Source - Abajo Target");
				changeArrowSource(x1 + source.getWidth() / 2.0, y1 + padding, 180.0);
				changeArrowTarget(x2 + target.getWidth() / 2.0, y2 + target.getHeight() - padding, 0.0);
			} else if (angle >= 135 && angle < 220) {// 135 a 220 derecha
				// System.out.println("Derecha Source - Izquierda Target");
				changeArrowSource(x1 + source.getWidth() - padding, y1 + source.getHeight() / 2.0, 270.0);
				changeArrowTarget(x2 + padding, y2 + target.getHeight() / 2.0, 90.0);
			} else if (angle >= 220 && angle < 320) {// 220 a 320 abajo
				// System.out.println("Abajo Source - Arriba Target");
				changeArrowSource(x1 + source.getWidth() / 2.0, y1 + source.getHeight() - padding, 0.0);
				changeArrowTarget(x2 + target.getWidth() / 2.0, y2 + padding, 180.0);
			}
			drawPolyline();
		}
	}

	/**
	 * Ligar a las propiedades del enlace listeners para que cuando se mueva el nodo origen o el nodo destino
	 * se redibuje el enlace.
	 */
	private void setupPropertyListeners() {
		targetX.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
				redrawLink();
			}
		});

		targetY.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
				redrawLink();
			}
		});

		sourceX.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
				redrawLink();
			}
		});

		sourceY.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
				redrawLink();
			}
		});

	}

	/**
	 * Indicar las coordenadas de origen del enlace
	 * @param startPoint Punto de inicio
	 */
	public void setStart(Point2D startPoint) {
		svgHeadA.setVisible(false);
		node_link.setStartX(startPoint.getX());
		node_link.setStartY(startPoint.getY());
	}

	/**
	 * Indicar las coordenadas de destino del enlace
	 * @param endPoint Punto final
	 */
	public void setEnd(Point2D endPoint) {
		svgHeadB.setVisible(false);
		node_link.setEndX(endPoint.getX());
		node_link.setEndY(endPoint.getY());
	}

	/**
	 * Cuando se destruye el enlace se llama esta funcion para que el nodo origen y destino
	 * eliminen la referencia de este enlace
	 * @param linkId Id del enlace a borrar
	 * @param nodeId Id del nodo a notificar
	 */
	public void notifyNodes(String linkId, String nodeId) {
		if (source != null && target != null) {
			this.source.deleteLinkReference(linkId, nodeId);
			this.target.deleteLinkReference(linkId, nodeId);
		}
	}

	/**
	 * Modificar el nodo origen de este enlace
	 * @param source Nuevo nodo origen del enlace
	 */
	public void updateSource(DraggableNode source) {
		this.source = source;
		sourceX.bind(source.layoutXProperty());
		sourceY.bind(source.layoutYProperty());
		this.enlaceAbs.setFuente(source.getNodo());
	}

	/**
	 * Modificar el nodo destino de este enlace
	 * @param target Nuevo nodo destino del enlace
	 */
	public void updateTarget(DraggableNode target) {
		this.target = target;
		targetX.bind(target.layoutXProperty());
		targetY.bind(target.layoutYProperty());
		this.enlaceAbs.setDestino(target.getNodo());
	}

	/**
	 * Ligar un nodo origen y un nodo destino, por medio de este enlace
	 * @param source Nodo origen
	 * @param target Nodo destino
	 * @return Si la operación es válida true, false en caso contrario
	 */
	public boolean bindEnds(DraggableNode source, DraggableNode target) {
		toBack();
		// Verificar que sea una conexión válida
		if (target.getNodo().validarConexion(source.getNodo(), enlaceAbs)) {
			// Agregar "referencias" a los nodos que conecta
			updateSource(source);
			updateTarget(target);

			// Mostrar heads
			svgHeadA.setVisible(true);
			svgHeadB.setVisible(true);

			// Registar el enlace en los nodos
			if (enlaceAbs != null) {
			 	padding_source = source.registerLink(getId(), enlaceAbs);
				padding_target = target.registerLink(getId(), enlaceAbs);
				//padding_source *= 20;
				//padding_target *= 20;
			}
			// Dibujar
			redrawLink();
			drawPolyline();

			return true;
		} else {
			if(old != null){
				old.registerLink(getId(), enlaceAbs);
				svgHeadA.setVisible(true);
				svgHeadB.setVisible(true);
			}
			return false;
		}
	}

	/**
	 * Dibujar una linea si el enlace se hace entre un nodo consigo mismo.
	 */
	private void drawPolyline(){
		if(source != null && source == target){
			polyline.toBack();
			int padding_poly = 15;
			//Inicio
			points[0] = node_link.getStartX();
			points[1] = node_link.getStartY();

			//Final
			points[10] = node_link.getEndX();
			points[11] = node_link.getEndY();

			//Salir de la derecha
			points[2] = points[0] - padding_poly;
			points[3] = points[1];

			//Abajo
			points[4] = points[2];
			points[5] = points[3] + (source.getHeight() / 2.0) + padding_poly;

			//Izquierda
			points[6] = points[10] + padding_poly;
			points[7] = points[5];

			//Arriba
			points[8] = points[6];
			points[9] = points[11];

			polyline.getPoints().clear();
			for(int i = 0; i < 12; i+=2){
				//System.out.println("X: "+ points[i] + " Y: " + points[i+1]);
				polyline.getPoints().addAll(points[i], points[i+1]);
			}
			polyline.setVisible(true);
		}else{
			polyline.setVisible(false);
		}
	}
}