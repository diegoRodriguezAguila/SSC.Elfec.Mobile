package com.elfec.ssc.model.exceptions;

import com.elfec.ssc.R;

/**
 * Excepci�n que se lanza cuando no se pudo obtener el token de conecci�n de google
 */
public class GcmConnectException extends BaseApiException{
    @Override
    public String getMessage() {
        return "No fue posible conectarse con el servidor, porfavor revise su conexi�n a internet" +
                ".<br/>Si su conexi�n a internet es correcta, porfavor verifique que tiene " +
                "<a href=\"https://play.google.com/store/apps/details?id=com.google.android.gms&hl=es_419/\">" +
                "Google Play Services</a> instalado.";
    }

    @Override
    public int getMessageStringRes() {
        return R.string.gcm_connect_exception;
    }
}
