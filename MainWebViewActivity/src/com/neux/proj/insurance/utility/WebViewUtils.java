package com.neux.proj.insurance.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.neux.proj.insurance.MyWebChromeClient;
import com.neux.proj.insurance.MyWebViewClient;
import com.neux.proj.insurance.MyWebChromeClient;
import com.neux.proj.insurance.MyWebViewClient;

/**
 * Created with IntelliJ IDEA.
 * User: titan
 * Date: 2013/12/30
 * Time: ¤U¤È 4:48
 * To change this template use File | Settings | File Templates.
 */
public class WebViewUtils {
    public static void setDefaultSetting(WebView webView,Context context) {
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);		// Set HTML5
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);


//        webView.getSettings().setAllowFileAccessFromFileURLs(true);
//        webView.getSettings().setAllowFileAccess(true);
//        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);

//        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);


        MyWebViewClient myWebViewClient = new MyWebViewClient(context);
        webView.setWebViewClient(myWebViewClient);

        MyWebChromeClient myWebChromeClient = new MyWebChromeClient(context,webView);
        webView.setWebChromeClient(myWebChromeClient);

        //ServiceCookieReq.web_view .addJavascriptInterface(new WebViewJSInterface(this), "DeviceInterface");  // Cloud Marked 20131108 GetLocation

        webView.getSettings().setAppCacheEnabled(true);

        String dir = context.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        webView.getSettings().setAppCachePath(dir);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setAppCacheMaxSize(1024*1024*8);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
//
//        if ( !isNetworkAvailable(context) )
//        {
            webView.getSettings().setCacheMode( WebSettings.LOAD_DEFAULT );
//        }
    }

    private static boolean isNetworkAvailable(Context context)
    {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
