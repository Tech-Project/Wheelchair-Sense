package com.neux.proj.insurance.dbsetget;

import com.neux.proj.insurance.db.DBHelper;
import com.neux.proj.insurance.db.DBHelper;


public class ConditionColumn 
{
	public String ConditionKey = null;
	public String ConditionSymbol = null;
	public String ConditionValue = "";
	public String ConbineQueryStr = null;
	
	public void SetQueryStr()
	{
		this.ConbineQueryStr = " " + this.ConditionKey + this.ConditionSymbol + "'" + this.ConditionValue + "'";		
	}
	
	public boolean CheckRule()
	{
		boolean flag = false;
		if (
		     (DBHelper.Column_TableName.equals(this.ConditionKey)) ||
		     (DBHelper.Column_TableId.equals(this.ConditionKey)) ||
		     (DBHelper.Column_XMLData.equals(this.ConditionKey)) ||
		     (DBHelper.Column_ModifyTime.equals(this.ConditionKey)) ||
		     (DBHelper.Column_Type.equals(this.ConditionKey))
		   )
		{
			flag = true;
		}
		
		if ("".equals(this.ConditionSymbol))
			flag = false;
		
		return flag;
	}
	
}


