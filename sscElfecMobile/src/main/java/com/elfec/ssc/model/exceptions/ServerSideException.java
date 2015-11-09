package com.elfec.ssc.model.exceptions;

/**
 * Esta excepci�n es lanzada cuando ha ocurrido una excepci�n no controlada en el lado del servidor
 * @author Diego
 *
 */
public class ServerSideException extends Exception {

	/**
	 * Serial obligatorio 
	 */
	private static final long serialVersionUID = 8703127544410953147L;

	private String message;
	public ServerSideException(String message) {
		this.message = message;
	}
	public ServerSideException() {
		this.message  = "Ocurri� un error en el servidor, porfavor intentelo nuevamente mas tarde";
	}
	@Override
	public String getMessage()
	{
		return this.message;
	}

}
