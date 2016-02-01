package com.elfec.ssc.model.exceptions;

import com.elfec.ssc.R;

public class OutdatedAppException extends BaseApiException {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = -7078227311059352234L;

	@Override
	public String getMessage() {
		return "No se pudo comunicar con los servicios debido a que la versi�n de la aplicaci�n " +
				"no es la m�s actual. Le recomendamos descargar la <b>�ltima versi�n</b> de la " +
				"playstore:  <a href=\"https://play.google.com/store/apps/details?id=com.elfec" +
				".ssc&hl=es-419/\">Obtener �ltima versi�n</a>.";
	}

	@Override
	public int getMessageStringRes() {
		return R.string.outdated_app_exception;
	}
}
