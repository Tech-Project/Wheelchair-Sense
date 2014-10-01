package com.neux.proj.insurance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.VideoView;


// ************** Chrome Class Begin **************//
public class MyWebChromeClient extends WebChromeClient
{

//    private Activity mainActivity = null;
//    private WebView webView = null;
//
//    private Object mediaPlayerInstance = null;

    public MyWebChromeClient(Context activity,WebView webView) {
//        this.mainActivity = (Activity) activity;
//        this.webView = webView;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message,JsResult jsResult) {
        final JsResult finalJsResult = jsResult;

        try{
            new AlertDialog.Builder(view.getContext()).setTitle(R.string.app_name)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            finalJsResult.confirm();
                        }
                    }).setCancelable(false).create().show();
        }catch(Exception e)
        {
            Log.e("DEBUG","onJsAlert Error e = " + e.toString());
        }
        return true;

    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        // TODO Auto-generated method stub
        // return super.onJsConfirm(view, url, message, result);
        new AlertDialog.Builder(view.getContext()).setTitle(R.string.app_name).setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                result.confirm();
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                result.cancel();
            }
        }).create().show();

        return true;
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback)
    {
        callback.invoke(origin, true, false);
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    @Override
    public void onExceededDatabaseQuota(String url,String databaseIdentifier, long quota,long estimatedDatabaseSize, long totalQuota,QuotaUpdater quotaUpdater)
    {
        super.onExceededDatabaseQuota(url, databaseIdentifier, quota,estimatedDatabaseSize, totalQuota, quotaUpdater);quotaUpdater.updateQuota(estimatedDatabaseSize * 2);
    }

    // 20130823 Added Begin ..
    @Override
    public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater)
    {
        quotaUpdater.updateQuota(spaceNeeded * 2);
    }
    // 20130820 Added End..

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {

        String message = view.getClass().getName() ;

        super.onShowCustomView(view, callback);
        if (view instanceof FrameLayout) {
            FrameLayout frame = (FrameLayout) view;
            message += "," + frame.getFocusedChild().getClass().getName();
            if (frame.getFocusedChild() instanceof VideoView) {
                VideoView video = (VideoView) frame.getFocusedChild();
                frame.removeView(video);

                video.requestFocus();
                video.start();

                message += ",video is start";
            }
        }

        new AlertDialog.Builder(view.getContext()).setTitle(R.string.app_name)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                }).setCancelable(true).create().show();

    }


}
// ************* Chrome Class End **************//
