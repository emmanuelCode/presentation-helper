package UI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import UI.Add.MenuController;
import UI.edit.EditChoiceController;
import UI.edit.NoteBuilderController;
import UI.export.ExportController;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import test.Project;

public class FXUtils {
	private static Stage primaryStage = Main.getPrimaryStage();
	private static Stage stage;
	private static int previousScene;
	static NoteBuilderController noteBuilder;
	static ExportController export;
	static MenuController menu;
	static EditChoiceController edit;
	
	
	/**
	 * 
	 * @param sceneChoice 
	 * @param project : Optional, put null if not needed
	 * @param previousScene : Optional, put null if not needed
	 */
	
	public static void switchScene(final int sceneChoice, ArrayList<Project> projects,
			final int index ,final Boolean usePreviousScene){
		if(stage == null) stage = new Stage();
		stage.hide();
		try {
			stage.setScene(getScene(sceneChoice, projects, index,usePreviousScene));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Setting scene failed!");
			
		}
	   	stage.setResizable(false);
	   	stage.show();
	   	final ArrayList <Project> tmpProjects = projects;
	   	
	   	
	   	
	   	
		
		
		EventHandler<WindowEvent> onClose =new EventHandler<WindowEvent>() 
		{
				@Override
				public void handle(WindowEvent event) {
					if(usePreviousScene == false){
					
						
						
						primaryStage.show();
					}else{
						
						try {
							stage = new Stage();
							stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

								@Override
								public void handle(WindowEvent event) {
										
									
									primaryStage.show();
									
								}
							});
							stage.hide();
							
							stage.setScene(getScene(previousScene, tmpProjects, index, false));
							stage.show();
							
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
			};
			stage.setOnCloseRequest(onClose);
			if(edit != null){
				edit.closeThread();
				edit = null;
		}
			if(noteBuilder != null){
				noteBuilder.saveChanges();
				noteBuilder = null;
			}
		
		stage.show();
		primaryStage.hide();
	}
	
	
	
	private static Scene getScene(final int choice, ArrayList<Project> projects, int index, Boolean usePreviousScene) throws IOException{
		FXMLLoader loader = null;
		Scene sceneGiver = null;
	
		switch(choice){
		case SceneSelection.MENU:
			loader = new FXMLLoader(FXUtils.class.getResource("Add/Menu.fxml"));
			menu = new MenuController(projects);
			loader.setController(menu);
			if(!usePreviousScene)previousScene = SceneSelection.MENU;
			break;
		
		case SceneSelection.NOTES_CHOICE:
			loader = new FXMLLoader(FXUtils.class.getResource("Edit/EditChoice.fxml"));
			edit = new EditChoiceController(projects.get(index));
			loader.setController(edit);
			if(!usePreviousScene)previousScene = SceneSelection.NOTES_CHOICE;
			
			break;
		case SceneSelection.EXPORT:
			loader = new FXMLLoader(FXUtils.class.getResource("export/export.fxml"));
			export = new ExportController(stage,projects.get(index));
			loader.setController(export);
			if(!usePreviousScene)previousScene = SceneSelection.EXPORT;
			break;
		
		case SceneSelection.NOTES:
			loader = new FXMLLoader(FXUtils.class.getResource("Edit/NoteBuilder.fxml"));
			noteBuilder = new NoteBuilderController(projects.get(index));
			loader.setController(noteBuilder);
			if(!usePreviousScene)previousScene = SceneSelection.NOTES;
			break;
			
		}
		
		if (loader != null){sceneGiver = new Scene((Parent) loader.load());}
		
		
		return sceneGiver;
	}
	private void doOnClose(){
		
   		
   	}
	

}
