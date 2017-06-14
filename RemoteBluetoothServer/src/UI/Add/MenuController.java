
package UI.Add;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.controlsfx.control.StatusBar;

import UI.FXUtils;
import UI.Main;
import UI.SceneSelection;
import UI.edit.NoteBuilderController;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import test.IO;
import test.Project;

public class MenuController extends Application implements Initializable {
	File file;
	Stage prevStage;
	Stage stage;
	Pane mainPane;
	FXMLLoader loader;
	Scene scene;
	int index;
	ArrayList<Project> projects;
	int projectType = 0;
	public  MenuController(ArrayList<Project> getProject) {
		prevStage = Main.getPrimaryStage();
		projects = getProject;


	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		pptRadioButton.setSelected(true);
		
	}
	 	@FXML
	    private RadioButton pptRadioButton;

	    @FXML
	    private RadioButton slideRadioButton;

	    @FXML
	    private TextField pathTextField;
	    
	    @FXML
	    private StatusBar statusBar;

	    @FXML
	    void OnPPTClick(ActionEvent event) {
	    	slideRadioButton.setSelected(false);
	    	projectType = 0;

	    }

	    @FXML
	    void OnSlideClick(ActionEvent event) {
	    	pptRadioButton.setSelected(false);
	    	projectType = 1;

	    }

	    @FXML
	    void onConfirmClick(ActionEvent event) {

	    	add();
	    }

	    @FXML
	    void onOpenClick(ActionEvent event) {
	    	FileChooser fileChooser = new FileChooser();
	    	FileChooser.ExtensionFilter extFilter = 
	    			new FileChooser.ExtensionFilter("Presentation Files", 
	    			"*.ppt","*.pptx","*.pptm","*.ppsx","*.pps","*.xml");
	    	fileChooser.getExtensionFilters().add(extFilter);
	    	file = fileChooser.showOpenDialog(stage);
	    	if (file != null)
	    	pathTextField.setText(file.toString());
	    	

	    	
	    }
	    private void add()  {
	    	String fullName = file.getName();
	    	String extension = fullName.substring(fullName.indexOf("."));
	    	String name = fullName.substring(0,fullName.indexOf("."));
	    	
	    	
	    	File duplicate = IO.fileEnumeration(name, IO.dataDir.toString());
	    	Boolean isDupilcate = !duplicate.getName().equals(name);
	    	
	    	if(isDupilcate){
	    		
	    		Path duplicatePath = null;
				try {
					duplicatePath = 
							Files.copy(file.toPath(), 
									new File(IO.tempDir +"/" + 
							duplicate.getName() + extension).toPath());
				} catch (IOException e) {
					isDupilcate = false;
					IO.print(e.toString());
				}
	    		file = new File (duplicatePath.toFile().toString());
	    		
	    	}
	    		
	    	
	    	
	    	Project project = new Project(file,projectType);
	    	File fileDest = project.getProjectFile();
	    	fileDest.mkdirs();
	    	try {
				FileUtils.copyFileToDirectory(file,fileDest);
				if(isDupilcate) FileUtils.forceDelete(file);
				fileDest.renameTo(new File(fileDest.toString() + "." +FilenameUtils.getExtension(fileDest.toString())));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	project.updateDirectories(IO.dataDir.getName());
	    	projects.add(project);

	    			statusBar.setText("Creating Slide Images");
					try {
						createSlidesImages();
					} catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

	 
			
	    }
	    
	    public void loadNoteBuilder() {
	    	FXUtils.switchScene(SceneSelection.NOTES, projects, projects.size() - 1, false);
	    }
	    
	    @SuppressWarnings({ "unchecked", "rawtypes" })
		public void createSlidesImages() throws IOException, InterruptedException{
			   Task task = projects.get(projects.size() - 1).slidesToPng(statusBar);
			   task.stateProperty().addListener(new ChangeListener<Worker.State>() {

					@Override
					public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
						if(newValue==Worker.State.SUCCEEDED){
							statusBar.setText("Done!");
							Alert alert = new Alert(AlertType.CONFIRMATION);
					    	alert.setTitle("Confirmation");
					    	alert.setHeaderText("Do you want to build notes now?");
					    	alert.setContentText("Choose your option.");

					    	ButtonType buttonTypeYes = new ButtonType("Yes");
					    	ButtonType buttonTypeNo = new ButtonType("No");


					    	alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

					    	Optional<ButtonType> result = alert.showAndWait();
					    	if (result.get() == buttonTypeYes){
					    	    loadNoteBuilder();
					    	} else if (result.get() == buttonTypeNo) {
					    	    // ... user chose "Two"
					    	}
						}
						
					}
				});
		   }
		@Override
		public void start(Stage primaryStage) throws Exception {
			// TODO Auto-generated method stub
			
		}
		
	    
	    

		
}
