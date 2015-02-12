package com.elfec.ssc.helpers;

public class TextFormater {

	public static String capitalize(String line)
	{
		StringBuilder result=new StringBuilder();
		String[] words=line.split(" ");
		for(String word:words)
		{
			if(word.length()>0)
			result.append(Character.toUpperCase(word.charAt(0))+word.substring(1).toLowerCase()+" ");
		}
		return result.toString();
	}
}
