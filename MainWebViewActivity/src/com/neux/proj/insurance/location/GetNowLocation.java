package com.neux.proj.insurance.location;

import org.json.JSONArray;
import org.json.JSONObject;


import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GetNowLocation  implements LocationListener
{
	Context mContext;
	String mLatitude;
	String mLongitude;
	boolean getLocationFlag;
	public LocationManager mLocationManager;   // Added 20131108
	public String bestProvider = LocationManager.GPS_PROVIDER;	//最佳資訊提供者
	public GetNowLocation(Context context)
	{
		mContext = context;	
		//取得系統定位服務
		LocationManager status = (LocationManager) (mContext.getSystemService(Context.LOCATION_SERVICE));
		if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) 
		{
			getLocationFlag = true;	//確認開啟定位服務
			locationInitial();
		} 
		else 
		{
			mLatitude = "0";
			mLongitude = "0";
		}
		
		
	}
	
	
	private void locationInitial() 
	{
		mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);	//取得系統定位服務
		Criteria criteria = new Criteria();	//資訊提供者選取標準
		bestProvider = mLocationManager.getBestProvider(criteria, true);	//選擇精準度最高的提供者
		Location location = mLocationManager.getLastKnownLocation(bestProvider);
		gps_loc(location);
	}
	
	private void gps_loc(Location location)
	{
		if (location != null) 
		{
			mLatitude = String.valueOf(location.getLatitude());
			mLongitude = String.valueOf(location.getLongitude());

		} 
		else 	
		{
			mLatitude = "0";
			mLongitude = "0";
		}

	}
	
	public String getLocation()
	{
		JSONObject JSONitem =  new JSONObject();
		JSONArray JSON_Array = new JSONArray();
		try{
			JSONitem.put("latitude",mLatitude);
			JSONitem.put("longitude", mLongitude);
		}catch (Exception e)
		{
			return "false";
		}
		return JSONitem.toString();
	}
	
	
	public void Update()
	{
		if(getLocationFlag) 
    	{
    		mLocationManager.requestLocationUpdates(bestProvider, 1000, 1, this);
			//服務提供者、更新頻率60000毫秒=1分鐘、最短距離、地點改變時呼叫物件
		} 
	}
	
	public void remove()
	{
		if(getLocationFlag) 
    	{
    		mLocationManager.removeUpdates(this);	//離開頁面時停止更新
		}
	}


	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		gps_loc(location);
	}


	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
}
