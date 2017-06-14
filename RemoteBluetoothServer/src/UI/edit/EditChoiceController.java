package UI.edit;



import java.awt.Desktop;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.controlsfx.control.StatusBar;

import UI.FXUtils;
import UI.SceneSelection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ppt.AnimationBehavior;
import ppt.AnimationBehaviors;
import ppt.SlideShowView;
import ppt.Slides;
import ppt._Application;
import ppt._Presentation;
import ppt._Slide;
import test.Project;

public class EditChoiceController implements Initializable{
	Project project;
	  @FXML
	    private Button notesButton;

	   @FXML
	    private Button presentationButton;

	   @FXML
	   private StatusBar statusBar;


	   private Image notesImage;

	   private Image presImage;
	   
	   Thread listenerThread;
	   
	   Boolean listenerThreadIsRunning;
	

	public EditChoiceController(Project getProject){
		project = getProject;
	}





		@Override
		public void initialize(URL location, ResourceBundle resources) {

			notesImage = new Image(getClass().getResource("/res/Edit_150.png").toString());
			switch(project.getProjectType()){
			case 0:
				presImage = new Image(getClass().getResource("/res/PowerPoint_150.png").toString());
				break;
			case 1:
				presImage = new Image(getClass().getResource("/res/Google_Slides_150.png").toString());
				break;
			}
			notesButton.setGraphic(new ImageView(notesImage));
			presentationButton.setGraphic(new ImageView(presImage));


		}
		public void closeThread(){
			listenerThreadIsRunning = false;
		}

		public void loadNoteBuilder(){
			ArrayList<Project> tempArray = new ArrayList<Project>();
            tempArray.add(project);
				FXUtils.switchScene(SceneSelection.NOTES, tempArray, 0, true);
		}


		   @SuppressWarnings("unchecked")
		@FXML
		    void onNotesClick(ActionEvent event) throws IOException, InterruptedException {


			   listenerThreadIsRunning = false;




			   /*for (double i = 0.0; i < 1.0; i += 0.1){
				   statusBar.setProgress(i);
				   System.out.println(statusBar.getProgress());
				   Thread.sleep((long) 1000);
			   }*/

			   if(project.getLastModified() == project.getPresentationFile().lastModified()){
				   loadNoteBuilder();
			   }else{
				   statusBar.setText("Updating slide Images");
				   updateAndLoadNote();


			   }



			   	}

		   /*public void loadNoteBuilder() throws IOException{
			   FXMLLoader loader = new FXMLLoader (getClass().getResource("Notebuilder.fxml"));
		    	loader.setController(new NoteBuilderController(project));
		    	Stage stage = new Stage();
		    	Scene scene;



				scene = new Scene((Parent) loader.load());
		   	stage.setScene(scene);
		   	stage.setResizable(false);
		   	stage.show();



		   }*/

		   @SuppressWarnings("unchecked")
		public void updateAndLoadNote() throws IOException, InterruptedException{
			   Task task = project.slidesToPng(statusBar);
			   task.stateProperty().addListener(new ChangeListener<Worker.State>() {

					@Override
					public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
						if(newValue==Worker.State.SUCCEEDED){
							statusBar.setText("Done!");
							loadNoteBuilder();
						}

					}
				});
		   }
		    @FXML
		    void onPresentationClick(ActionEvent event) {
		    	Desktop desktop = Desktop.getDesktop();
		    	try {
					desktop.open(project.getPresentationFile());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	modificationListener();
		    	
		    	 	
		    }
		    // So this is method have the purpose to listen if the PowerPoint file has been modified
		    // if so then slides will be Updated 
		    @SuppressWarnings("unchecked")
			public void modificationListener(){
		    	listenerThreadIsRunning = true;
		    	listenerThread = new Thread(getListenerTask());
		    	listenerThread.start();
		    	
		    }
		    
		    @SuppressWarnings("unchecked")
			public Task getUpdaterTask(){
		    	
						Task worker = null;
						try {
							worker = project.slidesToPng(statusBar);
						} catch (IOException | InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						worker.setOnSucceeded(new EventHandler<Event>() {

							@Override
							public void handle(Event event) {
								statusBar.setText("Done!");
								
							}
						});
					
				
				return worker;
				
				
		    }
		    @SuppressWarnings("unchecked")
			public Task getListenerTask(){
		    	Task listenerTask = new Task() {

					@Override
					protected Object call() throws Exception {
						
						while(project.getLastModified() == project.getPresentationFile().lastModified() &&
								listenerThreadIsRunning){
							Thread.sleep(500);
						}
						return null;
					}
					
				};
				listenerTask.setOnSucceeded(new EventHandler<Event>() {

					@Override
					public void handle(Event event) {
						Thread updaterThread = new Thread(getUpdaterTask());
						updaterThread.start();
						statusBar.setText("Updating Slide Images");
						
					}
				});
				
				return listenerTask;
		    }
}
