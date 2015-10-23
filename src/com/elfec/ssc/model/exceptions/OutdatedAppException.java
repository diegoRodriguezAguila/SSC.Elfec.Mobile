package com.elfec.ssc.model.exceptions;

public class OutdatedAppException extends Exception {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = -7078227311059352234L;
	
	@Override
	public String getMessage()
	{
		return "No se pudo comunicar con los servicios debido a que la versión de la aplicación no es la más actual. "
				+ "Le recomendamos descargar la <b>última versión</b> de la playstore: <a href=\"https://play.google.com/\">Obtener última versión</a>.";
	}
}
