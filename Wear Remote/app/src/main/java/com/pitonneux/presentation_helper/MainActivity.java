package com.pitonneux.presentation_helper;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.pitonneux.presentation_helper.BluetoothHandler.BluetoothStateReceiver;
import com.pitonneux.presentation_helper.BluetoothHandler.BluetoothThreadClasses.BluetoothClientConnect;

public class MainActivity extends Activity implements View.OnClickListener {

    //detect if the screen is a round or square and prepare the appropriate setting.
   // private BoxInsetLayout boxInsetLayout;
    //prepare the cardFrame and make it scrollable
   // private CardScrollView cardScrollView;

    //prepare clickable imageView
    private ImageButton start_image;
    private ImageButton about_image;

    //prepare broadcastReceiver  to listen to OS activity in this case bluetooth
    private BroadcastReceiver bluetoothReceiver;

   // public static Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //set Layout file
        //keep screen on so the watch won't go to sleep
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //attach id
      //  boxInsetLayout=(BoxInsetLayout)findViewById(R.id.box_inset_Layout);//?
        //frameLayout=(FrameLayout)findViewById(R.id.frame_layout);
        //attach id
      //  cardScrollView=(CardScrollView)findViewById(R.id.card_scroll_view);


        //Attaching id
        start_image=(ImageButton) findViewById(R.id.start_image);
        about_image=(ImageButton)findViewById(R.id.about_image);

        //add listener for detecting click
        start_image.setOnClickListener(this);
        about_image.setOnClickListener(this);


        //initiliaze the bluetooth State receiver class
        bluetoothReceiver=new BluetoothStateReceiver();




       // mHandler=new Handler();
    }

    @Override
    public void onClick(View v) {
        ///get the id of the click to know witch one

        if(v.getId()==start_image.getId()) {


            //if socket not connected (open bluetooth setting with broadcastreceiver)
            if(!BluetoothClientConnect.gotConnected){

                //prepare for an intent filter to verify with the broadcast intent
                IntentFilter bluetoothFilter = new IntentFilter();


                // Intent test=new Intent();
                //a test
                //test.putExtra("test",BluetoothAdapter.STATE_CONNECTING);

                //add action to verify during broadcast (to listen event)
                bluetoothFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
                //bluetoothFilter.addAction() //could add other action...

                //register the broadcast must unregister at a certain point TO REMEMBER
                //this is were the broadcast start and pass in the filter to test
                registerReceiver(bluetoothReceiver, bluetoothFilter);

                //set the opening intent
                Intent bluetoothIntent = new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                //this open the os bluetooth setting
                //set requestCode to 1
                startActivityForResult(bluetoothIntent, 1);
        }else{
                //set projectActivity
                Intent openProjectActivity=new Intent(this, ProjectActivity.class);
                //open projectActivity
                startActivity(openProjectActivity);
            }//end of second if statement



        }else{


            Intent I= new Intent(this,AboutActivity.class);
            startActivity(I);

        }//end of first if statement

    }

    ///TEST TO WORK ON
    // when return from bluetooth setting I check the condition
    public void onActivityResult(int requestCode,int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

            //retrieve requestCode send to the bluetooth activity,check if it match
            //and test if bluetooth Socket is connect
            if(requestCode==1&& BluetoothClientConnect.gotConnected==true){
                                             /*data.getIntExtra("test",0)==BluetoothAdapter.STATE_CONNECTING*/

                Toast.makeText(this,"opening Project...",Toast.LENGTH_SHORT).show();
                Intent openProjectActivity=new Intent(this, ProjectActivity.class);
                startActivity(openProjectActivity);

                /*if(resultCode==RESULT_OK){}*///?//


        }




    }


   /* public void getPairedDevices(){
        Set<BluetoothDevice> devices= adapter.getBondedDevices();
        ArrayList<String> bluetoothName;
        for (int a=0;a<adapter.getBondedDevices().size();a++){


            bluetoothName.get(a)=devices.;
            adapter.getDefaultAdapter().getAddress();


        }

    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //TODO: error when nothing to unregister so try/catch
        try{unregisterReceiver(bluetoothReceiver);
        } catch (IllegalArgumentException error){


        }



    }


}
