package com.elfec.ssc.model.exceptions;

/**
 * Se lanza cuando una clase que requeria inicializaci�n se utiliza en su estado
 * actual
 * 
 * @author drodriguez
 *
 */
public class InitializationException extends RuntimeException {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = -5084877272342523516L;
	private String className;

    /**
     * Inicializa la excepci�n con el nombre de la clase que no fue inicializada
     * @param mClass clase que no fue inicializada correctamente
     */
    public InitializationException(Class<?> mClass) {
        this.className = mClass.getName();
    }

    @Override
	public String getMessage() {
		return "La clase "+className+"no se inicializ� correctamente antes de utilizarse";
	}

}
