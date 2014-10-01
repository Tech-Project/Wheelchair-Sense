/**
 * Copyright 2013 Cloud
 * This file is part of footdisc
 */
package com.neux.proj.insurance.db;

import com.neux.proj.insurance.MyUtilityManager;
import com.neux.proj.insurance.MyUtilityManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class DBHelperAdapter 
{
	private Context mContext;
	private DBHelper dbHelper;
	private SQLiteDatabase database;
	public DBHelperAdapter(Context context)
	{
		mContext = context;		
	}
	
	public DBHelperAdapter open() throws SQLException 
	{  
		//Log.e("DEBUG","DBHelperAdapter open");
        dbHelper = new DBHelper(mContext);          
        database = dbHelper.getWritableDatabase();  
        return this;  
    }  
	
	public void close()
	{		
		//Log.e("DEBUG","DBHelperAdapter close");
		dbHelper.close();
		database.close();
	}
	

	// update  return true or false (>0 or <0)
	public boolean updateRecord(String table_name,long rowId, String date, String FootSize , int FootStyle , int LegStyle , int Result) 
    {  
		/*
        ContentValues updateValues = createContentValues(date, FootSize , FootStyle , LegStyle , Result);    
        return database.update(table_name, updateValues, DBHelper.ID + "="  + rowId, null) > 0;
        */
		
		return false;
    }  
	

    
    public Cursor fetchAllSort(String table_name,String sort)
    {
    	
    	/*
    	return database.query(table_name, new String[] { DBHelper.ID, DBHelper.DATE, DBHelper.FOOTSIZE,
        		DBHelper.FOOTSTYLE,DBHelper.LEGSTYLE,DBHelper.RESULT}, 
        		null, null, null,  
                null, DBHelper.DATE + sort);
        */
    	
    	Cursor aa = null;
    	return aa;
    }
    

		
	private ContentValues createContentValues(String TableName, String TableId , String XMLData , String ModifyTime , String Type) 
    {  
        ContentValues values = new ContentValues();          
        values.put(DBHelper.Column_TableName,TableName);
        values.put(DBHelper.Column_TableId,TableId);
        values.put(DBHelper.Column_XMLData,XMLData);
        values.put(DBHelper.Column_ModifyTime,ModifyTime);
        values.put(DBHelper.Column_Type,Type);                                                         
        return values;  
    }
	private  ContentValues createValuesForUpdate(String TableName, String TableId , String XMLData , String ModifyTime , String Type)
	{
		ContentValues values = new ContentValues();
//		
//		if (MyUtilityManager.checkStrNotNull(TableName))
//			values.put(DBHelper.Column_TableName,TableName);
//		
//		if (MyUtilityManager.checkStrNotNull(TableId))			
//			values.put(DBHelper.Column_TableId,TableId);
		
		if (MyUtilityManager.checkStrNotNull(XMLData))
			values.put(DBHelper.Column_XMLData,XMLData);
		
						
		if (MyUtilityManager.checkStrNotNull(Type))
			values.put(DBHelper.Column_Type,Type);
		
		values.put(DBHelper.Column_ModifyTime,ModifyTime);	// ModifyTime Update
        return values;  
	}
	
	
	// ***************   Delete   ***************//
	public boolean Delete(String DB_TableName , String whereStr) throws SQLException
	{		
		return database.delete(DB_TableName, whereStr, null) > 0;  //  >0 ¬°ture
	}
	// ***************   Insert   ***************//	                              
	public boolean Insert(String DB_TableName,String TableName, String TableId , String XMLData , String ModifyTime , String Type) 
    {  		
		
        ContentValues Values = createContentValues(TableName, TableId , XMLData , ModifyTime , Type);    
        return database.insert(DB_TableName, null, Values)>0;  // return -1 = false , or return id
        		
    }  
	
	
	// ***************   Update   ***************//
	// update  return true or false (>0 or <0)
	public boolean Update(String DB_TableName,String TableName, String TableId , String XMLData , String ModifyTime , String Type , String whereStr) 
    {  		
		ContentValues Values = createValuesForUpdate(TableName, TableId , XMLData , ModifyTime , Type);				
        return database.update(DB_TableName, Values , whereStr, null) > 0;        // return -1 = false , or return id				
    }  

	
		
	// *****  CheckInsertRule  ****//
	private String GetWhereRule(String TableName , String TableId)
	{
		String str_q = DBHelper.Column_ID + " like '%'" ;
		if (MyUtilityManager.checkStrNotNull(TableName))
			str_q += " AND " + DBHelper.Column_TableName + "='" + TableName + "'";
		if (MyUtilityManager.checkStrNotNull(TableId))
			str_q +=" AND " + DBHelper.Column_TableId + "='" + TableId + "'";
		
		return str_q;
	}
	
	
	// ***************   Query   ***************//
	public Cursor QueryExist(String DB_TableName,String TableName,String TableId) throws SQLException 
    {      	    	  
		String str_q = GetWhereRule(TableName,TableId);							
		Cursor mCursor = null;
        mCursor = database.query(true, DB_TableName, new String[] 
        		{DBHelper.Column_ID, DBHelper.Column_TableName, DBHelper.Column_TableId,
        		 DBHelper.Column_XMLData,DBHelper.Column_ModifyTime,DBHelper.Column_Type},  
        		str_q
        		, null, null, null, null, null);  
        if (mCursor != null) 
        {  
            mCursor.moveToFirst();  
        }         
        return mCursor;            	    	    	
    }  	
    public Cursor QueryAll(String DB_TableName , String whereStr) throws SQLException
    {  
    	Cursor mCursor = null;
        mCursor = database.query(DB_TableName, new String[] { 
        		DBHelper.Column_ID, DBHelper.Column_TableName, DBHelper.Column_TableId,
        		DBHelper.Column_XMLData,DBHelper.Column_ModifyTime,DBHelper.Column_Type}, 
        		whereStr, null, null,  
                null, null);
        if (mCursor != null)
        	mCursor.moveToFirst();
        return mCursor;
    }  
	

		
}
