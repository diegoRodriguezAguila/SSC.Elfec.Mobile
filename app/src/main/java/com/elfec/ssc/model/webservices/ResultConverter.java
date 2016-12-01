package com.elfec.ssc.model.webservices;

/**
 * Interfaz que sirve para convertir la cadena resultado de un servicio web
 * a un tipo específico
 * @author Diego
 *
 * @param <TResult>
 */
public interface ResultConverter<TResult>
{
	TResult convert(String result);
}
