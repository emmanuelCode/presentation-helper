package UI.edit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;



import UI.IndexController;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import office.MsoShapeType;
import office.MsoTriState;
import ppt.AnimationBehavior;
import ppt.AnimationBehaviors;
import ppt.PpPlaceholderType;
import ppt.SlideShowView;
import ppt.Slides;
import ppt._Application;
import ppt._Presentation;
import ppt._Slide;
import test.IO;
import test.PPT;
import test.Project;

public class NoteBuilderController extends Application implements Initializable  {
	private int index;
	private Project project;
	private List <Image> slidesImages;
	
	InputStream is;
	Boolean listenerThreadIsRunning;
	 //The PowerPoint application Instance
		_Application app;
		// Your project
		_Presentation presentation;
		//List that have all the slides
		Slides slides;
		//single Slide
		_Slide slide;
		//for testing purpose
		AnimationBehaviors effects;
		AnimationBehavior effect ;
		PPT notesReader;
		
		SlideShowView testShowView;
	
	
	public NoteBuilderController(Project getProject) {
		
		project = getProject;
		notesReader = new PPT(project,MsoTriState.msoFalse);
	
		index = 0;
		
		
		
	    
		
		
	}
	
		@FXML
		private TextArea noteTextArea;
	
	  	@FXML
	    private ImageView slideImageView;
	  	
	    @FXML
	    private Label slideLocationLabel;

	    

	    
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	
    	notesReader.start();
    	
    	//Start a PowerPoint instance
    			app = ppt.ClassFactory.createApplication();
    			//Get the powerpoint file
    		    presentation = app.presentations().open(project.getPresentationFile().toString(),
    		    		MsoTriState.msoFalse, MsoTriState.msoFalse,MsoTriState.msoTrue);
    		    slides = presentation.slides();
    	
    	
    	
    	
    	
    //	project.updateArrays();
    	slidesImages = new ArrayList<Image>();
    	
		for(File file : IO.getFiles(project.getPreviewFile())){
			try {
				slidesImages.add(new Image(file.toURL().toString()));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		slideImageView.setImage(slidesImages.get(index));
	
		noteTextArea.setText(notesReader.read(index));
		//readText();
		slideLocationLabel.setText(index + 1 + "/" + slidesImages.size());

			
			
		
		
	}

    @FXML
    void OnPrevClick(ActionEvent event) {
    	if(index > 0 ){
    	index --;
    	slideImageView.setImage(slidesImages.get(index));
    	saveChanges();
    	
    	
    	
    	
    	
    	noteTextArea.setText(/* project.readText(index)*/ notesReader.read(index));
    	slideLocationLabel.setText(index + 1 + "/" + slidesImages.size());
    		
    	}
    }

    @FXML
    void onNextClick(ActionEvent event) {
    	if (index < slidesImages.size()-1){
    		index ++;
    	slideImageView.setImage(slidesImages.get(index));
    	saveChanges();
    	
    	
    	
    	
    	
    	
    	noteTextArea.setText(/* project.readText(index)*/ notesReader.read(index));
    	slideLocationLabel.setText(index + 1 + "/" + slidesImages.size());
    	}

    }
    
    @FXML
    void onKeyReleased(KeyEvent event) {
    	String str = "";
    	if(noteTextArea.getText() !=null)notesReader.write(noteTextArea.getText(), index);
    		//project.writeText(noteTextArea.getText(),index);
    	//if(noteTextArea.getText() !=null)str =  noteTextArea.getText();
    	//project.setNotes(index, str);
    	//saveChanges();
    }
   
    @Override
	 public void start(Stage primaryStage) throws IOException {

		/*FXMLLoader loader = new FXMLLoader (getClass().getResource("NoteBuilder.fxml"));
		loader.setController(new IndexController());
		Pane mainPane = loader.load();
		Scene scene = new Scene(mainPane);
		primaryStage.setScene(scene);
		primaryStage.show();*/
		
		
    }
    
    
   public void saveChanges(){
	   notesReader.saveChanges();
   }
      
       
	

}
