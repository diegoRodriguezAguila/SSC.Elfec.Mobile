package com.elfec.ssc.model.exceptions;

/**
 * Excepción que se lanza cuando el servidor esta
 * en mantenimiento o no es capaz de responder solicitudes
 */
public class ServerDownException extends Exception {
    @Override
    public String getMessage()
    {
        return "El servidor está temporalmente fuera de servicio o se encuentra en mantenimiento." +
                " Por favor, inténtelo de nuevo más tarde, lamentamos las molestias.";
    }
}
