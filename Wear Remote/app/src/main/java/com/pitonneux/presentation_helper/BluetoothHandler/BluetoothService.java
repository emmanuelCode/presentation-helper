package com.pitonneux.presentation_helper.BluetoothHandler;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by iHat3i on 1/26/2017.
 */
//TODO: tried to run a background service (JAD)
public class BluetoothService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
