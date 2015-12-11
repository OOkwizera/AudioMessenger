package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;



public class Main extends Application {
	
	 @Override
	    public void start(Stage primaryStage) throws Exception{
	        TabPane root = FXMLLoader.load(getClass().getResource("Gui.fxml"));
	        primaryStage.setTitle("AudioMessenger");
	        primaryStage.setScene(new Scene(root, 300, 400));
	        primaryStage.show();
	        primaryStage.setOnCloseRequest(event -> System.exit(0));
	    }
	
	public static void main(String[] args) {
		launch(args);
	}
}
