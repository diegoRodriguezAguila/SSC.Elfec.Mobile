package com.elfec.ssc.model.exceptions;

/**
 * Excepci�n que se lanza cuando el servidor esta
 * en mantenimiento o no es capaz de responder solicitudes
 */
public class ServerDownException extends Exception {
    @Override
    public String getMessage()
    {
        return "El servidor est� temporalmente fuera de servicio o se encuentra en mantenimiento." +
                " Por favor, int�ntelo de nuevo m�s tarde, lamentamos las molestias.";
    }
}
