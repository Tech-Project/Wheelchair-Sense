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
		else if ("eq".equalsIgnoreCase(str))	// ���� equal
			mSymBolStr = "=";					
		else
			mSymBolStr = "";	// defual ����						
	}

	public String GetSymbol()
	{
		return this.mSymBolStr;
	}
}
