package com.neux.proj.insurance.dbsetget;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.neux.proj.insurance.db.DBHelper;
import com.neux.proj.insurance.db.DBHelperAdapter;
import com.neux.proj.insurance.db.DBHelper;
import com.neux.proj.insurance.db.DBHelperAdapter;


public class DBSet
{
	Context mContext;
	dbColumnInterface mDBColumn;
	DBHelperAdapter DB_Adapter;
	
	public DBSet(Context context , dbColumnInterface dbcolumn)
	{
		mContext = context;
		mDBColumn = dbcolumn;
		DB_Adapter = new DBHelperAdapter(mContext);
	}
	
	// ******** dbUpdate **********//
	public String dbUpdate()
	{
		String strReturn = "false";
		boolean success = true;
		
		DB_Adapter.open();
		try{
			mDBColumn.setModifyTimeFormDevice();	// Set ModifyTime
			success = DB_Adapter.Update(DBHelper._TableName, mDBColumn.TableName, mDBColumn.TableId, mDBColumn.XMLData, mDBColumn.ModifyTime, mDBColumn.Type,mDBColumn.tmpQueryStr);
		}catch(Exception e)
		{
			Log.e("DEBUG","dbUpdate Error e = " + e.toString());
			DB_Adapter.close();
			return strReturn;
		}
		
		DB_Adapter.close();
		if (!success)
			strReturn = String.valueOf(success);
		else
			strReturn = mDBColumn.ModifyTime;
		return strReturn;
	}
	
	// ******** dbDelete **********//
	public String dbDelete()
	{
		String strReturn = "false";
		boolean success = true;
		
		DB_Adapter.open();
		try{			
			mDBColumn.setModifyTimeFormDevice();
			success = DB_Adapter.Delete(DBHelper._TableName, mDBColumn.tmpQueryStr);
		}catch(Exception e)
		{
			Log.e("DEBUG","dbDelete Error e = " + e.toString());
			DB_Adapter.close();	
			return strReturn;
		}
		DB_Adapter.close();
						
		strReturn = String.valueOf(success);
		return strReturn;
	}
	
	// ******** dbInsert **********//
	public String dbInsert()
	{
		String strReturn = "false";
		boolean success = true;
		Cursor c;
		DB_Adapter.open();	
		
		/*  Cloud Marked 20130918 Begin ..
		try{
			c = DB_Adapter.QueryExist(DBHelper._TableName,mDBColumn.TableName, mDBColumn.TableId);
		}catch(Exception e)
		{
			Log.e("DEBG","Inser QueryExit Error e = " + e.toString());
			DB_Adapter.close();
			return strReturn;
		}
		
		if (c.getCount() > 0)
		{        	
			// Update(i);	 有key是否還要刪除?
			DB_Adapter.close();
			strReturn = "Duplicate key !!";
			return strReturn;
		}                  
		else
		*/ // Cloud Marked End ..
		{   
			mDBColumn.setModifyTimeFormDevice();			
			try
			{
				success = DB_Adapter.Insert(DBHelper._TableName, mDBColumn.TableName, mDBColumn.TableId, mDBColumn.XMLData, mDBColumn.ModifyTime, mDBColumn.Type);				
			}catch(Exception e)
			{
				Log.e("DEBUG","DB Insert Error e = " + e.toString());
				DB_Adapter.close();
				return strReturn;
			}
			
			if (success)
				strReturn = mDBColumn.ModifyTime;	// 成功才傳回ModifyTime
			
		}
		DB_Adapter.close();    
          		
		return strReturn;
	}
	

}
