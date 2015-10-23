package com.elfec.ssc.helpers;

public class DownloadHelper {

	private static Object AccountsUpdated=null; 
	public static boolean isAccountsUpdated()
	{
		boolean response=false;
		if(AccountsUpdated!=null)
			AccountsUpdated=new Object();
		else
		{
			response=true;
		}
		return response;
	}
}
