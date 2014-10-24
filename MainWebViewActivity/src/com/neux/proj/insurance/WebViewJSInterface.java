package com.neux.proj.insurance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.ntu.esos.GPS.*;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.android.gcm.GCMRegistrar;
import com.neux.proj.insurance.contacts.ShowContacts;
import com.neux.proj.insurance.dbsetget.DBGet;
import com.neux.proj.insurance.dbsetget.DBSet;
import com.neux.proj.insurance.dbsetget.dbColumnInterface;
import com.neux.proj.insurance.location.GetNowLocation;
import com.neux.proj.insurance.utility.DateUtils;
import com.neux.proj.insurance.utility.JSCallbackUtils;
import com.neux.proj.insurance.utility.NetworkUtil;
import com.neux.proj.insurance.utility.StringUtils;


public class WebViewJSInterface// extends Activity
{
    Context mContext;
    String tmpTableName;
    String tmpTableId;
    String tmpXMLData;
    String tmpType;
    String tmpModifyTime;
    String tmpQueryStr;
    public static String QRCode_URL;

    // Cloud Added 20131018 Begin .. fb redirect bug
    public static String CompareURL;
    public static String RedirectURL;
    // Cloud Added End ..

    private GetNowLocation mLocation;  // Cloud Added 20131108 GetLocation

    //titan added 2013-12-30--about login
    private boolean isLogin = false;
    private String loginType = "";
    private String loginUserId = "";
    private static String loginSuccessCallbackFunction = null;
    private static String publishSuccessCallbackFunction = null;
    private static List<String> publishSuccessCallbackFunctionParams = new ArrayList<String>();

    
    
    
    public WebViewJSInterface(Context context,GetNowLocation location)
    {
        mContext = context;
        QRCode_URL = "";

        // Cloud Added 20131018 Begin .. fb redirect bug
        WebViewJSInterface.RedirectURL = "";
        WebViewJSInterface.CompareURL = "";
        //CompareURL = "https://m.facebook.com/dialog/feed;https://www.facebook.com/plugins/close_popup.php";
        //redirectURL = "http://www.google.com.tw";
        // Cloud Added End ..

        mLocation = location;	// Cloud Added 20131108 GetLocation
    }


    @JavascriptInterface
    public void setPublishSuccessCallbackFunction(String function)
    {
        this.publishSuccessCallbackFunction = function;
    }

    @JavascriptInterface
    public String getPublishSuccessCallbackFunction()
    {
        return this.publishSuccessCallbackFunction;
    }

    @JavascriptInterface
    public String getLoginSuccessCallbackFunction()
    {
        return this.loginSuccessCallbackFunction;
    }

    @JavascriptInterface
    public void setLoginSuccessCallbackFunction(String function)
    {
        this.loginSuccessCallbackFunction = function;
    }

    @JavascriptInterface
    public void addPublishSuccessCallbackFuncitonParam(String param) {
        this.publishSuccessCallbackFunctionParams.add(param);
    }

    @JavascriptInterface
    public void clearPublishSuccessCallbackFuncitonParam() {
        this.publishSuccessCallbackFunctionParams.clear();
    }

    @JavascriptInterface
    public String getDeviceId() {

        String regId = "";
        try {
            String GCMNUMBER = mContext.getString(R.string.gcm_project_number);
            GCMRegistrar.checkDevice(mContext);
            GCMRegistrar.checkManifest(mContext);

            regId = GCMRegistrar.getRegistrationId(mContext);

        } catch (Exception e) {
//            DialogUtils.openDialog(this, e.getMessage() + " oh my god");
            Log.e("DEBUG",e.getMessage());
        }

        return regId;
    }

    @JavascriptInterface
    public void sendSMS(String number,String text) {
        Log.e("DEBUG",text);
        SmsManager smsManager = SmsManager.getDefault();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(), 0);

        //傳送SMS
        smsManager.sendTextMessage(number, null, text, pendingIntent, null);
    }

    @JavascriptInterface
    public String getCalendar() {
        StringBuilder sb = new StringBuilder();

        Log.e("DEBUG","into android getCalendar method");

        try{

            String[] EVENT_PROJECTION = new String[] {
                    CalendarContract.Calendars._ID,                           // 0
                    CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
                    CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,          // 2
                    CalendarContract.Calendars.SYNC_EVENTS//3
            };

            // The indices for the projection array above.
            int PROJECTION_ID_INDEX = 0;
            int PROJECTION_ACCOUNT_NAME_INDEX = 1;
            int PROJECTION_DISPLAY_NAME_INDEX = 2;
            int PROJECTION_SYNC_EVENTS_INDEX = 3;

            ContentResolver cr = mContext.getContentResolver();
            Uri calendarURI = CalendarContract.Calendars.CONTENT_URI;

            Cursor calendarCur = cr.query(calendarURI, EVENT_PROJECTION, null, null, null);


            int count = 0;
            while (calendarCur.moveToNext()) {
                Log.e("DEBUG","have calendar data");

                long calendarID = calendarCur.getLong(PROJECTION_ID_INDEX);
                String displayName = calendarCur.getString(PROJECTION_DISPLAY_NAME_INDEX);
//                int syncEvents = calendarCur.getInt(PROJECTION_SYNC_EVENTS_INDEX);
//                String accountName = calendarCur.getString(PROJECTION_ACCOUNT_NAME_INDEX);


                if(count != 0) {
                    sb.append(",");
                }

                sb.append("{\"calendarID\": \""+calendarID+"\"," +
                        "    \"displayName\":\""+displayName +"\"}");

//                sb.append("{\"calendarID\": \""+calendarID+"\",\n" +
//                        "    \"displayName\":\""+displayName+"\",\n" +
//                        "    \"syncEvents\":\""+syncEvents+"\"}");


                count++;
            }


        }catch(Exception e) {
            JSCallbackUtils.call(MainWebViewActivity.m_WV, "alert", Arrays.asList(new String[]{e.getMessage()}));
            Log.e("DEBUG",e.getMessage());
            e.printStackTrace();
        }


        return "{" +
                "  \"calendar\": ["  + sb.toString() + "]" +
                "}";
    }

    @JavascriptInterface
    public String getCalendarEvent() {

        StringBuilder sb = new StringBuilder();
        Log.e("DEBUG","into android getCalendarEvent method");

//        JSCallbackUtils.call(MainWebViewActivity.m_WV,"alert",Arrays.asList(new String[]{"into android getCalendarEvent method"}));

        try{

            ContentResolver cr = mContext.getContentResolver();
            Uri eventURI = CalendarContract.Events.CONTENT_URI;

            String selection = "((" + CalendarContract.Events.TITLE + " <> ?) AND ("
                    + CalendarContract.Events.TITLE + " is not null))";

//            String selection = CalendarContract.Events.TITLE + " is not null";

            String[] selectionArgs = new String[] {""};

            Cursor cur = cr.query(eventURI, null, selection, selectionArgs, null);

            int count = 0;


            while (cur.moveToNext()) {

//                JSCallbackUtils.call(MainWebViewActivity.m_WV,"alert",Arrays.asList(new String[]{"into"}));

                int calendarID = cur.getInt(cur.getColumnIndex(CalendarContract.Events.CALENDAR_ID));
                long start = cur.getLong(cur.getColumnIndex(CalendarContract.Events.DTSTART));
                long end = cur.getLong(cur.getColumnIndex(CalendarContract.Events.DTEND));
                String title = cur.getString(cur.getColumnIndex(CalendarContract.Events.TITLE));
                String description = cur.getString(cur.getColumnIndex(CalendarContract.Events.DESCRIPTION));
                String allDay = cur.getString(cur.getColumnIndex(CalendarContract.Events.ALL_DAY));
                String eventId = cur.getString(cur.getColumnIndex(CalendarContract.Events._ID));

//                JSCallbackUtils.call(MainWebViewActivity.m_WV,"alert",Arrays.asList(new String[]{title,eventId}));

                title = title.replaceAll("\n","");
                description = description.replaceAll("\n","");

                String startDate = DateUtils.to14DateString(start);
                String endDate = DateUtils.to14DateString(end);

//                JSCallbackUtils.call(MainWebViewActivity.m_WV,"alert",Arrays.asList(new String[]{startDate}));

                if(count != 0) {
                    sb.append(",");
                }

                sb.append("{\"calendarID\": \""+calendarID+"\"," +
                        "    \"eventId\": \""+eventId+"\"," +
                        "    \"start\": \""+startDate+"\"," +
                        "    \"end\": \""+endDate+"\"," +
                        "    \"title\": \""+title+"\"," +
                        "    \"allDay\": \""+allDay+"\"," +
                        "    \"description\":\""+description+"\"}");

                count++;
            }
        }catch(Exception e) {
            Log.e("DEBUG",e.getMessage());
            e.printStackTrace();
            JSCallbackUtils.callJsAlert(MainWebViewActivity.m_WV,e.getMessage());
        }

        return "{" +
                "  \"event\": ["  + sb.toString() + "]" +
                "}";
    }

    @JavascriptInterface
    public String getAccounts() {

        StringBuilder json = new StringBuilder();

        int count = 0;

        try{
            AccountManager accountManager = AccountManager.get(mContext);
            Account[] accounts = accountManager.getAccounts();
            for(Account account : accounts){

                if(count != 0) {
                    json.append(",");
                }

                json.append("{\"name\": \""+account.name+"\",\n" +
                        "    \"type\": \""+account.type+"\"}");

//                String password = accountManager.getPassword(account);
//
//                json.append("{\"name\": \""+account.name+"\",\n" +
//                        "    \"type\": \""+account.type+"\",\n" +
//                        "    \"password\": \""+password+"\"}");

                count++;
            }
        }catch(Exception e) {
            Log.e("DEBUG",e.getMessage());
            e.printStackTrace();
            JSCallbackUtils.callJsAlert(MainWebViewActivity.m_WV,e.getMessage());
        }


        return "{\n" +
                "  \"account\": ["  + json.toString() + "]\n" +
                "}";
    }

    
    //Yuki added 2014.10.24  latitude and  longitude Function
    @JavascriptInterface
    public String getGPS() {

        StringBuilder json = new StringBuilder();


        try{
        	
        	GpsActivity gps = new GpsActivity();
        	
            json.append("{\"latitude\": \""+gps.getLatitude()+"\",\n" +
                        "    \"longitude\": \""+gps.getLongitude()+"\"}");

        }catch(Exception e) {
            Log.e("DEBUG",e.getMessage());
            e.printStackTrace();
            JSCallbackUtils.callJsAlert(MainWebViewActivity.m_WV,e.getMessage());
        }


        return "{\n" +
                "  \"position\": ["  + json.toString() + "]\n" +
                "}";
    }


    

    
    @JavascriptInterface
    public String updateCalendarEvent(String eventID,String start,String end,String title,String description) {

        ContentResolver cr = mContext.getContentResolver();

        int rows = 0;

        try{
            long startMillis = DateUtils.to14TimeInMillis(start);
            long endMillis = DateUtils.to14TimeInMillis(end);

            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, startMillis);
            values.put(CalendarContract.Events.DTEND, endMillis);
            values.put(CalendarContract.Events.TITLE, title);
            values.put(CalendarContract.Events.DESCRIPTION, description);

            Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, Long.parseLong(eventID));

            rows = cr.update(uri, values, null, null);

        }catch(Exception e) {
            Log.e("DEBUG",e.getMessage());
            e.printStackTrace();

        }

        return String.valueOf(rows);
    }


    @JavascriptInterface
    public String deleteCalendarEvent(String eventID) {

        ContentResolver cr = mContext.getContentResolver();

        int rows = 0;

        try{
            Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, Long.parseLong(eventID));

            rows = cr.delete(uri, null, null);

        }catch(Exception e) {
            Log.e("DEBUG",e.getMessage());
            e.printStackTrace();

        }

        return String.valueOf(rows);
    }

    // Cloud Added 20130923 Begin ..
    @JavascriptInterface
    public String getContacts()
    {
        ShowContacts contacts = new ShowContacts(mContext);
        String result = "";
        try{
            result = contacts.GetContactsJSON();
            Log.e("DEBUG",result);
        }catch(Exception e) {
            JSCallbackUtils.callJsAlert(MainWebViewActivity.m_WV, e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    // Cloud Added 20130923 End ..

    // Cloud Added 20131108 GetLocation Begin ..
    @JavascriptInterface
    public String getLocation()
    {
        try{
            //MainWebViewActivity.GetLocation.Update();
            mLocation.Update();

            String location = mLocation.getLocation();

            return location;
        }catch(Exception e) {
            Log.e("DEBUG",e.getMessage());
            e.printStackTrace();
            JSCallbackUtils.callJsAlert(MainWebViewActivity.m_WV,e.getMessage());
        }

        return "false";
    }
    // Cloud Added 20131108 End ..

    // Cloud Added 20130921 Begin ..
	
	/*
	@JavascriptInterface
	public void callQR()
	{
		QRCode_URL = "";
		Intent i = new Intent(mContext,QRMain.class);		
		mContext.startActivity(i);	
	}
	
	
	@JavascriptInterface
	public String getQRCode()
	{
		return QRCode_URL;
	}
	*/
    // Cloud Added End ..

    @JavascriptInterface
    public int getNetWorkStatus()
    {
        return NetworkUtil.getConnectivityStatus(mContext);
    }

    @JavascriptInterface
    public String dbInsert(String Normalresponse)
    {
        boolean flag = true;
        dbColumnInterface dbColumn = new dbColumnInterface();
        dbColumn.JsonParser(Normalresponse);  // input normal value
        flag = dbColumn.checkInsertData();
        if (!flag)
            return String.valueOf(flag);
        DBSet set = new DBSet(mContext,dbColumn);
        return set.dbInsert();
    }

    @JavascriptInterface
    public String dbUpdate(String Normalresponse , String Whereresponse)
    {
        boolean flag = true;
        dbColumnInterface dbColumn = new dbColumnInterface();
        dbColumn.JsonParser(Normalresponse); // input normal value
        flag = dbColumn.checkUpdateData();
        if (!flag)
            return String.valueOf(flag);

        if (!CheckWhereResponse(dbColumn,Whereresponse))  // input where string
            return "false";

        //dbColumn.debugShowValue();
        DBSet set = new DBSet(mContext,dbColumn);
        return set.dbUpdate();
    }


    // Cloud Added 20130812 Begin ..
    @JavascriptInterface
    public void newBrowser(String url)
    {
        try{
            if(!url.startsWith("http")) {
                url = mContext.getResources().getString(R.string.GetUrl) + "/" +url;
            }

            //改成是呼叫另一個activity載url
//            Intent i = new Intent(mContext,NewBrowserActivity.class);
//
////            MainWebViewActivity.m_WV.loadUrl("javascript:alert('"+url+"');");
//
//            Bundle bundle = new Bundle();
//            bundle.putString("url",url);
//            i.putExtras(bundle);
//
//            mContext.startActivity(i);

            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            mContext.startActivity(intent);
        }catch(Exception e)
        {
            MainWebViewActivity.m_WV.loadUrl("javascript:alert('"+e.toString()+"');");
            Log.e("DEBUG","newBrowser Error e = " + e.toString());
        }
    }


    // Cloud Added End ..

    @JavascriptInterface
    public void callPhone(String number)
    {

        //Intent intentDial = new Intent("android.intent.action.CALL",Uri.parse("tel:"+number));
        try{


            if(number.length() < 10) {
                number = StringUtils.leftPad(number,10,"0");
            }

//            if(!number.startsWith("+886") && number.startsWith("0")) {
//                number = "+886" + number.substring(1);
//            }

            if(!number.startsWith("tel:")) {
                number = "tel:" + number;
            }

            Intent intentDial = new Intent("android.intent.action.CALL",Uri.parse(number));
            mContext.startActivity(intentDial);
        }catch (Exception e)
        {
            JSCallbackUtils.callJsAlert(MainWebViewActivity.m_WV, e.getMessage());
            e.printStackTrace();

        }
    }


    // Cloud Added 20130806 Begin ..
    @JavascriptInterface
    public String dbUpdate(String response)
    {
        boolean flag = true;
        dbColumnInterface dbColumn = new dbColumnInterface();
        dbColumn.JsonParser(response); // input normal value
        flag = dbColumn.checkUpdateData();
        if (!flag)
            return String.valueOf(flag);

        String Whereresponse = dbColumn.tmp_query_jsonstr;

        if (!CheckWhereResponse(dbColumn,Whereresponse))  // input where string
            return "false";

        DBSet set = new DBSet(mContext,dbColumn);
        return set.dbUpdate();

    }
    // Cloud Added End ..


    // Cloud Added 20131018 Begin .. fb redirect bug
    @JavascriptInterface
    public void setRedirectURL(String redirectURL)
    {
        WebViewJSInterface.RedirectURL = redirectURL;
    }

    @JavascriptInterface
    public void setCompareURL(String CompareURL)
    {
        WebViewJSInterface.CompareURL = CompareURL;
    }
    // Cloud Added End ..

    @JavascriptInterface
    public String dbDelete(String Whereresponse)
    {
        dbColumnInterface dbColumn = new dbColumnInterface();
        if (!CheckWhereResponse(dbColumn,Whereresponse))	// input where string
            return "false";
        DBSet set = new DBSet(mContext,dbColumn);
        return set.dbDelete();
    }

    @JavascriptInterface
    public String dbQuery(String Whereresponse)
    {

        dbColumnInterface dbColumn = new dbColumnInterface();
        if (!CheckWhereResponse(dbColumn,Whereresponse))	// input where string
            return "false";


        DBGet get = new DBGet(mContext,dbColumn);

        return get.dbQuery();
    }

    @JavascriptInterface
    public void gmap(String mapUrl) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        mContext.startActivity(intent);

        // Bundle bundle = new Bundle();
        // bundle.putString("lat", lat);
        // bundle.putString("lng", lng);
        // bundle.putString("title", title);
        // bundle.putString("address", address);
        //
        // Intent intent = new Intent();
        // intent.setClass(WebViewActivity.this, GoogleMapActivity.class);
        // intent.putExtras(bundle);
        // startActivity(intent);
    }

    @JavascriptInterface
    public void openProgressBar() {
        new ProgressDialogAsyncTask().execute("show");
    }

    @JavascriptInterface
    public void closeProgressBar() {
        new ProgressDialogAsyncTask().execute("hide");
    }

    private class ProgressDialogAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(String result) {
            if ("show".equals(result)) {
                MainWebViewActivity.showProgressDialog();
            } else if ("hide".equals(result)) {
                MainWebViewActivity.closeProgressDialog();
            }
        }
    }

//    @JavascriptInterface
//    public void showWebView() {
//
//        JSCallbackUtils.callJsAlert(MainWebViewActivity.m_WV,"into native showWebView -> " + MainWebViewActivity.imageLayout.getVisibility());
//
//        if(MainWebViewActivity.imageLayout.getVisibility() == View.VISIBLE) {
//            JSCallbackUtils.callJsAlert(MainWebViewActivity.m_WV,"finish1");
//
//            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
//                    MainWebViewActivity.imageLayout.getLayoutParams().width,
//                    0, 0.0f);
//
//            MainWebViewActivity.imageLayout.setLayoutParams(param);
//
//
////            MainWebViewActivity.imageLayout.setVisibility(View.GONE);
////            MainWebViewActivity.imageView.getLayoutParams().height = 0;
////            MainWebViewActivity.imageView.setMaxHeight(0);
//
//            JSCallbackUtils.callJsAlert(MainWebViewActivity.m_WV,"finish3");
//
////            MainWebViewActivity.webviewLayout.setVisibility(View.VISIBLE);
//            JSCallbackUtils.callJsAlert(MainWebViewActivity.m_WV,"finish4");
////            MainWebViewActivity.instance.setContentView(MainWebViewActivity.m_WV);
//
////            Display display = MainWebViewActivity.instance.getWindowManager().getDefaultDisplay();
////            Point size = new Point();
////            display.getSize(size);
////            int width = size.x;
////            int height = size.y;
//
////            JSCallbackUtils.callJsAlert(MainWebViewActivity.m_WV,"finish4->" + MainWebViewActivity.m_WV.getHeight() + "->" + MainWebViewActivity.m_WV.getLayoutParams().height);
////            MainWebViewActivity.m_WV.setMinimumHeight(height);
////            JSCallbackUtils.callJsAlert(MainWebViewActivity.m_WV,"finish5->" + MainWebViewActivity.m_WV.getHeight() + "->" + MainWebViewActivity.m_WV.getLayoutParams().height);
////            MainWebViewActivity.instance.setContentView(MainWebViewActivity.m_WV);
////            JSCallbackUtils.callJsAlert(MainWebViewActivity.m_WV,"finish6->" + MainWebViewActivity.m_WV.getHeight() + "->" + MainWebViewActivity.m_WV.getLayoutParams().height);
//
////            MainWebViewActivity.m_WV.refreshDrawableState();
////            MainWebViewActivity.m_WV.onResume();
//        }
//
////        if(MainWebViewActivity.m_WV.getVisibility() == View.GONE) {
////            MainWebViewActivity.imageView.setVisibility(View.GONE);
////
////        }
//    }


    private void clearLoginInfo() {
        isLogin = false;
        loginType = "";
        loginUserId = "";
    }


    private boolean CheckWhereResponse(dbColumnInterface dbColumn , String Whereresponse)
    {
        boolean flag = true;
        if (MyUtilityManager.checkStrNotNull(Whereresponse))
            flag = dbColumn.JsonParserWhere(Whereresponse);

        return flag;
    }



    public void SetTempStrInit()
    {
        this.tmpTableName = null;
        this.tmpTableId = null;
        this.tmpType = null;
        this.tmpModifyTime = null;
        this.tmpXMLData = null;
        this.tmpQueryStr = null;

    }
}
