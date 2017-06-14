package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.openxml4j.exceptions.PartAlreadyExistsException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.sl.usermodel.Placeholder;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFNotes;
import org.apache.poi.xslf.usermodel.XSLFNotesMaster;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.controlsfx.control.StatusBar;

import javafx.concurrent.Task;
import javafx.scene.image.Image;

public class Project implements Serializable {
	//verison of the class
	public static final long serialVersionUID = 1L;
	private int slideCount;
	private int[] animationCount;
	//PowerPoint or Google Slide?
	private int projectType;
	//The notes that will be displayed on the watch
	private String[] notes; // TODO Save the note in the PowerPoint file.
	//Where the files of the project is stored
	private String dir;
	//Where the serialized Instance Is stored
	private String dataPath;
	//name of the project
	private String name;
	//name of the Powerpoint File
	private String presFileName;
	//Extension of the powerpoint File
	private String extension;
	//Last modified PowerPoint File
	private long lastModified;
	//Where all the projects files are stored
	static  transient File dataDir;
	//Same as dir  where the project is stored
	private transient File project;
	//where the PowePoint File is stored
	private transient File presentation;
	//Where the serialized Instance Is stored...
	private transient File data;
	//Where the previews image of slide is stored
	private transient File preview;
	//Icon of the Project Type
	private transient Image icon;
	
	private transient XMLSlideShow slideShow;
	
	/**
	 * 
	 * @param getPFile
	 * the powerpoint File
	 * @param getProjectType
	 * Google Slide Or PowerPoint?
	 */
	public Project(File getPFile ,int getProjectType){
		
		
		
		
		//this is a meesssssssssssssssssssssss but its working xD
		
		
		presFileName = getPFile.getName();
		name = presFileName.substring(0,presFileName.indexOf("."));
		extension = presFileName.substring(presFileName.indexOf("."));
		
		
		dataDir = new File(IO.userDir + "/data");
		if(!dataDir.exists()){ 
			dataDir.mkdir();
		}
		System.out.println("dataDir: " + dataDir.toString()); // Debugging noob style xD
		
		dir = dataDir.toString() + "\\" + name;
		
		System.out.println("dir: " + dir);
		
		dataPath = dir + "\\" + "data" + ".dat";
		
		System.out.println("dataPath: " + dataPath);

		data = new File(dataPath);
		
		System.out.println("data: " + data.toString());
		
		presentation = new File(dir + "\\" + name + extension);
		
		System.out.println("presentation: " + presentation.toString());
		
		project = new File(dir);
		
		System.out.println("project: " + project.toString());
		
		preview = new File(dir + "\\" + "preview");
		
		System.out.println("preview: " + preview.toString());
		
		
		
		projectType = getProjectType;
		//Getting the proper icon
		switch (projectType){
		case 0:
				icon = new Image ( getClass().getResource("/res/PowerPoint_25.png").toString());
			break;
		case 1:
			icon = new Image (getClass().getResource("/res/Google_Slides_25.png").toString());
			break;
		}

	}
	/**
	 * 
	 * @param where
	 * for specifing if its on the data file or the Temp file
	 * 
	 * this method is called everytime the instance is deserialized
	 * used in the reason that filePaths can change.
	 * 
	 */
	public void updateDirectories(String where) {
		dataDir = new File(IO.userDir + "/" + where);
		dir = dataDir.toString() + "\\" + name;
		dataPath = dir + "\\" + "data" + ".dat";
		data = new File(dataPath);
		presentation = new File(dir + "\\" + name + extension);
		project = new File(dir);
		preview = new File(dir + "\\" + "preview");
		lastModified = presentation.lastModified();
		String url= null;
		switch (projectType){
		case 0:
				icon = new Image ( getClass().getResource("/res/PowerPoint_25.png").toString());
			break;
		case 1:
			icon = new Image (getClass().getResource("/res/Google_Slides_25.png").toString());
			break;
		}
		
		
		
		
	}
	//all the getters
	public int getSlideCount(){return slideCount;}
	public String getDataPath(){return dataPath;}
	public File getProjectFile(){return project;}
	public File getDataFile(){return data;}
	public String getName(){return name;}
	public File getPreviewFile(){return preview;}
	public String[] getNotes(){return notes;}
	public File getPresentationFile(){return presentation;}
	public Image getIcon(){return icon;}
	public int getProjectType(){return projectType;}
	public long getLastModified(){return lastModified;}
	public int[] getAnimationCount(){return animationCount;}
	
	
	//all the setters
	public void setLastModified(){lastModified = presentation.lastModified();}
	public void setNotes(int index, String str){notes[index] = str;}
	public void setName(String setName){name = setName;}
	
	
	// not sure if needed anymore
	public void init(){
		
			if(!data.exists()){
				try {
					data.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
			
	}
	
	//get the count of slides off a PowerPoint file
	public int getCount() throws FileNotFoundException, IOException{
		
		XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(presentation));
		int count = ppt.getSlides().size();
		ppt.close();
		return count;
		
	}
	
	
	//TODO have to be fixed. Works only when the size is bigger but if small its GG
	//Updating the Array. This is for when the PowerPoint File is modified.
	//TODO implement the Animation count. May be impossible tho
	public void updateArrays(){
		
		int count = -1;
	try {
		count = getCount();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	if(count != -1){
		if (notes != null && notes.length != count){
			String[] temp = new String[count];
			
			for(int i = 0; i < notes.length + 1; i++){
				temp[i] = notes[i];
			}
			notes = temp;
			
			
		}
		if (animationCount != null && animationCount.length != count){
			int[] temp = new int[count];
			int length = animationCount.length;
			for(int i = 0; i < length; i++){
				temp[i] = animationCount[i];
			}
			animationCount = temp;
			
			
		}
		if(notes == null) notes = new String[count];
		
		if(animationCount == null) animationCount = new int[count];
		
		for (int i = 0; i < notes.length; i ++) if (notes[i] == null) notes[i] = "";
	}
	
		
	}
	
	
	
	
	
	
	  
	  // task that takes the slides of PowerPoint and convert it in Png images GO see exportToPngTask
	   public Task slidesToPng(StatusBar statusBar ) throws IOException, InterruptedException{
		   
		   Task task = exportToPngTask(statusBar); 
		   
		   
		   //unBind in case a task is binded
           statusBar.progressProperty().unbind();
           //bind the status bar to the task
           statusBar.progressProperty().bind(task.progressProperty());
           
           
           
           
           
           // start the task
           Thread test = new Thread(task);
           test.start();
           return task;
           
           
           
           
	   }
	   
	   //TODO To be built if possible
	   public int[] updateAnimationCount(){
		   
		   
		   return animationCount;
	   }
	   public Task exportToPngTask(final StatusBar statusBar){
	        return new Task() {

	                   @Override
	                   protected Object call() throws Exception {
	                	   //creating an empty presentation
	          	         
	          	         XMLSlideShow ppt;
	          	         ppt = new XMLSlideShow(new FileInputStream(presentation));
	          	         
	          	         //getting the dimensions and size of the slide 
	          	         Dimension pgsize = ppt.getPageSize();
	          	         List<XSLFSlide> slide = ppt.getSlides();
	          	         BufferedImage img = null;
	          	         List<Graphics2D> graphics =  new ArrayList<Graphics2D>();
	          	         int size = slide.size();
	          	         slideCount = size;
	          	         for (int i = 0; i < slide.size(); i++) {
	          	        	// System.out.println("width" + pgsize.width);
	          	        	 //System.out.println("height" + pgsize.height);
	          	            img = new BufferedImage(pgsize.width, pgsize.height,BufferedImage.SCALE_SMOOTH);
	          	            graphics.add(img.createGraphics());

	          	            //clear the drawing area
	          	            graphics.get(i).setPaint(Color.white);
	          	            graphics.get(i).fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

	          	            	
	          	            slide.get(i).draw(graphics.get(i));
	          	            if (!preview.exists())preview.mkdir();
	          	           FileOutputStream out = new FileOutputStream(preview.toString()+ "\\" + Integer.toString(i) + ".png");
	          	            javax.imageio.ImageIO.write(img, "png", out); 
	          	            ppt.write(out);
	          	            out.close();
	          	            updateProgress(i + 1, size); // this update the statusbar
	          	            
	          
	          	         }
	          	        
	          	         ppt.close();
	          	      //  updateArrays();
	          	         setLastModified();
	          	         
	                     
	                       return true;
	                   }
	               };
	    }
	   
	   	public void setNoteArray(){
	   		initSlideShow();
	   		String[] str = new String[slideShow.getSlides().size()];
	   		for( int i = 0; i < str.length; i ++ ) str[i] = (readText(i));
	   		notes = str;
	   	}
	   	public String readText(int index){
	   		initSlideShow();
	    	String str = "";
	    	XSLFNotes note = slideShow.getSlides().get(index).getNotes();
	    /*	if (note == null){
	    		note = slideShow.getNotesSlide(slideShow.getSlides().get(index));
	    	}*/
	    	if (note != null){
	    		str = readText(index, note);
	    	}
	   		return str;
	   	}
	   private String readText(int index, XSLFNotes note){
		   
		   String str = null;
	    	test:
	    	for (XSLFShape shape : note) {
	    		
	            if (shape instanceof XSLFTextShape) {
	            	/*if(wasNotEmpty){
	            		wasNotEmpty = false;
	            		continue;
	            	}*/
	            	
	                XSLFTextShape txShape = (XSLFTextShape) shape;
	                if (txShape.getTextType() !=  Placeholder.BODY){
	    				continue;
	    				
	    			}
	                str = new String (txShape.getText());
	               //if(str.equals(Integer.toString(i + 1))) continue;
	               
	                
	               break test;
	               
	            }
	    	
	            
	        }
	    	 
	    	return str;
	    	
	    } 
		
	   public void writeText(String str ,int index){
		   initSlideShow();
			XSLFNotes note = slideShow.getNotesSlide(slideShow.getSlides().get(index));
			boolean wasNull = false;
			/*if (note == null){ 
				wasNull = true;
				note = new XSLFNotes();
						
			}*/
			for (XSLFTextShape shape : note.getPlaceholders()){
				if (shape.getTextType() ==  Placeholder.BODY){
					shape.setText(str);
					break;
				}
			}
		}
	   
	   
	   
	   public void saveChanges(){
       	FileOutputStream out = null;
			try {
				out = new FileOutputStream(getPresentationFile());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       	try {
				slideShow.write(out);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       	try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       	
       }
	   
	   public void initSlideShow(){
		   if (slideShow == null){
			   try {
					slideShow = new XMLSlideShow(new FileInputStream(getPresentationFile()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		   }
			   }
	

}