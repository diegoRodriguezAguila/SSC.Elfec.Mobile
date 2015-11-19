package com.elfec.ssc.model.exceptions;

/**
 * Excepción que se lanza cuando no se pudo obtener el token de conección de google
 */
public class GcmConnectException extends Exception{
    @Override
    public String getMessage() {
        return "No fue posible conectarse con el servidor, porfavor revise su conexión a internet" +
                ".<br/>Si su conexión a internet es correcta, porfavor verifique que tiene " +
                "<a href=\"https://play.google.com/store/apps/details?id=com.google.android.gms&hl=es_419/\">" +
                "Google Play Services</a> instalado.";
    }

}
