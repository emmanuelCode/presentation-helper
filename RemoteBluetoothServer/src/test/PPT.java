package test;






import javafx.scene.input.KeyEvent;

import java.io.File;
import java.util.Arrays;
import java.util.Random;

import org.apache.poi.hssf.util.HSSFColor.GOLD;

import com4j.Com4jObject;
import com4j.ComThread;
import com4j.EventCookie;
import com4j.Holder;
import javafx.animation.Animation;
import office.MsoShapeType;
import office.MsoSyncEventType;
import office.MsoTriState;
import ppt.AnimationBehavior;
import ppt.AnimationBehaviors;
import ppt.AnimationSettings;
import ppt.DocumentWindow;
import ppt.EApplication;
import ppt.Effect;
import ppt.PpPlaceholderType;
import ppt.PpProtectedViewCloseReason;
import ppt.ProtectedViewWindow;
import ppt.Selection;
import ppt.SlideRange;
import ppt.SlideShowView;
import ppt.SlideShowWindow;
import ppt.Slides;
import ppt._Application;
import ppt._Presentation;
import ppt._Slide;
import vba.Events;
import vba._CommandBarControlEvents;
import vba._ReferencesEvents;
import vba._VBProject;

public class PPT extends Thread  {
	
	
	
	
	
	final static String dir = IO.userDir.toString();
	//The PowerPoint application Instance
	_Application app;
	// Your project
	_Presentation presentation;
	//List that have all the slides
	Slides slides;
	//Sicngle Slide
	_Slide slide;
	//for testing purpose
	AnimationBehaviors effects;
	AnimationBehavior effect ;
	SlideShowView testShowView;
	//for testing purpose
	
	// For handling events buts its not working
	Events appEvents;
	int slideCount;
	int slideIndex;
	int animationCount;
	int animationIndex;
	boolean readingView; //not used... Maybe I will
	EApplication test;
	Project project; //the actual Project that will be presented
	MsoTriState isVisible;
	
	
	public PPT(Project getProject, MsoTriState isVisible){
		project = getProject;
		this.isVisible = isVisible;
	}
	
	public void run(){
		
		//Start a PowerPoint instance
	app = ppt.ClassFactory.createApplication();
	//Get the powerpoint file
    presentation = app.presentations().open(project.getPresentationFile().toString(),
    		MsoTriState.msoFalse, MsoTriState.msoFalse,isVisible);
    //Put the prensentation on SlideSHowMode (Full Screen)
    if(isVisible == MsoTriState.msoTrue)presentation.slideShowSettings().run();


    		
    	
     slides = presentation.slides(); //get the slides
     slideCount = slides.count(); // get the slidecount
     //slide = slides.item(app.activeWindow().selection().slideRange().slideNumber()); //Having somes exceptions!?
    // slideIndex = slide.slideIndex();
     

	}
	public void close(){	
		//close powerpoint
		presentation.close();
		app.quit();
	}

	int i = 0;
	// this is for controlling animation, not fully fonctionnal yet
	public void nextEffect(){
		//getClickCount(); 
		//i ++;
		app.slideShowWindows(1).view().gotoClick(i);
		
				/*try{
				    slides.item(slideIndex).select();

					}catch(com4j.ComException ex){
						app.slideShowWindows(1).view().next();
						slide = app.slideShowWindows(1).view().slide();

					}*/
				
			
				//if(slide.slideIndex() != slideIndex){
					//slideIndex ++;
					//send int ++
				}
			

		

	
	public void prevEffect(){	
		
		try{
		    slides.item(slideIndex).select();

			}catch(com4j.ComException ex){
				app.slideShowWindows(1).view().previous();
				slide = app.slideShowWindows(1).view().slide();

			}
		
	
		if(slide.slideIndex() != slideIndex){
			slideIndex --;
			//send int -- 
		}
	
	}

	/**
	 * 
	 * @param index
	 * @return if the index is valid
	 * 
	 * Change the slides index
	 */
	public boolean gotoSlide(int index){
		
		boolean indexIsValid = true;
		if(slideCount > index && index >= 0 ){
			slideIndex = index + 1;
			try{
				app.slideShowWindows(1).view().gotoSlide(slideIndex, MsoTriState.msoTrue);
				//slide = app.slideShowWindows(1).view().slide();

		    
		    
			}catch(com4j.ComException ex){
				slides.item(slideIndex).select();
			    slide = slides.item(slideIndex);
			}




		}else indexIsValid = false;
		return indexIsValid;
	}

// not used anymore
	public boolean prevSlide(){
		boolean indexIsValid = true;
		if(slideIndex - 1 >= 1 ){
			slideIndex--;
			try{
			    slides.item(slideIndex).select();
			    slide = slides.item(slideIndex);
				}catch(com4j.ComException ex){
					//app.slideShowWindows(1).view().next();
					app.slideShowWindows(1).view().gotoSlide(slideIndex, MsoTriState.msoFalse);
					slide = app.slideShowWindows(1).view().slide();

				}

		}else indexIsValid = false;
		    return indexIsValid;

	}
	public int  getClickCount(){return app.slideShowWindows(1).view().getClickCount();}//Animation count
	public int getClickIndex(){return app.slideShowWindows(1).view().getClickIndex();} //Animation index,  throws an exception
	
	 public void write(String str, int i){
	    	getShape(i)
	    	.textFrame().textRange().text(str);
	    }
	    
	    public String read(int index){
	    	return
	    		getShape(index)
	    	.textFrame().textRange().text();
	    }
	    public String[] getNotes(){
	    	String[] str = new String[slideCount]; 
	    	System.out.println(slideCount);
	    	
	    	for (int i = 0  ;i< slideCount ; i++){
	    		str[i] = read(i);
	    	}
	    	return str;
	    }
	    public void saveChanges(){
	    	//if(presentation.saved() == MsoTriState.msoFalse)
	    	 presentation.save();
	    	 
	    }
	    
	    private ppt.Shape getShape(int index){
	    	ppt.Shape shape = null;
	    	ppt.Shapes shapes = 
	    	slides.item( index + 1).notesPage().shapes();
	    	
	    	for(int i  = 1; i < shapes.count() ; i ++){
	    		
	    		if(shapes.item(i).type() == MsoShapeType.msoPlaceholder){
	    			if(shapes.item(i).placeholderFormat().type()
	    					== PpPlaceholderType.ppPlaceholderBody){
	    				shape = shapes.item(i);
	    				break;
	    			}
	    		}
	    	}
	    	
	    	return shape;
	    }

	
}
