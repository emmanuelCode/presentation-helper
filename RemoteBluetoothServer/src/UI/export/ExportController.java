package UI.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.zip.ZipException;

import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;

import org.apache.commons.io.CopyUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Labeled;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import test.IO;
import test.Project;

public class ExportController implements Initializable {
    @FXML
    private RadioButton pptRadioButton;

    @FXML
    private RadioButton NotesRadioButton;

    @FXML
    private TextField pathTextField;
    
    private AutoCompletionBinding<File> fileBinding;
    private List<File> fileList;
    
    File file;
    File validFile;
    
    Stage stage;
    Project project;
    String str;
    
    public ExportController(Stage getStage, Project getProject) {

		stage = getStage;
		project = getProject;
		fileList = new ArrayList<File>();
		
	}
    
    @FXML
    void OnTextFieldReleased(KeyEvent event) {
    	//fileBinding.dispose();
    	file = new File(pathTextField.getText());

    	/*if (file.exists()) validFile = new File(file.toString());
    	fileList = Arrays.asList(validFile.listFiles());
    	TextFields.bindAutoCompletion(pathTextField, FXCollections.observableArrayList(fileList));*/
    	
    	
    }

    @FXML
    void OnNotesAndPPTClick(ActionEvent event) {
    	pptRadioButton.setSelected(false);

    }

    @FXML
    void OnPPTClick(ActionEvent event) {
    	NotesRadioButton.setSelected(false);

    }

    @FXML
    void onConfirmClick(ActionEvent event)  {
    	String exportTypeStr = null;
    	str = pathTextField.getText();
    	if (str != null && str.length() > 0)
    	{
    		
			if (!str.substring(str.length() - 1).equals("")){
				str += "\\";
			}
			file = new File(str);
	    	if (NotesRadioButton.isSelected())
	    	{
	    		IO.serialize(project,IO.dataDir.getName());

					str = IO.zipIt(project.getProjectFile().toString(),file.toString()  + "\\" + project.getName());
					
					exportTypeStr = "Presentation and notes";
	    		}else{
	    		
	    			try {
	    				String strPres = project.getPresentationFile().toString();
	    				strPres = strPres.substring(strPres.lastIndexOf("\\") ,strPres.length()); 
	    				strPres = file.toString() +strPres;
	    				System.out.println(new File(strPres).getName());
	    				System.out.println();
	    					/*while(new File(strPres).exists()){
	    						
	    					}*/
	    				
	    				String in = project.getPresentationFile().toString();
	    				File tempFile = IO.fileEnumeration(in, str);
						Files.copy(project.getPresentationFile().toPath(), tempFile.toPath());
						str = tempFile.toString();
						exportTypeStr = "Presentation";
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}
    	
	    	Alert alert = new Alert(AlertType.INFORMATION);
	    	alert.setHeaderText("Export Successful!");
	    	alert.setContentText("Your " + exportTypeStr + " has been exported to the directory " + str);
	    	alert.show();
    	}else {
    		Alert errorAlert = new Alert(AlertType.ERROR);
    		errorAlert.show();
    	} //error message
    	 
    		
    	}
			
    	
    		
    	
    	

    
    
    @FXML
    void onOpenClick(ActionEvent event) {
    	DirectoryChooser dirChooser = new DirectoryChooser();
    	File pathFile =dirChooser.showDialog(stage);
    	if(pathFile != null)pathTextField.setText(pathFile.toString());
    }
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}

}
