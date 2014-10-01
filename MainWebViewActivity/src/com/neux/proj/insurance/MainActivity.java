package com.neux.proj.insurance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    private static String GCMNUMBER = "";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);


//        try {
//            GCMNUMBER = getResources().getString(R.string.gcm_project_number);
//            GCMRegistrar.checkDevice(MainActivity.this);
//            GCMRegistrar.checkManifest(MainActivity.this);
//
//            String regId = GCMRegistrar.getRegistrationId(MainActivity.this);
//            if("".equals(regId)) {
////                GCMRegistrar.register(MainActivity.this, "511006677612");
//                GCMRegistrar.register(MainActivity.this,getResources().getString(R.string.gcm_project_number));
//            }
//
//        } catch (Exception e) {
////            DialogUtils.openDialog(this, e.getMessage() + " oh my god");
//            Log.e("DEBUG",e.getMessage());
//        }

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent().setClass(MainActivity.this, MainWebViewActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 1500);

    }

    public static String getGCMNUMBER() {
        return GCMNUMBER;
    }
}
