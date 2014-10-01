package com.neux.proj.insurance.utility;

import android.webkit.WebView;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: titan
 * Date: 2013/12/30
 * Time: ¤W¤È 10:56
 * To change this template use File | Settings | File Templates.
 */
public class JSCallbackUtils {

    public static void callJsAlert(WebView webview,String message) {
        call(webview,"alert", Arrays.asList(new String[]{message}));
    }

    public static void call(WebView webview,String function,JSONObject json) {
        webview.loadUrl("javascript:" + function + "('"+json.toString()+"')");
    }

    public static void call(WebView webview,String function , List<String> paramList) {
        String paramString = "";
        if(paramList != null) {
            for(String param : paramList) {
                if(paramString.equals("")) paramString = "'"+param+"'";
                else paramString = ",'"+param+"'";
            }
        }

        webview.loadUrl("javascript:" + function + "("+paramString+")");
    }
}
