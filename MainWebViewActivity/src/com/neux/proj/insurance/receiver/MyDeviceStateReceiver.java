package com.neux.proj.insurance.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class MyDeviceStateReceiver extends BroadcastReceiver 			 
{
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		String intentAction = intent.getAction();
		if (intentAction.equals(ConnectivityManager.CONNECTIVITY_ACTION) || intentAction.equals(Intent.ACTION_BOOT_COMPLETED)) 
		{
			//Log.e("DEBUG","Calories RecevierConnection !");
		}
		
		if (Intent.ACTION_PACKAGE_ADDED.equalsIgnoreCase(intentAction) ||	Intent.ACTION_PACKAGE_REMOVED.equalsIgnoreCase(intentAction)) 
		{
			//Log.e("DEBUG","Add Package !");
		}
	}
	

}
