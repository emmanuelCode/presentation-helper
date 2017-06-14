package com.pitonneux.presentation_helper.BluetoothHandler.BluetoothThreadClasses;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.math.BigInteger;
import java.util.UUID;

/**
 * Created by cacau on 12/9/2016.
 */
//TODO: need to better understand this class
//WILL NEED TO UNDERSTAND THIS CODE FROM THE ANDROID DEV SITE
public class BluetoothClientConnect extends Thread {
    private final BluetoothSocket bluetoothSocket;
    private final BluetoothDevice bluetoothDevice;
    private final BluetoothAdapter bluetoothAdapter;
    public static BluetoothDataShare bluetoothDataShare;
    public static boolean gotConnected;

                       /*
                       *
                       * @param remote bluetooth device
                       * @param current bluetooth adapter
                        */
   /* public void tempExample(){

        for(BluetoothDevice device :  new BluetoothManager().getConnectedDevices(0)){
            BluetoothClientConnect(device)

        }
    }*/
    public BluetoothClientConnect(BluetoothDevice device, BluetoothAdapter adapter) {
        // Use a temporary object that is later assigned to bluetoothSocket,
        // because bluetoothSocket is final
        BluetoothSocket tmp = null;
        this.bluetoothDevice = device;
        this.bluetoothAdapter=adapter;



        // Get a BluetoothSocket to connect with the given BluetoothDevice

        try {
            // MY_UUID is the app's UUID string, also used by the server code
            String s = "04c6093b00001000800000805f9b34fb";
            String s2 = s.replace("-", "");
            UUID uuid = new UUID(
                    new BigInteger(s2.substring(0, 16), 16).longValue(),
                    new BigInteger(s2.substring(16), 16).longValue());





            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) { gotConnected = false;}
        bluetoothSocket = tmp;
    }

    public void run() {
        // Cancel discovery because it will slow down the connection
        bluetoothAdapter.cancelDiscovery();

        try {
            // Connect the bluetoothDevice through the bluetoothSocket. This will block
            // until it succeeds or throws an exception

            bluetoothSocket.connect();

            gotConnected=true;

        } catch (IOException connectException) {
            // Unable to connect; close the bluetoothSocket and get out
            try {
                bluetoothSocket.close();
            } catch (IOException closeException) { }
            return;
        }

        // Do work to manage the connection (in a separate thread)

       // manageConnectedSocket(bluetoothSocket);


        bluetoothDataShare=new BluetoothDataShare(bluetoothSocket);
        bluetoothDataShare.start();



    }

    /** Will cancel an in-progress connection, and close the bluetoothSocket */
    public void cancel() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) { }
    }



    //I don't think it the right place to post this method......?
    // turn int value to bytes so we can send commands
    public static final byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }
}