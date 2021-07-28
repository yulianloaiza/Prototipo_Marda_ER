package application.controllers.node;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.Optional;

/**
 * Menu que se despliega en los campos que se pueden agregar a los nodos
 */
public class LabelContextMenu extends ContextMenu {
    /**
     * Constructor
     * @param label Campo al que se le agregará la funcionalidad de este menú
     * @param list_view VBox con los campos del nodo
     */
    public LabelContextMenu(Label label, VBox list_view){
        MenuItem menuItem1 = new MenuItem("Eliminar");
        menuItem1.setOnAction(actionEvent -> list_view.getChildren().remove(label));

        MenuItem menuItem2 = new MenuItem("Modificar");
        menuItem2.setOnAction(actionEvent -> {
            TextInputDialog dialog = new TextInputDialog(label.getText());
            dialog.setTitle("Insertar nuevo campo");
            dialog.setHeaderText("Formato: <nombre_campo>:<tipo_dato>. Ejemplo id:int");
            dialog.setContentText("Ingrese un campo válido:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(label::setText);
        });

        Menu fontStyleMenu = new Menu("Cambiar estilo");
        MenuItem bold = new MenuItem("Bold");
        bold.setOnAction( e -> {
            label.setFont(Font.font("System", FontPosture.REGULAR, label.getFont().getSize()));
            label.getStyleClass().clear();
            label.setStyle("-fx-font-weight: bold");
        });

        MenuItem italic = new MenuItem("Italic");
        italic.setOnAction(e -> {
            label.getStyleClass().clear();
            label.setStyle("-fx-font-weight: none");
            label.setFont(Font.font("System", FontPosture.ITALIC, label.getFont().getSize()));
        });

        MenuItem normal = new MenuItem("Normal");
        normal.setOnAction(e -> {
            label.setFont(Font.font("System", FontPosture.REGULAR, label.getFont().getSize()));
            label.getStyleClass().clear();
            label.setStyle("-fx-font-weight: normal");
        });
        fontStyleMenu.getItems().addAll(bold, italic, normal);

        getItems().addAll(menuItem1,menuItem2, fontStyleMenu);
    }
}
