package com.elfec.ssc.model.exceptions;

import com.elfec.ssc.R;

/**
 * Excepci�n que se lanza cuando ocurri� un error en el lado m�vil
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
		return "Ocurri� un error al procesar la resupesta del servidor, disculpe las molestias,  " +
				"porfavor intentelo nuevamente mas tarde";
	}

	@Override
	public int getMessageStringRes() {
		return R.string.mobile_side_exception;
	}
}
