package com.elfec.ssc.helpers;

import java.util.ArrayList;
import java.util.List;

import com.elfec.ssc.helpers.threading.OnReleaseThread;

/**
 * Esta clase sirve para controlar el acceso a Client.getActiveClient() y porporciona métodos para
 * ocupar o liberar el recurso 
 * @author Diego
 *
 */
public class ActiveClientThreadMutex {

	private static boolean isFree = true;
	private static List<OnReleaseThread> onReleaseEvents = new ArrayList<OnReleaseThread>();
	
	/**
	 * Indica si es que se puede utilizar el cliente activo actual
	 * @return
	 */
	public static boolean isFree()
	{
		return isFree;
	}
	
	/**
	 * Asigna el estado de ocupado al cliente activo actual
	 */
	public static void setBusy()
	{
		isFree = false;
	}
	
	/**
	 * Libera el recurso del cliente activo y ejecuta los eventos para notificar a
	 * aquellos que hayan estado esperando a que se libere siempre y cuando ninguno
	 * de ellos haya vuelto a ocupar el recurso
	 */
	public static void setFree()
	{
		isFree = true;
		for(OnReleaseThread onReleased : onReleaseEvents)
		{
			if(!isFree)
				return;
			onReleased.threadReleased();
		}
		onReleaseEvents.clear();
	}
	
	/**
	 * Añade un evento a la lista de eventos que se ejecutaran en cuanto se libere el hilo que 
	 * ocupa el recurso del cliente activo
	 * @param onReleaseEvent
	 */
	public static void addOnThreadReleasedEvent(OnReleaseThread onReleaseEvent)
	{
		onReleaseEvents.add(onReleaseEvent);
	}
}
