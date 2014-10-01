package com.neux.proj.insurance;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.neux.proj.insurance.utility.DialogUtils;
import com.neux.proj.insurance.utility.HttpUtils;

import java.net.URLDecoder;

/**
 * Created with IntelliJ IDEA.
 * User: titan
 * Date: 2014/7/4
 * Time: ¤U¤È 3:31
 * To change this template use File | Settings | File Templates.
 */
public class GCMIntentService extends GCMBaseIntentService {
//    private static final String SENDER_ID = "721456884031";

    public GCMIntentService() {

        super(MainActivity.getGCMNUMBER());
//        super("511006677612");
//        TODO Auto-generated constructor stub
//        super(SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String regId) {
        // TODO Auto-generated method stub
        try {
//            Toast.makeText(context, "onRegistered regId = " + regId, Toast.LENGTH_LONG);
//            DialogUtils.openDialog(context,"GCMIntentService regId = " + regId);
            HttpUtils.getHtml(context.getString(R.string.GetUrl) + "/gcm/register.jsp?" + regId);
            GCMRegistrar.setRegisteredOnServer(context, true);
        } catch (Exception e) {
            Log.e("DEBUG", e.getMessage());
//            DialogUtils.openDialog(context,e.getMessage());
        }
    }

    @Override
    protected void onUnregistered(Context context, String regId) {
//        Toast.makeText(context, "onUnregistered regId = " + regId, Toast.LENGTH_LONG);

        // TODO Auto-generated method stub
        if (GCMRegistrar.isRegisteredOnServer(context)) {
            try {
                HttpUtils.getHtml(context.getString(R.string.GetUrl) + "/gcm/unregister.jsp?" + regId);
                GCMRegistrar.setRegisteredOnServer(context, false);
            } catch (Exception e) {
                Log.e("DEBUG",e.getMessage());
//                DialogUtils.openDialog(context,e.getMessage());
            }
        }
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        String url = intent.getStringExtra("url");
//        String id = intent.getStringExtra("id");
//        String log_id = intent.getStringExtra("log_id");

        // title = new String(Base64.decode(title, Base64.DEFAULT));
        try {
            title = URLDecoder.decode(title, "utf-8");
            message = URLDecoder.decode(message, "utf-8");
            url = URLDecoder.decode(url, "utf-8");
            // title = new String(title.getBytes("utf-8") , "iso-8859-1");
        } catch (Exception e) {

        }

//        try{
//            HttpUtils.getHtml(getResources().getString(R.string.GetUrl) + "/android.jsp?type=" + type + "&id=" + id + "&logId=" + log_id);
//        }catch(Exception e) {
//            ;
//        }
//
//        try{
//            HttpUtils.getHtml(getResources().getString(R.string.GetUrl) + "/gcm/gcmHitLog.jsp?logId=" + log_id + "&userId=&regId=");
//        }catch(Exception e) {
//            ;
//        }

        int icon = R.drawable.missno3;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
//        bundle.putString("id", id);
//        bundle.putString("log_id", log_id);
        notificationIntent.putExtras(bundle);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        Notification notification = new Notification(icon, message, System.currentTimeMillis());
        notification.tickerText = title;
        // notification.defaults = Notification.DEFAULT_ALL;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.setLatestEventInfo(context, title, message, pendingIntent);
        notificationManager.notify(0, notification);
        // sendGCMIntent(context , "12345");
    }

    @Override
    protected void onError(Context context, String errorId) {
        // TODO Auto-generated method stub
        DialogUtils.openDialog(context, "GCMIntentService errorId = " + errorId);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // TODO Auto-generated method stub
        return super.onRecoverableError(context, errorId);
    }


}
