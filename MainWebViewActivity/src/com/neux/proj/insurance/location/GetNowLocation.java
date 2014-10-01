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
	public String bestProvider = LocationManager.GPS_PROVIDER;	//�̨θ�T���Ѫ�
	public GetNowLocation(Context context)
	{
		mContext = context;	
		//���o�t�Ωw��A��
		LocationManager status = (LocationManager) (mContext.getSystemService(Context.LOCATION_SERVICE));
		if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) 
		{
			getLocationFlag = true;	//�T�{�}�ҩw��A��
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
		mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);	//���o�t�Ωw��A��
		Criteria criteria = new Criteria();	//��T���Ѫ̿���з�
		bestProvider = mLocationManager.getBestProvider(criteria, true);	//��ܺ�ǫ׳̰������Ѫ�
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
			//�A�ȴ��Ѫ̡B��s�W�v60000�@��=1�����B�̵u�Z���B�a�I���ܮɩI�s����
		} 
	}
	
	public void remove()
	{
		if(getLocationFlag) 
    	{
    		mLocationManager.removeUpdates(this);	//���}�����ɰ����s
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
