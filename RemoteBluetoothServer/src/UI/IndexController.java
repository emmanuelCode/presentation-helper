package UI;

import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import test.IO;
import test.PPT;
import test.Project;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import java.util.Optional;
import java.util.ResourceBundle;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import office.MsoTriState;

import org.apache.commons.io.FileUtils;
import org.controlsfx.control.StatusBar;

import com.google.gson.Gson;
import com.intel.bluetooth.BlueCoveImpl;
import bluetooth.LiteProject;
import bluetooth.ProcessConnectionThread;
import java.util.ArrayList;
import java.util.Arrays;


public class IndexController implements Initializable{
	Stage primaryStage; //instance of itself
	Stage stage; // for rest of the scenes
	Pane mainPane; //Honestly I do know I need it but not why
	FXMLLoader loader; //For loading FXML Files  (Layout Files)
	Scene scene; //for switching scenes
	ArrayList<Project> projects; //all the projects Instances

	
	
	 public IndexController() {
		primaryStage = Main.getPrimaryStage();
		primaryStage.setResizable(false);
		
		stage = new Stage();
		setEvents();
		
	}
	 public  void setConnected(){
		 StatusBarLabel.setText("Connected"); 
	 }
	 public  void setNotConnected(){
		 StatusBarLabel.setText("Not Connected");
	 }


    @FXML
    private AnchorPane IndexAnchor;

    @FXML
    private StatusBar connectionStatusBar;

    @FXML
    public  Label StatusBarLabel;

    @FXML
    private TextField searchTextField;

    @FXML
    private ListView<Project> projectListView;
  
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	//connectionStatusBar.setPadding(new Insets(0,0,-100,0));

    	//thread.start();1 
    	readProjects();
    	// in order to just pick on item at a time
    	projectListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	
    	projectListView.setCellFactory(new Callback<ListView<Project>, ListCell<Project>>() {

			@Override
			public ListCell<Project> call(ListView<Project> param) {
				ListCell<Project> projectListCell = new ListCell<Project>(){
					@Override
					protected void updateItem(Project item, boolean empty) {
						super.updateItem(item, empty);
						
						if(item != null || !empty){
							Image img = item.getIcon();
							ImageView imgView = new ImageView(img);
							imgView.setImage(img);
							setGraphic(imgView);
							setText(item.getName());
							
						}else{
							setGraphic(null);
							setText(null);
						}
					}
				};
				
				return projectListCell;
			}
		});
    	
    	
    	loadViewList();

	}
    @FXML
    void onStartClick(ActionEvent event) throws IOException {

    	
		
    	if(isSelected()){
    	OutputStream outputStream = ProcessConnectionThread.outputStream;
    	Project project = selectedProject();
    	Main.test = new PPT(project, MsoTriState.msoTrue);
    	Main.test.start();
    	System.out.println(Arrays.toString(Main.test.getNotes()));
    	
    	if(outputStream != null){
    		
    		project.setNoteArray();
    		Gson gson = new Gson();
        	
        	
        	LiteProject liteProject = new LiteProject(project);
        	String jsonStr = gson.toJson(liteProject);
    		DataOutputStream dou = new DataOutputStream(outputStream);
    		System.out.println(jsonStr);
    		
    		dou.writeUTF(jsonStr);
    		
    		
    	 
    	 
    	 
    	}else{
    		System.out.println("Please connect a device before starting!");
    	}

    }	
    }
    @FXML
    void onAddClick(ActionEvent event) throws IOException {
    	FXUtils.switchScene(SceneSelection.MENU, projects, selectedIndex() ,false);
    }
    
    @FXML
    void onEditClick(ActionEvent event) throws IOException {
    	if(isSelected()){
    		FXUtils.switchScene(SceneSelection.NOTES_CHOICE, projects, selectedIndex(), false);
    	}
   	
    	
    	
    	

    }

    @FXML
    void onRemoveClick(ActionEvent event) {
    	if(isSelected()){
    		if(removeConfirm()){
    	int i = selectedIndex();

    	try {
			FileUtils.deleteDirectory(projects.get(i).getProjectFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	projects.remove(i);
    	loadViewList();
    		}
    	}
    	

    }
    

    @FXML
    void onImportExportClick(ActionEvent event) throws ClassNotFoundException {
    		importExportChoice();

    }

    
    @FXML
    void onKeyReleasedSearch(KeyEvent event){
    	//Main.test.getCount();
    	projectListView.getItems().clear();
    	if (searchTextField.getText() != null){
    		for (Project project : projects){
    			if(project.getName().contains(searchTextField.getText())){
    				projectListView.getItems().add(project);
    			}
    		}
    	}else{loadViewList();}
    }
    
   	public boolean isSelected(){
   		boolean b = selectedIndex() > -1;
   		if(!b){

	    	Alert alert = new Alert(AlertType.ERROR);
	    	alert.setTitle("Error");
	    	alert.setHeaderText("No prensetation selected.");
	    	alert.setContentText("Please select a presentation!");

	    	ButtonType buttonTypeOk = new ButtonType("Ok");
	    	alert.getButtonTypes().setAll(buttonTypeOk);
	    	alert.show();
	    	
   		}
   		
   		return b;
   	}
   	
   	public boolean removeConfirm(){
   		
   		boolean b = false;
   		String str = null;
   		switch (selectedProject().getProjectType()){
   		case 0:
   			str = "Do you wish to delete this presentation? \n"
   					+ "The notes and the PowerPoint file will be permanently deleted.";
   					break;
   		case 1:
   			str = "Do you wish to delete this presentation? \n"
   					+ "The notes will be permanently deletd";
   			break;
   		}
   		
   		Alert alert = new Alert(AlertType.CONFIRMATION);
   		alert.setTitle("Confirmation");
   		alert.setHeaderText(str);
   		alert.setContentText("Please confirm your choice.");
   		
   		ButtonType buttonTypeYes = ButtonType.YES;
    	ButtonType buttonTypeNo = ButtonType.NO;
    	


    	alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
    	//alert.onCloseRequestProperty(Event)
    	

    	Optional<ButtonType> result = alert.showAndWait();;
    	if (result.get() == buttonTypeYes){
    	b = true;
    	}
   		
   		return b;
   	}
   	
   	public void importExportChoice() throws ClassNotFoundException{
   		final Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Import/Export");
    	alert.setHeaderText("Do you want to import or export?");
    	alert.setContentText("Choose your option.");
    	ButtonType buttonTypeCancel = new ButtonType("Cancel");
    	ButtonType buttonTypeImport = new ButtonType("Import");
    	ButtonType buttonTypeExport = new ButtonType("Export");
    	


    	alert.getButtonTypes().setAll(buttonTypeImport, buttonTypeExport ,buttonTypeCancel);
    	Optional<ButtonType> result = alert.showAndWait();
    	if(result.get() == buttonTypeCancel){
    		
    	}
    	
    	else if (result.get() == buttonTypeImport){
    		try {
				importProject();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	}

    		
    	else if (result.get() == buttonTypeExport) {
    		if (isSelected()){
    			System.out.println("EXPORT");
    			FXUtils.switchScene(SceneSelection.EXPORT, projects, selectedIndex(), false);
    			
    			}
    		
    		}
    	

		
	}
   	private void importProject() throws ClassNotFoundException, IOException{
   		FileChooser fileChooser = new FileChooser();
		//FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Compressed File (.zip)", ".zip");
		//fileChooser.getExtensionFilters().add(extFilter);
		File pathFile = fileChooser.showOpenDialog(primaryStage);
		ZipFile importFile = null;
		if (pathFile != null){
		try {

			
			importFile = new ZipFile(pathFile);
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File importedProject = null;
		String error = null;
		
		
			try {
				importedProject = IO.importProject(importFile);
			} 
				catch (FileNotFoundException |ClassNotFoundException e) {
					System.out.println("this project file is not valid");
					e.printStackTrace();
					
			} catch (ZipException e) {
				System.out.print("this is not a valid zip file: " + e.toString());
				
			} catch (IOException e) {
				System.out.println(e.toString());
			}
			
			if(importedProject != null) {
				File data = new File(importedProject + "/"+"data.dat");
				projects.add(IO.deserialize(data));
				Alert importSuccesAlert = new Alert(AlertType.INFORMATION);
				importSuccesAlert.setTitle("Succes!");
				importSuccesAlert.setHeaderText("the project " + importedProject.getName() + " has been imported!");
				importSuccesAlert.show();
			}
			
			loadViewList();
		}
   	}

    private void loadViewList(){
    	projectListView.getItems().clear();
    	
    	try{
    		
        	for(Project project : projects){
        		projectListView.getItems().add(project);
        	}
        	
        	}catch(NullPointerException ex){
        		
        	}

    }
   
private Project selectedProject(){return projects.get(selectedIndex());}
private int selectedIndex(){return projectListView.getSelectionModel().getSelectedIndex();}

public  void readProjects(){
	projects = new ArrayList<Project>();
	try {
		projects = IO.deserialize();
	} catch (ClassNotFoundException e) {
		projects = new ArrayList<Project>();
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}
public void setEvents(){	
	primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
    public void handle(WindowEvent we) {
    	System.out.println(projects.size());
    	  for (Project project: projects){
    		  
    		  IO.serialize(project,IO.dataDir.getName());
    		  }
    	  try{
    		  if (Main.test != null ) Main.test.close();
    	  }catch (com4j.ComException e){}
          BlueCoveImpl.shutdown();
      }
  });
	
primaryStage.setOnShown(new EventHandler<WindowEvent>() {

	@Override
	public void handle(WindowEvent event) {
		
		loadViewList();
		
	}
});
	}
}
