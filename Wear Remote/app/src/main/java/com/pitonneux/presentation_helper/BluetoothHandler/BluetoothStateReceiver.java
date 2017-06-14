package com.pitonneux.presentation_helper.BluetoothHandler;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.pitonneux.presentation_helper.BluetoothHandler.BluetoothThreadClasses.BluetoothClientConnect;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by cacau on 1/7/2017.
 */
//broadcastReceiverClass
public class  BluetoothStateReceiver extends BroadcastReceiver {

    //prepare required variable for bluetooth
    private BluetoothDevice bluetoothDevice;
    private BluetoothClientConnect bluetoothClientConnect;
    private BluetoothAdapter bluetoothAdapter;

    // WITH THOSE EVENT I TRICK THE BLUETOOTH SETTING IN GENERAL
    // ON THING I NOTICE THE BLUETOOTH SETTING USES STATEMACHINE IN THERE METHODS
    // CANT GET ANYTHING ON THE BLUETOOTH SETTING ACTIVITY NO GETTERS RETURN VALUE ETC..
    // BUT I CAN GET THE REQUEST CODE AND CLOSE IT WITH IT (ONLY THE FIRST ACTIVITY)
    @Override
    public void onReceive(Context context, Intent intent) {
        ///get the action ....?
        String action =intent.getAction();
        //get the bluetooth defaultAdapter
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

        ///listening if the intent filter send matches the broadcastAction (in form of a String Constant)
        if(action.equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)) {

            //then we store a value, for checking a particular int Constants for  listening its activities  /*need to explain better*/
            int state=intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE,BluetoothAdapter.ERROR);

            // test the variable condition with other constants
            switch (state) {

               //-was a test - BluetoothAdapter.ACTION_DISCOVERY_STARTED- don t mind this line-

                //if case were connecting
               case BluetoothAdapter.STATE_CONNECTING :
                    //get the bluetooth device were on to
                   bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                   //get the bluetooth name and  make a Toast
                  // Toast.makeText(context, "Connecting to: "+bluetoothDevice.getName(),Toast.LENGTH_SHORT).show();

                    //set the bluetooth adapter to get the remote MAC address required for connecting
                   bluetoothAdapter.getRemoteDevice(bluetoothDevice.getAddress());//got doubt about this line...?

                    //initialize a bluetooth client connection to the server
                   bluetoothClientConnect= new BluetoothClientConnect(bluetoothDevice,bluetoothAdapter);
                   //TODO: difference between Thread.run and Thread.start
                   //run the Thread to stay connected
                   bluetoothClientConnect.run();//TREAD RUN SHOW MY TOAST  THAN THREAD START.///???///

                   //if connect success show a notification/Toast
                  if(bluetoothClientConnect.gotConnected){

                      Log.d(TAG, "onReceive: it works! opening activity  ");
                      Toast.makeText(context,bluetoothDevice.getName()+":Connected!",Toast.LENGTH_SHORT).show();

                      //TODO: here I tried to close the bluetooth setting activity automatically once connected
                        /*THOSE ARE TEST */
                         //Activity task =(Activity)context;
                         //task.setResult(3);

                         //task.finishActivity(1);


                      /*Intent openProjectActivity=new Intent(context, ProjectActivity.class);
                      context.startActivity(openProjectActivity);*/

                  }


                   break;

               /*THOSE ARE TEST */
               /* try {
                    bluetoothSocket.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                    break;*/

                /*case BluetoothDevice.ACTION_ACL_CONNECTED:
                    bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Toast.makeText(context, "Connected to " + bluetoothDevice.getName(), Toast.LENGTH_SHORT).show();
                    break;

                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Toast.makeText(context, "Disconnected from " + bluetoothDevice.getName(),
                            Toast.LENGTH_SHORT).show();
                    break;*/
            }

        }


    }
}
