package com.neux.proj.insurance;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.WindowManager;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import com.google.android.gcm.GCMRegistrar;
import com.neux.proj.insurance.utility.WebViewUtils;
import com.neux.proj.insurance.utility.WebViewUtils;

public class MainWebViewActivity extends Activity
{
    public static WebView m_WV;

//    private GetNowLocation GetLocation;

    private static ProgressDialog progressBar = null;

    public static void showProgressDialog() {
        if(progressBar != null && !progressBar.isShowing()) {
            progressBar.show();
        }
    }

    public static void closeProgressDialog() {
        if(progressBar != null && progressBar.isShowing()) {
            progressBar.dismiss();
//            progressBar.hide();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.e("DEBUG","MainWebViewActivity start");

////        //set full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //設定隱藏APP標題
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //lock change screen
        setRequestedOrientation(1);

//        openDialog(this,"test1");

        try {
            String GCMNUMBER = getResources().getString(R.string.gcm_project_number);
            GCMRegistrar.checkDevice(MainWebViewActivity.this);
            GCMRegistrar.checkManifest(MainWebViewActivity.this);

            String regId = GCMRegistrar.getRegistrationId(MainWebViewActivity.this);
            if("".equals(regId)) {
//                GCMRegistrar.register(MainActivity.this, "511006677612");
                GCMRegistrar.register(MainWebViewActivity.this,getResources().getString(R.string.gcm_project_number));
            }

        } catch (Exception e) {
//            DialogUtils.openDialog(this, e.getMessage() + " oh my god");
            Log.e("DEBUG",e.getMessage());
        }

        String appName = getResources().getString(R.string.app_name);
        String loading = getResources().getString(R.string.loading);
        String url = getResources().getString(R.string.GetUrl);


        progressBar = ProgressDialog.show(this, appName, loading);

//        openDialog(this,"test2");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        m_WV = (WebView)findViewById(R.id.webview);


        Log.e("DEBUG","temp_url = " + url);

        Log.e("DEBUG","MainWebViewActivity start3");


        if(getIntent().getExtras() != null) {
            String pushMessageURL = getIntent().getExtras().getString("url");

            if(pushMessageURL != null) {
                url = pushMessageURL;
            }


        }


        //InitWebVie
        WebViewUtils.setDefaultSetting(m_WV, this);
//
        m_WV.addJavascriptInterface(new WebViewJSInterface(this,null), "DeviceInterface");// Cloud Added 20131108 GetLocation

        m_WV.loadUrl(url);

//        openDialog(this,"test4");

        Log.e("DEBUG","MainWebViewActivity start4");

    }

    @Override
    protected void onNewIntent(Intent intent) {

        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        if (intent != null) {

            if (intent.getExtras() != null) {
                Bundle bundle = intent.getExtras();
                String url = bundle.getString("url");
                m_WV.loadUrl(url);
            }

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && m_WV.canGoBack()) {
            m_WV.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static String getResourceString(int id) {
        return m_WV.getResources().getString(id);
    }

    // 20130823 Added Begin ..
    private boolean isNetworkAvailable()
    {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
    // 20130823 Added

    @Override
    public void onResume()
    {
        super.onResume();

        // Cloud Added 20131108 GetLocation
//        GetLocation.Update();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        // Cloud Added 20131108 GetLocation
//        GetLocation.remove();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 999) {
            if (resultCode == RESULT_OK) {

                String id = intent.getStringExtra("id");
                String name = intent.getStringExtra("name");
                String gender = intent.getStringExtra("gender");
                String locale = intent.getStringExtra("locale");

//                WebViewJSInterface.googleLoginSuccess(id,name,gender,locale);

            }
        }

        else {

        }

    }

    private boolean checkURLMatch(String url) {
        //get webview url
        String webViewURL = getResources().getString(R.string.GetUrl);
        if(webViewURL.startsWith("http://")) {
            webViewURL = webViewURL.substring(7);
        }

        boolean isMatch = url.indexOf(webViewURL) != -1;

        if(!isMatch) {
            if(webViewURL.indexOf("/") != -1) {
                String rootURL = webViewURL.substring(0,webViewURL.indexOf("/"));
                isMatch = url.indexOf(rootURL) != -1;
            }
        }

        return isMatch;
    }

    private void openDialog(Context context,String message) {
        new AlertDialog.Builder(context).setTitle(R.string.app_name).setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();
    }

}

