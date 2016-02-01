package com.elfec.ssc.model.exceptions;

import com.elfec.ssc.R;

/**
 * Esta excepci�n es lanzada cuando ha ocurrido una excepci�n no controlada en el lado del servidor
 * @author Diego
 *
 */
public class ServerSideException extends BaseApiException {

	/**
	 * Serial obligatorio 
	 */
	private static final long serialVersionUID = 8703127544410953147L;

	@Override
	public String getMessage()
	{
		return "Ocurri� un error en el servidor, porfavor intentelo nuevamente mas tarde";
	}

	@Override
	public int getMessageStringRes() {
		return R.string.server_side_exception;
	}
}
