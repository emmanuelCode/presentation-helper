package com.pitonneux.presentation_helper;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.wearable.view.GridViewPager;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;


import com.pitonneux.presentation_helper.BluetoothHandler.BluetoothThreadClasses.BluetoothClientConnect;

import com.pitonneux.presentation_helper.BluetoothHandler.BluetoothThreadClasses.BluetoothDataShare;
import com.pitonneux.presentation_helper.GridViewPager.SimpleGridPagerAdapter;

import static com.pitonneux.presentation_helper.BluetoothHandler.BluetoothThreadClasses.BluetoothClientConnect.intToByteArray;


public class ProjectActivity extends FragmentActivity implements GridViewPager.OnPageChangeListener {


//THOSE VARIABLE WERE TEST
    //private TextView mTextView;

    //public static Handler mHandler;
    //private String readMessage;

    //this variable keep count of the column in grid view pager
    //private int oldPosition = 0;
    private int column;
    private int columnSize;
    private GridViewPager gridViewPager;
    private boolean onFling = false;

   /* private DismissOverlayView mDismissOverlay;
    private GestureDetector mDetector;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //getWindow().addFlags(Window.FEATURE_SWIPE_TO_DISMISS);//freeze my app?





        /*mHandler = new Handler () {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {


                    case BluetoothDataShare.MESSAGE_READ:


                        //Toast.makeText(context,"we passed the first part",Toast.LENGTH_SHORT).show();
                        Log.d("Handler:","we passed the first part");
                        byte[] readBuf = (byte[]) msg.obj;
                        // construct a string from the valid bytes in the buffer
                        readMessage = new String(readBuf, 0, msg.arg1);




                       // mTextView.setText(readMessage);
                        break;


                }
            }
        };*/

        //Toast.makeText(this,readMessage,Toast.LENGTH_SHORT).show();

         //final GridViewPager mViewPager;



       // final String data[]={"row","column","row","row","on the boat","..."};

       // final String data[]=liteProject.getNotes();

        gridViewPager=(GridViewPager)findViewById(R.id.pager);


        // a listener to detect changing rows column of the gridviewpager
        // the method for this is in the bottom
        gridViewPager.setOnPageChangeListener(this);

        //gridViewPager.setOnClickListener(this);



       // mViewPager.setAdapter(new SimpleGridPagerAdapter(this,data,getFragmentManager()));


        loadProject(gridViewPager);

       /* // Obtain the DismissOverlayView element
        mDismissOverlay = (DismissOverlayView) findViewById(R.id.dismiss_overlay);
        mDismissOverlay.setIntroText("EXIT");
        mDismissOverlay.showIntroIfNecessary();

        // Configure a gesture detector
        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            public void onLongPress(MotionEvent ev) {
                logOnLongPress();
            }
        });*/

        }


    public void loadProject(GridViewPager gridViewPager){

        LiteProject liteProject = null;
        liteProject = BluetoothDataShare.getLiteProject();

        if(liteProject==null){

            Toast.makeText(this,"send your project note please!",Toast.LENGTH_SHORT).show();

        }else{

            gridViewPager.setAdapter(new SimpleGridPagerAdapter(this,liteProject.getNotes(),getFragmentManager()));
            columnSize = liteProject.getNotes().length;
        }


    }




    //TODO: tried to set anClickListener
  /*  @Override
    public void onClick(View v) {

        BluetoothClientConnect.bluetoothDataShare.write(intToByteArray(3));
        
    }*/


    int count =0;
    ///control gesture commands
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode){
            case KeyEvent.KEYCODE_NAVIGATE_NEXT:
                if(column < columnSize) {
                    onFling = true;

                   // Toast.makeText(this, "next page", Toast.LENGTH_SHORT).show();
                    //to understand better
                    column++;

                    gridViewPager.setCurrentItem(0, this.column);
                    BluetoothClientConnect.bluetoothDataShare.write(intToByteArray(this.column));
                    onFling = false;
                }else {
                    count++;
                    Toast.makeText(this, "fling outward to exit", Toast.LENGTH_SHORT).show();
                    if(count>1){
                        BluetoothClientConnect.bluetoothDataShare.write(intToByteArray(-1));
                        android.os.Process.killProcess(android.os.Process.myPid());//kill app process
                        //finishAffinity();//finish the app

                        count=0;
                    }



                }
                return true;
            case KeyEvent.KEYCODE_NAVIGATE_PREVIOUS:
                if(column > 0) {
                    onFling = true;
                    column--;
                    gridViewPager.setCurrentItem(0, this.column);
                    //Toast.makeText(this, "previous page", Toast.LENGTH_SHORT).show();
                    BluetoothClientConnect.bluetoothDataShare.write(intToByteArray(this.column));
                    onFling = false;
                }else{Toast.makeText(this, "beginning of slides", Toast.LENGTH_SHORT).show();}
                return true;


        }

        return  super.onKeyDown(keyCode, event);
    }




  /*  // Capture long presses
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mDetector.onTouchEvent(ev) || super.onTouchEvent(ev);
    }*/










    ///I DID THIS BECAUSE I COULDNT ACCESS THE VARIABLE ON TOP(COLUMN) FOR THE GESTURES
    @Override
    public void onPageScrolled(int i, int i1, float v, float v1, int i2, int i3) {

    }

    @Override
    public void onPageSelected(int row , int column) {



//for gestures
        this.column=column;
        if(!onFling){BluetoothClientConnect.bluetoothDataShare.write(intToByteArray(column));}




    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }



    public void logOnLongPress(){
        Toast.makeText(this,"long press made",Toast.LENGTH_SHORT).show();

    }




}








