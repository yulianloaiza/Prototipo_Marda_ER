package application;
	
import application.controllers.RootLayout;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

/**
 * Puerta de entrada de la aplicación, solo se encarga de cargar el frame del marda.
 */
public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();
		
		try {
			
			Scene scene = new Scene(root,640,480);
			scene.setFill(Color.WHITE);
			scene.getStylesheets().add(getClass().getResource("./views/application.css").toExternalForm());
			primaryStage.setTitle(Constants.NAME_APP);
			primaryStage.setScene(scene);
			primaryStage.setMaximized(true);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		root.setCenter(new RootLayout());
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
