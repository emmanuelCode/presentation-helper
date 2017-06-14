package bluetooth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import com.google.gson.Gson;

import UI.Main;
import test.Project;

public class ProjectMethods {
	/*this is a class where you have utilities for the LiteProject class*/
	
	
	public static String[] getNames(List <Project> projects){
		String[] str = new String[projects.size()];
		for(int i = 0; i < str.length; i ++){
			str[i] = projects.get(i).getName();
		}
		return str;
	}
	
	
	public static byte[] serialize(LiteProject liteProject){
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        try(ObjectOutputStream o = new ObjectOutputStream(b)){
        o.writeObject(liteProject);
        
        }catch(IOException ex ){}
        
		return b.toByteArray();
        
    }
	public static void liteToJson(LiteProject lite){
		Gson gson = new Gson();
		
		String jsonString = gson.toJson(lite);
		System.out.println(jsonString);
	}

}
