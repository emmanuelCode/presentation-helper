package test;

import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.poi.util.TempFile;
import org.omg.CORBA.REBIND;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

public class IO {
	//directory of where the jar file would be
	public static final File userDir = new File(System.getProperty("user.dir"));
	//where all the serialized Project Object are stored with the powerpoint file and previews images
	public static final File dataDir = new File(userDir +  "\\data");
	//Directory used for file testing or file modification
	public static final File tempDir = new File(userDir + "\\Temp");
	// serialization of one Object. Return true if done succesfully 
	public static boolean serialize(Project project, String where){
		boolean gotSerialized = true;
		project.updateDirectories(where);
		try (ObjectOutputStream out =
				new ObjectOutputStream(new FileOutputStream(project.getDataFile()))){
			out.writeObject(project);


		}catch(IOException ex){
			System.out.println("Serialization failed!");
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			gotSerialized = false;
		}
		return gotSerialized;
	}

	//return all the subDirectory of its parent. Files are not returned Only directories
	public static File[] getDirectories(File file){
		File[] directories = new File(file.toString()).listFiles(new FileFilter() {
		    @Override
		    public boolean accept(File file) {
		        return file.isDirectory();
		    }
		});
		return directories;
	}
	public static File[] getFiles(File file){
		File[] directories = file.listFiles();
		return directories;
	}
	/* import a zipped project file 
	test it on the Temp directory. 
	If its a valid Object its added into the data directory*/
	public static File importProject(ZipFile file) throws ZipException, FileNotFoundException, IOException, ClassNotFoundException{
		ZipFile importFile = file;
		File fileDest = new File(userDir + "/Temp");
		try {
			importFile.extractAll(fileDest.toString());
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File tempFile = fileDest.listFiles()[0];
		print(tempFile.toString());
		File dataFile = new File(tempFile +  "/data.dat");
		//dataFile = new File(fileDest+ "/"+ projectFile.getName()+ "/data.dat");
		print("DataFile "+dataFile.toString());
		Project project = IO.deserialize(dataFile);
		File renamedFile = null;
		if(project != null){
			 renamedFile = fileEnumeration(tempFile.toString(), dataDir.toString() );
			renameProject(project, renamedFile.getName(),tempDir.getName());
			Files.move(renamedFile.toPath(), new File (dataDir + "/" + renamedFile.getName()).toPath());
			
			
		}
		
		
		return renamedFile;
	}
	// Rename a project directory with its powerpoint file renamed also
	
	public static void renameProject(Project project, String reName, String where){
		File projectFile = project.getProjectFile();
		File presFile = project.getPresentationFile();
		String name = project.getName();
		String presName = presFile.getName();
		int index = presName.indexOf(".");
		String extension = presName.substring(index);
		
		print ("getDataFile" + project.getDataFile().toString());
		
		
		presFile.renameTo(new File(projectFile + "/" + reName + extension));
		projectFile.renameTo(new File(dataDir + "/" + reName));
		project.setName(reName);
		project.updateDirectories(dataDir.getName());
		serialize(project, dataDir.getName());
	}
	
	//got the the list of all subdirs of the data dir and deserialize 
	//each data.dat one by one and add it to the ArrayList that wil be returned 
	public static ArrayList<Project> deserialize() throws ClassNotFoundException, IOException{





		ArrayList<Project> projects = new ArrayList<Project>();
		
		File[] directories = getDirectories(dataDir);
		if(directories != null)
		for (File data : directories){
			String dir = data.getCanonicalPath() + "\\" + "data.dat";
		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(dir))){
			projects.add ((Project) in.readObject());
			projects.get(projects.size() - 1 ).updateDirectories(dataDir.getName());
		}catch(IOException ex){
			ex.printStackTrace();
			System.out.println("Deserialization failed!");
			break;
			}
		}else projects = null;



		return projects;
	}
	//deserialize a single file (file must equals data.dat)
	public static Project deserialize(File file) throws ClassNotFoundException{
		Project project = null;
		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))){
			project = (Project) in.readObject();
			
		}catch(IOException ex){
			ex.printStackTrace();
			System.out.println("Deserialization failed!");

		}
		File destFile = new File (new File(file.getParent()).getParent());
		
		
		project.updateDirectories(new File (new File(file.getParent()).getParent()).getName());
		return project;
	}
	//take a directory from in and if there a directory from out that have the same name
	//the name of the dir in will be numerated like that test test(1) in order to not overwrite
	//the existing file
	public static File fileEnumeration(String in, String out){
		File fileTemp = new File(in);
	
		String fullName = fileTemp.getName();
		fileTemp = null;
		System.out.println(fullName);
		int extensionIndex = fullName.indexOf(".");
		String name = null;
		String extension = null;
		if (extensionIndex != -1){
			name = fullName.substring(0,extensionIndex);
			extension= fullName.substring(extensionIndex,fullName.length());
		}else{
			name = fullName;
			extension = "";
		}
		String path = out;
		if(!out.substring(out.length()-1,out.length()).contains("\\")) path += "\\";
		int count = 1;
		String finalPath = path + name;
		while(new File(finalPath + extension).exists()){
			finalPath = path + name + "(" + Integer.toString(count) + ")";
			count ++;
			
		}
		return new File(finalPath + extension);
	}

	public static String zipIt(String in, String out){
		int count = 1;
		String str = out  + ".zip";
		try {

			// Initiate ZipFile object with the path/name of the zip file.

			 //in + "(" + Integer.toString(1) + ")";
			if (new File(out + ".zip").exists()){
				str = out + "(" + Integer.toString(1) + ").zip";
			while(new File(str).exists()){
				count ++;
				str = out + "(" + Integer.toString(count) + ").zip";
					}
				}

			ZipFile zipFile = new ZipFile(str);

ZipParameters parameters = new ZipParameters();

			// set compression method to store compression
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

			// Set the compression level. This value has to be in between 0 to 9
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

			// Create a split file by setting splitArchive parameter to true
			// and specifying the splitLength. SplitLenth has to be greater than
			// 65536 bytes
			// Please note: If the zip file already exists, then this method throws an
			// exception


			zipFile.createZipFileFromFolder(in, parameters, true, 10485760);
		} catch (ZipException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static void print(String str){System.out.println(str);}


}
