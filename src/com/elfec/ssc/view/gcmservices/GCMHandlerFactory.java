package com.elfec.ssc.view.gcmservices;

import java.util.Hashtable;

/**
 * Factory para crear GCM handlers según su key
 * @author Diego
 *
 */
public class GCMHandlerFactory {
	private static Hashtable<String,Class<? extends IGCMHandler>> gcmHandlers = new Hashtable<String, Class<? extends IGCMHandler>>();
	static
	{
		gcmHandlers.put("NewAccount", NewAccountGCMHandler.class);
		gcmHandlers.put("AccountDeleted", AccountDeletedGCMHandler.class);
	}
	
	/**
	 * Obtiene el GCMHandler para la key dada, si no existe una clase con la key retorna null
	 * @param key
	 * @return
	 */
	public static IGCMHandler getGCMHandler(String key)
	{
		try {
			return gcmHandlers.get(key).newInstance();
		} 
		catch (InstantiationException e) {}
		catch (IllegalAccessException e) {}
		catch(NullPointerException e) {}
		return null;
	}
}
