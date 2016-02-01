package com.elfec.ssc.model.exceptions;

import com.elfec.ssc.R;

/**
 * Excepci�n que se lanza cuando el servidor esta
 * en mantenimiento o no es capaz de responder solicitudes
 */
public class ServerDownException extends BaseApiException {
    @Override
    public String getMessage()
    {
        return "El servidor está temporalmente fuera de servicio o se encuentra en mantenimiento." +
                " Por favor, inténtelo de nuevo más tarde, lamentamos las molestias.";
    }

    @Override
    public int getMessageStringRes() {
        return R.string.server_down_exception;
    }
}
