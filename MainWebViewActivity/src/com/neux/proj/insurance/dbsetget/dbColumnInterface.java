package com.neux.proj.insurance.dbsetget;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.neux.proj.insurance.db.DBHelper;
import com.neux.proj.insurance.utility.AppDefault;
import com.neux.proj.insurance.MyUtilityManager;

import android.util.Log;

public class dbColumnInterface 
{
	public String TableName = null;
	public String TableId = null;
	public String XMLData = null;
	public String Type =  null;
	public String ModifyTime = null;
	public String tmpQueryStr = "1 = 1";
	
	public String tmp_query_jsonstr = null;	// Added 20130806  


	public void JsonParser(String response)
	{							
		try
		{
			JSONObject jsonObj = new JSONObject(response);						
			Iterator iterator = jsonObj.keys();
			
			 while(iterator.hasNext())
             {	    	            	  
				 String key = (String)iterator.next();	            	  	            	  
				 if (DBHelper.Column_TableName.equals(key))
					 	this.TableName = jsonObj.getString(key);	            		  	            	  
				 if (DBHelper.Column_TableId.equals(key))
					 	this.TableId = jsonObj.getString(key);
				 if (DBHelper.Column_XMLData.equals(key))
					 	this.XMLData = jsonObj.getString(key);
				 if (DBHelper.Column_ModifyTime.equals(key))
					 	this.ModifyTime = jsonObj.getString(key);
				 if (DBHelper.Column_Type.equals(key))
					 	this.Type = jsonObj.getString(key);		
				 
				 // Cloud Added 20130806 Begin ..				 
				 if (AppDefault.JSON_INTERFACE_QUERY_STR.equals(key))
				 {
					this.tmp_query_jsonstr = jsonObj.getString(key);					
				 }
				 
				 // Cloud Added End ..
             }				              	          
		}
		catch(Exception e)
		{
			Log.e("DEBUG","JsonParser Error e  = " + e.toString());
		}
	}
	
	public boolean JsonParserWhere(String response)
	{				
		Boolean flag = true;
		try
		{
			//JSONObject jsonMainObj = new JSONObject(response);						
			//JSONArray arrayJson =jsonMainObj.getJSONArray(AppDefault.JSON_INTERFACE_CONDITION);
			JSONArray arrayJson = new JSONArray(response);		
			ConditionColumn[] condition = new ConditionColumn[arrayJson.length()];			
			for (int i = 0 ; i<=arrayJson.length()-1 ; i++)
			{				
				condition[i] = new ConditionColumn();
				JSONObject jsonSubObject = arrayJson.getJSONObject(i);				
	            Iterator iterator = jsonSubObject.keys();	            
	            while(iterator.hasNext())
	            {	            	 
	            	 String key = (String)iterator.next();	            	 
	            	 if (AppDefault.JSON_CONDITION_KEY.equals(key))	            	 	            		 
	            		 condition[i].ConditionKey = jsonSubObject.getString(key);	            	 
	            	 if (AppDefault.JSON_CONDITION_SYMBOL.equals(key))
	            	 {
	            		 //condition[i].ConditionSymbol = jsonSubObject.getString(key);
	            		 ParserSymbol sym = new ParserSymbol(jsonSubObject.getString(key));	            		 
	            		 condition[i].ConditionSymbol = sym.GetSymbol();
	            	 }
	            	 if (AppDefault.JSON_CONDITION_VALUE.equals(key))	            		            	
	            	 	 condition[i].ConditionValue = jsonSubObject.getString(key);	            	 	            	 
	            }
			}			
			
			
			for (int i = 0 ; i<=condition.length-1 ; i++)	
			{
				flag = condition[i].CheckRule();  // Check Parm Rule
				if (!flag)
					return flag;
				
				condition[i].SetQueryStr();
				
				if (MyUtilityManager.checkStrNotNull(condition[i].ConbineQueryStr))
				{
					this.tmpQueryStr += " AND ";
					this.tmpQueryStr += condition[i].ConbineQueryStr;						
				}
				
			}
			
			/*
			for (int i = 0 ; i<=condition.length-1 ; i++)
			{
				if (MyUtilityManager.checkStrNotNull(condition[i].ConbineQueryStr))
				{
					this.tmpQueryStr += " AND ";
					this.tmpQueryStr += condition[i].ConbineQueryStr;						
				}
			}
			*/				
			
		//	Log.e("DEBUG","tmpQueryStr = " + this.tmpQueryStr);
		}
		catch(Exception e)
		{			
			Log.e("DEBUG","ParserWhere Error e = " + e.toString());
			return false;
		}
		
		return flag;
	}
			
	public void setModifyTimeFormDevice()
	{
		this.ModifyTime = MyUtilityManager.GetDateTime();
	}
	
	public boolean checkInsertData()
	{
		boolean flag = true;
		if(
			(MyUtilityManager.checkStrIsNull(this.TableName)) ||
			(MyUtilityManager.checkStrIsNull(this.TableId)) ||
			(MyUtilityManager.checkStrIsNull(this.Type)) ||
			(MyUtilityManager.checkStrIsNull(this.XMLData))
		  )
		{
			flag = false;
		}
		
		return flag;
	}
	
	public boolean checkUpdateData()
	{
		boolean flag = true;
		if(
//			(MyUtilityManager.checkStrIsNull(this.TableName)) ||
//			(MyUtilityManager.checkStrIsNull(this.TableId)) ||
			(MyUtilityManager.checkStrIsNull(this.Type)) ||
			(MyUtilityManager.checkStrIsNull(this.XMLData))
		  )
		{
				flag = false;
		}
		
		return flag;
	}
	
	public void debugShowValue()
	{
		Log.e("DEBUG","TableName = " + this.TableName );
		Log.e("DEBUG","TableID = " + this.TableId );
		Log.e("DEBUG","XMLData = " + this.XMLData );
		Log.e("DEBUG","ModifyTime = " + this.ModifyTime );
		Log.e("DEBUG","Type = " +  this.Type);
		Log.e("DEBUG","QueryStr = " + this.tmpQueryStr);
	}
}
