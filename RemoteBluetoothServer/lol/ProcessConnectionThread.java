package bluetooth;

import java.awt.Robot;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.StreamConnection;

import UI.IndexController;
import UI.Main;

public class ProcessConnectionThread implements Runnable{

	private StreamConnection mConnection;

	// Constant that indicate command from devices
	private static final int EXIT_CMD = -1;
	//Constant to determine an animation change
	private static final int ANIMATION_COMMAND = -3;
	
	public static OutputStream outputStream;

	public ProcessConnectionThread(StreamConnection connection)
	{
		mConnection = connection;
	}
	

	@Override
	public void run() {
		try {

			// prepare to receive data from the watch
			InputStream inputStream = mConnection.openInputStream();
			outputStream = mConnection.openOutputStream();
			
			 
			System.out.println("waiting for input");
			

	        while (true) {
	        		
	        		DataInputStream din = new DataInputStream(inputStream); //Inputstream from the bluetooth socket
	        		//Read Integers. Their value is the index of the column of the gridviewPager
	        	int command = din.readInt();
	        	
	        	// if the inputstream return -1 then the connection is terminated
	        	if (command == EXIT_CMD)
	        	{
	        		//close the powerpoint presentation
	        		if (Main.test != null ) Main.test.close(); //Instance of the PPT class (Class that foward the commands to the Powerpoint application)
	        		System.out.println("finish process");
	        		break;
	        	}

	        	processCommand(command);
        	}
        } catch (Exception e) {
    		e.printStackTrace();
    	}
	}

	/**
	 * Process the command from client
	 * @param command the command code
	 */
	private void processCommand(int command) {
		System.out.println(command);
		try {
			switch (command) {
	    	case -2: //for commands will be eventually built
	    		break;
	    	case -3: 
	    		break;
	    		default: //if not negative number, then its an index
	    			Main.test.gotoSlide(command); //Instance of PPT Class
	    			break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}