package com.neux.proj.insurance.jsparser;

import android.util.Log;

public class DBColumn 
{
	public String[] TableName;
	public int[] TableId;
	public String[] XMLData;
	public String[] Type;
	public String[] ModifyTime;
	public int count;
	
	public DBColumn()
	{
		this.count = 0;
	}
	
	public void SetArrayCount(int size)
	{
		TableName = new String[size];
		TableId = new int[size];
		XMLData = new String[size];
		Type = new String[size];
		ModifyTime = new String[size];
		this.count  = size;
	}
	
	public void SetArrayValue(int i,String TableName , int TableId , String XMLData , String Type , String ModifyTime)
	{
		this.TableName[i] = TableName;
		this.TableId[i] = TableId;
		this.XMLData[i] = XMLData;
		this.Type[i] = Type;
		this.ModifyTime[i] = ModifyTime;			
	}
	
	public void GetValue()
	{
		for (int i=0 ; i<= TableName.length-1 ; i++)
		{
			Log.e("DEBUG","TableName[" + i + "] = " + TableName[i]);
			Log.e("DEBUG","TableId[" + i + "] = " + TableId[i]);
			Log.e("DEBUG","XMLData[" + i + "] = " + XMLData[i]);
			Log.e("DEBUG","Type[" + i + "] = " + Type[i]);
			Log.e("DEBUG","ModifyTime[" + i + "] = " + ModifyTime[i]);
			
		}
	}
	
	public int GetCount()
	{
		return this.count;
	}
}
