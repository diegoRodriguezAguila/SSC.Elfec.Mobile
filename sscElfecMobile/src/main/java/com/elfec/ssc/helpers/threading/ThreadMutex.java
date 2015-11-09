package com.elfec.ssc.helpers.threading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Esta clase sirve coordinar el acceso a recursos entre hilos
 * organizados segun una clave (key)
 * @author Diego
 *
 */
public class ThreadMutex {

	private static ConcurrentHashMap<String,ThreadMutex> instances;
	private boolean isFree = true;
	private String keyOnDictionary;
	private List<OnReleaseThread> onReleaseEvents = new ArrayList<OnReleaseThread>();
	
	/**
	 * Constructor privado, solo se puede instanciar con el metodo <b>instance(key)</b>
	 */
	private ThreadMutex(String keyOnDictionary)
	{	
		this.keyOnDictionary = keyOnDictionary;
	}
	
	static {
		instances=new ConcurrentHashMap<String,ThreadMutex>();
	}
	/**
	 * Devuelve la instancia actual de un mutex segun su key
	 * @param key
	 * @return
	 */
	public static ThreadMutex instance(String key)
	{
		ThreadMutex mutex=instances.get(key);
		if(mutex==null)
		{
			mutex=new ThreadMutex(key);
			instances.put(key, mutex);
		}
		return mutex;
	}
	
	/**
	 * Indica si es que se puede utilizar el recurso compartido, si es que el hilo está libre
	 * @return
	 */
	public boolean isFree()
	{
		return isFree;
	}
	
	/**
	 * Asigna el estado de ocupado al recurso compartido
	 */
	public void setBusy()
	{
		isFree = false;
	}
	
	/**
	 * Libera el recurso compartido y ejecuta los eventos para notificar a
	 * aquellos que hayan estado esperando a que se libere siempre y cuando ninguno
	 * de ellos haya vuelto a ocuparlo
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
	 * Elimina el mutex del diccionario para liberar memoria
	 */
	public void dispose()
	{
		ThreadMutex.instances.remove(keyOnDictionary);
	}
	
	/**
	 * Añade un evento a la lista de eventos que se ejecutaran en cuanto se libere el hilo que 
	 * ocupa un recurso compartido, si el hilo ya se encuentra libre lo ejecuta directamente
	 * @param onReleaseEvent
	 */
	public void addOnThreadReleasedEvent(OnReleaseThread onReleaseEvent)
	{
		if(isFree)
			onReleaseEvent.threadReleased();
		else onReleaseEvents.add(onReleaseEvent);
	}
}
