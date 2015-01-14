package com.elfec.ssc.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.elfec.ssc.helpers.threading.OnReleaseThread;

/**
 * Esta clase sirve para controlar el acceso a Client.getActiveClient() y porporciona métodos para
 * ocupar o liberar el recurso 
 * @author Diego
 *
 */
public class ThreadMutex {

	private static ConcurrentHashMap<String,ThreadMutex> instances;
	private boolean isFree = true;
	private List<OnReleaseThread> onReleaseEvents = new ArrayList<OnReleaseThread>();
	private ThreadMutex()
	{
		
	}
	
	static {
		instances=new ConcurrentHashMap<String,ThreadMutex>();
	}
	public static ThreadMutex instance(String key)
	{
		ThreadMutex mutex=instances.get(key);
		if(mutex==null)
		{
			mutex=new ThreadMutex();
			instances.put(key, mutex);
		}
		return mutex;
	}
	
	/**
	 * Indica si es que se puede utilizar el cliente activo actual
	 * @return
	 */
	public boolean isFree()
	{
		return isFree;
	}
	
	/**
	 * Asigna el estado de ocupado al cliente activo actual
	 */
	public void setBusy()
	{
		isFree = false;
	}
	
	/**
	 * Libera el recurso del cliente activo y ejecuta los eventos para notificar a
	 * aquellos que hayan estado esperando a que se libere siempre y cuando ninguno
	 * de ellos haya vuelto a ocupar el recurso
	 */
	public void setFree()
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
	public void addOnThreadReleasedEvent(OnReleaseThread onReleaseEvent)
	{
		onReleaseEvents.add(onReleaseEvent);
	}
}
