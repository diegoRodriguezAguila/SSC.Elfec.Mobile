package com.elfec.ssc.model.exceptions;

/**
 * Excepci�n que se lanza cuando ocurri� un error en el lado m�vil
 * 
 * @author drodriguez
 *
 */
public class MobileSideException extends Exception {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 3870416436055569657L;
	private String message;

	public MobileSideException(String message) {
		this.message = message;
	}

	public MobileSideException() {
		this.message = "Ocurri� un error al procesar la resupesta del servidor, disculpe las molestias, porfavor intentelo nuevamente mas tarde";
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}
