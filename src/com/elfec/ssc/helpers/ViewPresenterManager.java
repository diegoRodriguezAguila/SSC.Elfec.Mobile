package com.elfec.ssc.helpers;

/**
 * Esta clase se encarga de guardar el presenter actual para ser accedido por las notificaciones
 * el momento en que llegan
 * @author Diego
 *
 */
public class ViewPresenterManager {

	private static Object currentPresenter;
	
	/**
	 * Setea el presenter actual
	 * @param presenter
	 */
	public static <T> void setPresenter(T presenter)
	{
		currentPresenter =  presenter;
	}
	
	/**
	 * obtiene el presenter actual
	 * @param type
	 * @return
	 */
	public static <T> T getPresenter(Class<T> type)
	{
		try
		{
			return type.cast(currentPresenter);
		}
		catch(ClassCastException e)
		{
			return null;
		}
	}
}
