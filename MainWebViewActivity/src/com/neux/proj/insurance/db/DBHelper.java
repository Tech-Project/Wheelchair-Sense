package com.neux.proj.insurance.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper 
{

	private final static int _DBVersion = 1;	
	private final static String _DBName = "neux.db";
	
	// *** Table *** //
	public final static String _TableName = "Calories";
	
	// *** Column *** //
	public final static String Column_ID = "_id";	// Android Default
	public final static String Column_TableName = "_table_name";
	public final static String Column_TableId = "_table_id";
	public final static String Column_XMLData = "_xml_data";
	public final static String Column_ModifyTime = "_modify_time";
	public final static String Column_Type = "_type";
	
	
	
	
		
	
	public DBHelper(Context context) 
	{		
		super(context, _DBName, null, _DBVersion);	
	}
	
	public DBHelper(Context context, String name, CursorFactory factory,int version) 
	{
		super(context, name, factory, version);		
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		Log.e("DEBUG","DB onCreate !");
		
		final String SQL = "CREATE TABLE IF NOT EXISTS " + _TableName + "( " +				
				this.Column_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +				
				this.Column_TableName + " VARCHAR(300), " +				
				this.Column_TableId + " VARCHAR(300)," +
				this.Column_XMLData + " VARCHAR(2048)," +
				this.Column_ModifyTime + " VARCHAR(14)," +
				this.Column_Type + " VARCHAR(1)" +
				");";				
				db.execSQL(SQL);		
	}

			
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{		
		Log.e("DEBUG","DB oldVersion = " + oldVersion);
		Log.e("DEBUG","DB newVersion = " + newVersion);
		
	//	String SQL = "DROP TABLE IF EXISTS " + _TableName;				
	//	db.execSQL(SQL); 
		
		/*
		if (newVersion > oldVersion) 
		{
			db.beginTransaction();//建立交易			     
			boolean success = false;//判斷參數			        
			//由之前不用的版本，可做不同的動作     
			switch (oldVersion) 
			{
				case 1:           
					db.execSQL("ALTER TABLE newMemorandum ADD COLUMN reminder integer DEFAULT 0");
			    	db.execSQL("ALTER TABLE newMemorandum ADD COLUMN type VARCHAR");
			    	db.execSQL("ALTER TABLE newMemorandum ADD COLUMN memo VARCHAR");
			    	oldVersion++;			             
			    	success = true;
			    	break;
			}			                
			if (success)
				db.setTransactionSuccessful();//正確交易才成功
			      
			db.endTransaction();
		}
		else 
		{
			    onCreate(db);
		} 
		*/  
		
	}

}
