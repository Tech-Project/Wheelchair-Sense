package com.neux.proj.insurance;

import java.io.File;
import java.io.InputStream;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
import com.neux.proj.insurance.R;

public class MyUtilityManager 
{
//	File sdDir = null; 
//    File sdDir1 = null;
//    File sdDir2 = null;
//	public UtilityManager()
//	{
//		//判斷sd記憶卡是否存在
//        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); 
//        if(sdCardExist)   
//        {                               
//        	sdDir = Environment.getExternalStorageDirectory();//得到根目錄
//        	sdDir1 = Environment.getDataDirectory();
//        	sdDir2 =Environment.getRootDirectory();
//       }   
//	}
	
	public static void CreatePath(String path)	// path = "XXX"
	{
		boolean sdCardExist = CheckSdCardStatus(); 
        if(sdCardExist)  
        {
        	File  sd=Environment.getExternalStorageDirectory();         	
        	path = sd.getPath() + "/" + path;
        	File file = new File(path);
        	//判斷文件夾是否存在,如果不存在則建立文件夾
        	if (!file.exists())
        		file.mkdir();
        }		  
	}
	
	public static boolean CheckSdCardStatus()
	{
		return true;
		//return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}
	
	public static void ShowToast(Context context,String str)
	{
		Toast toast = Toast.makeText(context,
			     str, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM, 0, 0);
		toast.show();
	}
	
	public static Bitmap RotateBitmpToPortrait(Bitmap bp)
	{		
		// Rotation picture
		int orientation;
		if(bp.getHeight() < bp.getWidth())
			orientation = 90;
	    else
	        orientation = 0;
		
	    if (orientation != 0) 
	    {
	    	Matrix matrix = new Matrix();
	        matrix.postRotate(orientation);
	        return Bitmap.createBitmap(bp, 0, 0, bp.getWidth(),bp.getHeight(), matrix, true);
	    } 
	    else
	    {	    	
	    	return bp;
	    	//return Bitmap.createScaledBitmap(bp, bp.getWidth(),bp.getHeight(), true);
	    }
	}
	
	public static Bitmap RotateBitmp(Bitmap bp)
	{
		int orientation = 90;
		Matrix matrix = new Matrix();
        matrix.postRotate(orientation);
		return Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(), matrix, true);
	}
	
	
	public static BitmapFactory.Options GetBitmapOptions(boolean inJustDecodeBoundsFlag)
	{		
		BitmapFactory.Options options = new BitmapFactory.Options();
 	   	try
 	   	{
 	   		// because not support 4.0 up
 	   		//BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(options,true);
 	   		
 	   		// 	on成true 並不會傳回真的bitmap，只會傳回長跟高,取得方法是參：http://fecbob.pixnet.net/blog/post/38335473-bitmapfactory.options%E8%A9%B3%E8%A7%A3
 	   		options.inJustDecodeBounds = inJustDecodeBoundsFlag;
 	   		
 	   		
 	        options.inPurgeable = true;
 		    options.inInputShareable = true; 	 		    
 		    options.inPreferredConfig = Bitmap.Config.RGB_565;
 	   	} catch (IllegalArgumentException e){
 	   		Log.e("DEBUG","1 e = " + e.toString());
 			return null;
 		} catch (SecurityException e) {
 			Log.e("DEBUG","2 e = " + e.toString());
 			return null;
 		} 
// 	   	catch (IllegalAccessException e) {
// 			Log.e("DEBUG","3 e = " + e.toString());
// 			return null;
// 		} catch (NoSuchFieldException e) {
// 			Log.e("DEBUG","4 e = " + e.toString());
// 			return null;
// 		}
 	   	return options;
	}
	
	public static Bitmap GetPrivatePic(String path , String filename)	// filename = "xxx.png"
	{
		//Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options(); // create options  
		options = MyUtilityManager.GetBitmapOptions(true);	// set options avoid OOM
		File  sd=Environment.getExternalStorageDirectory();
		path = sd.getPath() + "/" + path + "/" + filename;
		File file=new File(path);//創建圖片文件為了下面的判斷，你也可以不創建
        if(file.exists())
        {
            //decodeFile將文件轉化為Bitmap物件
            //bitmap=BitmapFactory.decodeFile(path,options);
        	return BitmapFactory.decodeFile(path,options);
     	   	
        }
        
        return null;
	}
	
	

	public static Bitmap fixSize(Bitmap bitmapAPIcon,int width , int height)
    {
        int iconWidth = 0;
        int iconHeight = 0;
        int newIconWidth = width;
        int newIconHeight = height;
        iconWidth = bitmapAPIcon.getWidth();   
        iconHeight = bitmapAPIcon.getHeight(); 
        float scaleIconWidth = ((float) newIconWidth)/iconWidth; 
        float scaleIconHeight = ((float) newIconHeight)/iconHeight; 
        Matrix iconMatrix = new Matrix();
        iconMatrix.postScale(scaleIconWidth, scaleIconHeight); 
       
        //Bitmap finalAPicon = Bitmap.createBitmap(bitmapAPIcon, 0, 0,iconWidth, iconHeight, iconMatrix, true); //建立新的Bitmap

        return Bitmap.createBitmap(bitmapAPIcon, 0, 0,iconWidth, iconHeight, iconMatrix, true); //建立新的Bitmap
        //return finalAPicon;
    }

	

	
	public static Bitmap DrawableToBitmap(Context context,int drawableId , boolean inJustDecodeBoundsFlag)
	{
		InputStream is = context.getResources().openRawResource(drawableId);
		BitmapFactory.Options options=new BitmapFactory.Options();
		options = GetBitmapOptions(inJustDecodeBoundsFlag);	
		return BitmapFactory.decodeStream(is,null,options);
	}

	
	
	// Show Msg
	public static void UserOnlyShowMsgPositive(Context context,int title,int message,int PosStr)
	{
		new AlertDialog .Builder(context) .setTitle(title) .setMessage(message) .setPositiveButton(PosStr, new DialogInterface.OnClickListener()
        {  
           public void onClick(DialogInterface arg0, int arg1)
           {  
           } 
        }).show();
	}
	public void UserOnlyShowMsgPositive(Context context,String title,String message,String PosStr)
	{
		new AlertDialog .Builder(context) .setTitle(title) .setMessage(message) .setPositiveButton(PosStr, new DialogInterface.OnClickListener()
        {  
           public void onClick(DialogInterface arg0, int arg1)
           {  
           } 
        }).show();
	}

	
	public static String GetDateTime()
	{
		
		Time t = new Time();
		t.setToNow();
 	   	int year_i = t.year;
 	   	int month_i = t.month;
 	   	month_i += 1;  // because month is 0~11
 	   	int day_i = t.monthDay;
 	   	String year_t = Integer.toString(year_i);
 	   	String month_t = Integer.toString(month_i);
 	   	String day_t = Integer.toString(day_i);
 	   	if (month_t.length() == 1)
 	   		month_t = "0" + month_t;
 	   	if (day_t.length() == 1)
 	   		day_t = "0" + day_t;
 	   	
 	   	int hour_i = t.hour;
 	   	int min_i = t.minute;
 	   	int second_i = t.second;
 	   	String hour_t = Integer.toString(hour_i);
 	   	String min_t = Integer.toString(min_i);
 	   	String second_t = Integer.toString(second_i);
 	   	if (hour_t.length() == 1)
 	   		hour_t = "0" + hour_t;
 	   	if (min_t.length() == 1)
 	   		min_t = "0" + min_t;
 	   	if (second_t.length() == 1)
 	   		second_t = "0" + second_t;
 	     	   
 	   	String time_str = year_t + month_t + day_t + hour_t + min_t + second_t; 	   
 	   
		return time_str;
	}
	
	public static Double StringToDouble(String str) // format xx.xx or xx
	{
		String temp_str;
		temp_str = str;
		str = "";
		for(int i=0;i<temp_str.length();i++)	
		{
			if (temp_str.charAt(i) == '.')
				str+=temp_str.charAt(i);
			if(temp_str.charAt(i)>='0' && temp_str.charAt(i)<='9')
				 str+=temp_str.charAt(i);
		}
		
		double number = Double.parseDouble(str);
		return number;
	}
	public static int StringToInt(String str)
	{
		String temp_str;
		temp_str = str;
		str = "";
		for(int i=0;i<temp_str.length();i++)	
		{						
			if(temp_str.charAt(i)>='0' && temp_str.charAt(i)<='9')
				 str+=temp_str.charAt(i);
		}
		
		int number = Integer.parseInt(str);
		return number;
	}
	
	public static String SplitDate(Context context,String Ori_Date)
	{
		 String year = Ori_Date.trim().substring(0,4);
		 String month = Ori_Date.trim().substring(4,6);
		 String date = Ori_Date.trim().substring(6,8);
		 String hour = Ori_Date.trim().substring(8,10);
		 String minute = Ori_Date.trim().substring(10,12);
		 String second = Ori_Date.trim().substring(12,14);
		 
		 String tmp_str = year + context.getResources().getString(R.string.str_year);
		 tmp_str += month + context.getResources().getString(R.string.str_month);
		 tmp_str += date + context.getResources().getString(R.string.str_date);
		 tmp_str += "\n";
		 tmp_str += hour + context.getResources().getString(R.string.str_hour);
		 tmp_str += minute + context.getResources().getString(R.string.str_minute);
		 tmp_str += second + context.getResources().getString(R.string.str_second);
		 return tmp_str;
		 
	}
	
	public static boolean checkStrIsNull(String str)
	{
		if ((str == null) || "".equals(str))
			return true;
		
		return false;
	}
	
	public static boolean checkStrNotNull(String str)
	{		

						
		if (str == null)
			return false;
		if ("".equals(str))
			return false;
		
		// Cloud Added 20130806 Begin ..
		if ("null".equals(str))
			return false;
		// Cloud Added 20130806 End ..

		return true;
	}
	
	public static void GetDeviceId()
	{
		
	 	
//		String imeistring=null;				
//        String imsistring=null;		
//        TelephonyManager   telephonyManager=(TelephonyManager)getSystemService( Context.TELEPHONY_SERVICE );
//        
//        // IMEI
//        imeistring = telephonyManager.getDeviceId();
//        // IMSI
//        imsistring = telephonyManager.getSubscriberId();         
//        // Serial no ;  Works for Android 2.3 and above	
//        String hwID = android.os.SystemProperties.get("ro.serialno", "unknown");
//        
//        
//        String serialnum = null;      
//        try 
//        {         
//        	Class<?> c = Class.forName("android.os.SystemProperties");        	           	      
//        	Method get = c.getMethod("get", String.class, String.class );                 
//            serialnum = (String)(   get.invoke(c, "ro.serialno", "unknown" ));    	 
//        } 
//   	 	catch (Exception ignored) 
//        {       
//   	 		Log.e("DEBUG","serialnum exception e = " + ignored.toString());
//        }
//   	 	String serialnum2 = null;
//        try 
//        {
//        	Class myclass = Class.forName( "android.os.SystemProperties" );
//          	 Method[] methods = myclass.getMethods();
//          	 Object[] params = new Object[] { new String( "ro.serialno" ) , new String("Unknown" ) };        	
//          	 serialnum2 = (String)(methods[2].invoke( myclass, params ));        	             
//        }
//        catch (Exception ignored) 
//   	 	{   
//        	Log.e("DEBUG","serialnum2 exception e = " + ignored.toString());
//   	 	}
//        
//      /* Settings.Secure.ANDROID_ID returns the unique DeviceID
// 	   * Works for Android 2.2 and above				     
// 	   */
//        String androidId = Settings.Secure.getString(this, Settings.Secure.ANDROID_ID);
//        
//        Log.e("DEBUG","imei = " + imeistring);
//        Log.e("DEBUG","imsi = " + imsistring);
//        Log.e("DEBUG","serialnum = " + serialnum);
//        Log.e("DEBUG","serialnum2 = " + serialnum2);
//        Log.e("DEBUG","hwID = " + hwID);
//        Log.e("DEBUG","androidID = " + androidId);
		
	}
}
