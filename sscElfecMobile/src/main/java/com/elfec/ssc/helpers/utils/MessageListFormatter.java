package com.elfec.ssc.helpers.utils;

import android.text.Html;
import android.text.Spanned;

import java.util.List;

/**
 * Formatea las listas de errores
 * @author drodriguez
 *
 */
public class MessageListFormatter {

	/**
	 * Formatea una lista de errores una lista de errores en html
	 * @param errors errores
	 * @return mensaje formateado en html
	 */
	public static Spanned fotmatHTMLFromErrors(List<Exception> errors)
	{
		StringBuilder str = new StringBuilder();
		int size = errors.size();
		if(size==1)
			return Html.fromHtml(str.append(errors.get(0).getMessage()).toString());
		for (int i = 0; i < size; i++) {
			str.append("\u25CF ").append(errors.get(i).getMessage());
			str.append((i<size-1?"<br/>":""));
		}
		return Html.fromHtml(str.toString());
	}
	
	/**
	 * Formatea una lista de objetos una lista (en cadena) en html
	 * @param objects lista de objetos
	 * @return mensaje formateado en html
	 */
	public static <T> String fotmatHTMLStringFromObjectList(List<T> objects, AttributePicker<String, T> attributePicker)
	{
		StringBuilder str = new StringBuilder();
		int size = objects.size();
		if(size==1)
			return str.append(attributePicker.pickAttribute(objects.get(0))).toString();
		for (int i = 0; i < size; i++) {
			str.append("\u25CF ").append(attributePicker.pickAttribute(objects.get(i)));
			str.append((i<size-1?"<br/>":""));
		}
		return str.toString();
	}
	
	/**
	 * Formatea una lista de mensajes una lista (en cadena) en html
	 * @param messages lista de cadenas
	 * @return mensaje formateado en html
	 */
	public static Spanned fotmatHTMLFromStringList(List<String> messages)
	{
		StringBuilder str = new StringBuilder();
		int size = messages.size();
		if(size==1)
			return Html.fromHtml(str.append(messages.get(0)).toString());
		for (int i = 0; i < size; i++) {
			str.append("\u25CF ").append(messages.get(i)).append((i<size-1)?"<br/>":"");
		}
		return Html.fromHtml(str.toString());
	}
}
