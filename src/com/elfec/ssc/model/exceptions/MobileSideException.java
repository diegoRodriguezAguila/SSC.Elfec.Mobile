package com.elfec.ssc.model.exceptions;

/**
 * Excepción que se lanza cuando ocurrió un error en el lado móvil
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
		this.message = "Ocurrió un error al procesar la resupesta del servidor, disculpe las molestias, porfavor intentelo nuevamente mas tarde";
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}
