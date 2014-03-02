package org.hmoe.hm_music;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class LocalService extends Service { 

    private static final String TAG = "LocalService"; 

    @Override 
    public IBinder onBind(Intent intent) { 
            Log.i(TAG, "onBind"); 
            return null; 
    } 

    @Override 
    public void onCreate() { 
            Log.i(TAG, "onCreate"); 
            super.onCreate(); 
    } 

    @Override 
    public void onDestroy() { 
            Log.i(TAG, "onDestroy"); 
            super.onDestroy(); 
    } 

    @Override 
    public void onStart(Intent intent, int startId) { 
            Log.i(TAG, "onStart"); 
            super.onStart(intent, startId); 
    } 
}