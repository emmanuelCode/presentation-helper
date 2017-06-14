package test;




import java.util.Arrays;

import org.apache.poi.hssf.util.HSSFColor.GOLD;

import javafx.animation.Animation;
import office.MsoTriState;
import ppt.AnimationBehavior;
import ppt.AnimationBehaviors;
import ppt.AnimationSettings;
import ppt.EApplication;
import ppt.Effect;
import ppt.SlideRange;
import ppt.SlideShowView;
import ppt.Slides;
import ppt._Application;
import ppt._Presentation;
import ppt._Slide;

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
	EApplication appEvents;
	int slideCount;
	int slideIndex;
	int animationCount;
	int animationIndex;
	boolean readingView; //not used... Maybe I will
	Project project; //the actual Project that will be presented
	
	public PPT(Project getProject){
		project = getProject;
	}
	public void run(){
		//Start a PowerPoint instance
	app = ppt.ClassFactory.createApplication();
	//Get the powerpoint file
    presentation = app.presentations().open(project.getPresentationFile().toString(),
    		MsoTriState.msoTrue, MsoTriState.msoFalse,MsoTriState.msoTrue);
    //Put the prensentation on SlideSHowMode (Full Screen)
    presentation.slideShowSettings().run();


    		
    	
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
	
}
