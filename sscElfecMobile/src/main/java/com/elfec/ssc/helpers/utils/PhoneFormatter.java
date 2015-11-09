package com.elfec.ssc.helpers.utils;

import java.security.InvalidParameterException;

/**
 * Clase que con métodos para formatear numéros de teléfono
 * @author Diego
 *
 */
public class PhoneFormatter {

	/**
	 * Formatea el numero de teléfono con espacios
	 * @param phone
	 * @return phone con espacios
	 */
	public static String formatPhone(String phone)
	{
		StringBuilder str = new StringBuilder(phone);
		int length = str.length();
		String ending = "";
		if(length>=9)
			return format9DigitsPhone(phone);
		if(length>4)
		{
			ending = str.substring(length-4, length);
			str = new StringBuilder(str.substring(0, length-4));
		}
		str = str.reverse();
		int times = (str.length()-1)/3;
		for (int i = 0; i <= times; i++)
		{
			str.insert(((i*3)+i), " ");
		}
		return str.reverse().append(ending).toString();
	}
	
	/**
	 * Formatea un número de teléfono que tenga 9 dígitos o más
	 * @param phone
	 * @return
	 */
	public static String format9DigitsPhone(String phone)
	{
		StringBuilder str = new StringBuilder(phone);
		int length = str.length();
		if(length < 9)
			throw new InvalidParameterException("El teléfono proporcionado tiene que tener al menos 9 dígitos");
		String ending = str.substring(length-4, length);
		String start = str.substring(0, 3);
		String middle = str.substring(3, length-4);
		return start + " " + middle + " " + ending;
	}
}
