package com.neux.proj.insurance.dbsetget;

import com.neux.proj.insurance.db.DBHelper;
import com.neux.proj.insurance.db.DBHelperAdapter;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class DBGet 
{
	Context mContext;
	dbColumnInterface mDBColumn;
	DBHelperAdapter DB_Adapter;
	
	public DBGet(Context context , dbColumnInterface dbcolumn)
	{
		mContext = context;
		mDBColumn = dbcolumn;
		DB_Adapter = new DBHelperAdapter(mContext);
	}
	
	public String dbQuery()
	{
		String strReturn = "false";
		DB_Adapter.open();		
		Cursor c;
		try{
			c = DB_Adapter.QueryAll(DBHelper._TableName, mDBColumn.tmpQueryStr);
		}catch (Exception e)
		{
			Log.e("DEBUG","dbQuery Error e = " + e.toString());
			DB_Adapter.close();
			return strReturn;
		}
		JSONObject JSONobj = null; 
		JSONArray JSONarry = new JSONArray();		
        if (c.getCount() > 0)
        {
        	if (c.moveToFirst())
        	{
        		do
	        	{        			
        			try
        			{
        				JSONobj = new JSONObject();	// JSON Object
        				JSONobj.put(DBHelper.Column_TableName, c.getString(1));
        				JSONobj.put(DBHelper.Column_TableId, c.getString(2));
        				JSONobj.put(DBHelper.Column_XMLData, c.getString(3));
        				JSONobj.put(DBHelper.Column_ModifyTime, c.getString(4));
        				JSONobj.put(DBHelper.Column_Type, c.getString(5));         				
        			}
        			catch (Exception e)
        			{
        				Log.e("DEBUG","Get JSON Error e = " + e.toString());
        				JSONarry = null;        				
        			}
        			        			        			        		
        			JSONarry.put(JSONobj);
	        	}while(c.moveToNext());
        	}
        }
        
      
        strReturn = JSONarry.toString();
        	 
  
		DB_Adapter.close();		
		return strReturn;
	}

}
