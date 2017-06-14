package UI;

import javafx.application.Application;
import javafx.event.EventHandler;
import bluetooth.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.intel.bluetooth.BlueCoveImpl;

import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import test.IO;
import test.PPT;
import test.Project;

public class Main extends Application{
	public static PPT test; //TODO not good practice
	private static Stage primaryStage;
	public static Stage getPrimaryStage(){return primaryStage;}
	 
	
	

	public static void main(String[] args) throws IOException {
		RemoteBluetoothServer server = new RemoteBluetoothServer(); 
		server.start();
		Application.launch(args);

	}

	

	//Starts the main UI AKA Index
	@Override
	 public void start(Stage primaryStage) throws IOException {
		this.primaryStage = primaryStage;
		FXMLLoader loader = new FXMLLoader (getClass().getResource("Index.fxml"));
		loader.setController(new IndexController());
		Pane mainPane = loader.load();
		Scene scene = new Scene(mainPane);
		primaryStage.setScene(scene);
		primaryStage.show();
		
    }
	



}
