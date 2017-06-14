package test;



//presentation.slideShowSettings().showWithAnimation(MsoTriState.msoTrue);
import office.MsoTriState;
import ppt.AnimationBehavior;
import ppt.AnimationBehaviors;
import ppt.EApplication;
import ppt.SlideShowView;
import ppt.Slides;
import ppt._Application;
import ppt._Presentation;
import ppt._Slide;

public class PPTOriginal extends Thread {
	final static String dir = System.getProperty("user.dir");
	_Application app;
	_Presentation presentation;
	Slides slides;
	_Slide slide;
	AnimationBehaviors effects;
	AnimationBehavior effect ;
	SlideShowView testShowView;
	

	EApplication appEvents;
	int slideCount;
	int slideIndex;
	boolean readingView;
	Project project;
	public PPTOriginal(Project getProject){
		project = getProject;
	}
	public void run(){
	app = ppt.ClassFactory.createApplication();
    presentation = app.presentations().open(project.getPresentationFile().toString(),
    		MsoTriState.msoTrue, MsoTriState.msoFalse,MsoTriState.msoTrue);
    
   




     slides = presentation.slides();
     slideCount = slides.count();
     slide = slides.item(app.activeWindow().selection().slideRange().slideNumber());
     slideIndex = slide.slideIndex();


	}
	public void nextEffect(){
		try{
			
			
		}catch(com4j.ComException ex){
			
		}
		
	}
	
	public void prevEffect(){
		
	}



	public boolean nextSlide(){
		boolean indexIsValid = true;
		if(slideCount >= slideIndex + 1){
			slideIndex++;
			try{
		    slides.item(slideIndex).select();
		    slide = slides.item(slideIndex);
			}catch(com4j.ComException ex){
				app.slideShowWindows(1).view().next();
				slide = app.slideShowWindows(1).view().slide();

			}
		}else indexIsValid = false;
		return indexIsValid;
	}


	public boolean prevSlide(){
		boolean indexIsValid = true;
		if(slideIndex - 1 >= 1 ){
			slideIndex--;
			try{
		 slides.item(slideIndex).select();
		    slide = slides.item(slideIndex);
			}catch(com4j.ComException ex){
				app.slideShowWindows(1).view().previous();
				slide = app.slideShowWindows(1).view().slide();
			}

		}else indexIsValid = false;
		    return indexIsValid;

	}
	public boolean slideSelect(int i){
		boolean indexIsValid = true;
		if(slideCount >=  i){
		try{
		slides.item(i).select();
		slideIndex =  i;
		} catch(com4j.ComException ex){
			app.slideShowWindows(1).view().gotoSlide(i, MsoTriState.msoFalse);
			slide = app.slideShowWindows(1).view().slide();
		}
		}else indexIsValid = false;

		return indexIsValid;

	}
}
