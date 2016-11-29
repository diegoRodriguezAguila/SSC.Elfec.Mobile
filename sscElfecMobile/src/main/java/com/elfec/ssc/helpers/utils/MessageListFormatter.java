package com.elfec.ssc.helpers.utils;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;

import com.elfec.ssc.model.exceptions.BaseApiException;

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
	public static Spanned formatHTMLFromErrors(final Context context, List<Exception> errors)
	{
        return formatHTMLFromObjectList(errors, new AttributePicker<String, Exception>() {
            @Override
            public String pickAttribute(Exception error) {
                return error instanceof BaseApiException? context.getResources()
                        .getString(((BaseApiException) error).getMessageStringRes())
                        :error.getMessage();
            }
        });
	}
	
	/**
	 * Formatea una lista de objetos una lista (en cadena) en html
	 * @param objects lista de objetos
	 * @return mensaje formateado en html
	 */
	public static <T> Spanned formatHTMLFromObjectList(List<T> objects, AttributePicker<String, T> attributePicker)
	{
		StringBuilder str = new StringBuilder();
		int size = objects.size();
		if(size==1)
			return Html.fromHtml(str.append(attributePicker.pickAttribute(objects.get(0))).toString());
		for (int i = 0; i < size; i++) {
			str.append("\u25CF ").append(attributePicker.pickAttribute(objects.get(i)));
			str.append((i<size-1?"<br/>":""));
		}
		return Html.fromHtml(str.toString());
	}
	
	/**
	 * Formatea una lista de mensajes una lista (en cadena) en html
	 * @param messages lista de cadenas
	 * @return mensaje formateado en html
	 */
	public static Spanned formatHTMLFromStringList(List<String> messages)
	{
        return formatHTMLFromObjectList(messages, new AttributePicker<String, String>() {
            @Override
            public String pickAttribute(String str) {
                return str;
            }
        });
	}
}
