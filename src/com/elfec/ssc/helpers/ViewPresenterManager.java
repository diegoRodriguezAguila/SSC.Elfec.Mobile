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
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getPresenter()
	{
		try
		{
			return (T) currentPresenter;
		}
		catch(ClassCastException e)
		{
			return null;
		}
	}
}
