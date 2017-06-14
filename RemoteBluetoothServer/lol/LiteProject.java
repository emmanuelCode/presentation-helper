package bluetooth;

import java.io.Serializable;

import test.Project;

//Class that take a  project instance to have just the important information
//this object will be sent to the watch
public class LiteProject {
	private int slideCount;
	private String[] notes;
	private int[] animationsCount;
	private String name;
	public LiteProject(Project project){
		slideCount =  project.getSlideCount();
		notes = project.getNotes();
		name = project.getName();
		animationsCount = project.getAnimationCount();


	}
	public String getName(){return name;}
	public int getSlideCount(){return slideCount;}
	public String[] getNotes(){return notes;}
	















}
