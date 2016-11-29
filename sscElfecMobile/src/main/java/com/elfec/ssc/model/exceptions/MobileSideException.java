package com.elfec.ssc.model.exceptions;

import com.elfec.ssc.R;

/**
 * Excepción que se lanza cuando ocurrió un error en el lado móvil
 * 
 * @author drodriguez
 *
 */
public class MobileSideException extends BaseApiException {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 3870416436055569657L;

	@Override
	public String getMessage() {
		return "Ocurrió un error al procesar la resupesta del servidor, disculpe las molestias,  " +
				"porfavor intentelo nuevamente mas tarde";
	}

	@Override
	public int getMessageStringRes() {
		return R.string.mobile_side_exception;
	}
}
