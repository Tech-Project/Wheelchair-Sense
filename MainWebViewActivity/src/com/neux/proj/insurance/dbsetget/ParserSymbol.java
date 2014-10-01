package com.neux.proj.insurance.dbsetget;

public class ParserSymbol 
{
	public String mSymBolStr;	
	public ParserSymbol(String str)
	{
		if ("gt".equalsIgnoreCase(str))	
			mSymBolStr = ">";
		else if ("lt".equalsIgnoreCase(str))
			mSymBolStr = "<";
		else if ("ge".equalsIgnoreCase(str))
			mSymBolStr = ">=";
		else if ("le".equalsIgnoreCase(str))
			mSymBolStr = "<=";
		else if ("eq".equalsIgnoreCase(str))	// 等於 equal
			mSymBolStr = "=";					
		else
			mSymBolStr = "";	// defual 等於						
	}

	public String GetSymbol()
	{
		return this.mSymBolStr;
	}
}
