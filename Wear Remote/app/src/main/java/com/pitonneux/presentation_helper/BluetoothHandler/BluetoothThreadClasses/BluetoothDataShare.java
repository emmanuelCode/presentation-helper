package com.pitonneux.presentation_helper.BluetoothHandler.BluetoothThreadClasses;

import android.bluetooth.BluetoothSocket;

import com.pitonneux.presentation_helper.LiteProject;
import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by cacau on 12/9/2016.
 */


//TODO: applying handler
public class BluetoothDataShare extends Thread {

        public static final int MESSAGE_READ = 0;
        //private Handler mHandler= new Handler;

        public static LiteProject lite;


        private final BluetoothSocket mmSocket;
        private static InputStream mmInStream; ///private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private final DataInputStream mDataInputStream;
        private final DataOutputStream mDataOutputStream;




        public BluetoothDataShare(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;


            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();


            } catch (IOException e) { }


            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            mDataInputStream = new DataInputStream(mmInStream);
            mDataOutputStream = new DataOutputStream(mmOutStream);







        }

       public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
          /* while(true){
               String str = null;
               try {
                   str = mDataInputStream.readUTF();
               }catch (IOException ex ){}
              // Intent intent = new Intent(Constants.PROJECT_STRING);


           }*/
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }

        //construct an object from the bluetooth data
        public static LiteProject getLiteProject(){

            LiteProject lite= null;
            Gson gson=new Gson();
            DataInputStream dataInputStream=new DataInputStream(mmInStream);

            try{
                String jsonString= dataInputStream.readUTF();
                lite=gson.fromJson(jsonString,LiteProject.class);

            }catch(IOException ex){

            }



            return lite ;
        }

}
