package com.neux.proj.insurance.service;

import com.neux.proj.insurance.utility.AppDefault;

import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.webkit.WebView;

public class ServiceCookieReq extends Service 
{
	
	public static Handler handler = new Handler();
	public static final int ONLINE = 1;
	public static final int OFFLINE = ONLINE + 1;
	
	private int lastest_status;
	
	
	public static WebView web_view;
	
	private boolean isNetworkAvailable() 
	{
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}
	
	
	@Override
	public IBinder onBind(Intent intent) 
	{		
		return null;		
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		//Log.e("DEBUG","service onCreate");
		handler.postDelayed(tCookieReq, AppDefault.SERVICE_NETWORK_THREAD);		// beacase : service == null : onCreate->onStart-> ...
													//			 service != null : onStart ...
		
		 if (!isNetworkAvailable() ) 
			 this.lastest_status = OFFLINE;
		 else 
			 this.lastest_status = ONLINE;
	}
	
	@Override
	public void onStart(Intent intent , int startId)
	{					
		//handler.postDelayed(tCookieReq, 20000);
		//Log.e("DEBUG","service onStart");
		super.onStart(intent, startId);
	}
	
	@Override 
	public void onDestroy()
	{
		handler.removeCallbacks(tCookieReq);
		super.onDestroy();
	}

	private Runnable tCookieReq = new Runnable() 
	{
		        public void run() 
		        {
		        	synchronized(handler)
		        	{
		            MyAsyncTask bkTask = new MyAsyncTask();
		        	bkTask.execute(this.getClass().getName());		        	
		            handler.postDelayed(this, AppDefault.SERVICE_POST_DELAY);
		        	}
		        }
	};

	// **************** AsyncTask ************** //
		public class MyAsyncTask extends AsyncTask<String, Integer, String> 
		{		
			
			String str_result = "";
		    public MyAsyncTask() 
		    {
		    }
		    @Override
		    protected void onPreExecute() 
		    {
		    }
		    @Override
		    protected String doInBackground(String... urls) 
		    {   		
		    	 str_result = "false";
		    	 
		    	
		    //	 if (MainWebViewActivity.m_WV == null)
		   // 		 return str_result;
		    	 
		    	 

		    	 
		    	 if (!isNetworkAvailable() ) 
		         {      		    		 
		
		    		 if (lastest_status == OFFLINE)
		    			 str_result = "false";
		    		 else
		    		 {
		    			 str_result =  String.valueOf(OFFLINE);	// 狀態改變時
		    		 	 lastest_status = OFFLINE;
		    		 	 //MainWebViewActivity.uiMessageHandler.sendEmptyMessage(1);
		    		 }
		         }
		    	 else
		    	 {
		    		 if (lastest_status == ONLINE)
		    			str_result = "false";
		    		 else
		    		 {
		    			str_result = String.valueOf(ONLINE);	// 狀態改變時
		    			lastest_status = ONLINE;
		    			//MainWebViewActivity.uiMessageHandler.sendEmptyMessage(2);
		    		 }
		    	 }
		        	
		          
		    	
		    	return str_result;
		    }		    		    
		    @Override
		    protected void onProgressUpdate(Integer... progress) {    		        
		    }
		    @Override
		    protected void onPostExecute(String result) 
		    {    
		    	
		    	/*
		    	if (String.valueOf(OFFLINE).equals(result))
		    		MainWebViewActivity.uiMessageHandler.sendEmptyMessage(1);
		    	else if (String.valueOf(ONLINE).equals(result))
		    		MainWebViewActivity.uiMessageHandler.sendEmptyMessage(2);
		    		*/
		    		
		    	if ("false".equals(result))
		    	{
		    		
		    	}
		    	else
		    	{
		    		try{
		    			//MainWebViewActivity.uiMessageHandler.sendEmptyMessage(1001);
		    			web_view.loadUrl("javascript:setNetWorkConf()");
		    		}catch(Exception e)
		    		{
		    			Log.e("DEBUG","call uiMessage Error e = " + e.toString());
		    		}
		    		Log.e("DEBUG","NetWork Status Changed");
		    	}
		    	
		    }
		}			
}
