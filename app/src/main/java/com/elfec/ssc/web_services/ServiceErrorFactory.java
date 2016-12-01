package com.elfec.ssc.web_services;

import com.elfec.ssc.model.exceptions.ServerSideException;
import com.google.gson.JsonParseException;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.zip.DataFormatException;


/**
 * Interpreta los errores que pudieron haber ocurrido al
 * contactar con un servicio web
 */
public class ServiceErrorFactory {
    @SuppressWarnings("ThrowableInstanceNeverThrown")
    public static final Exception DEFAULT_ERROR =
            new Exception("Ocurrió un error inesperado al conectarse al servicio, " +
                    "lamentamos las molestias, inténtelo de nuevo mas tarde.");

    /**
     * Interpreta un throwable a una excepción
     *
     * @param throwable throwable
     * @return excepción correspondiente al error recibido
     */
    public static Throwable fromThrowable(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof JsonParseException)
            return new DataFormatException("La información recibida del servidor no es válida, " +
                    "detalles: " + throwable.getMessage());
        if (throwable instanceof ConnectException)
            return new ConnectException("No fue posible conectarse con el servidor, " +
                    "porfavor revise su conexión a internet");
        if (throwable instanceof SocketTimeoutException)
            return new SocketTimeoutException("No fue posible conectarse con el servidor, " +
                    "puede que el servidor no se encuentre disponible temporalmente, " +
                    "porfavor verifique su conexión a internet");
        if (throwable instanceof IOException)
            return new ConnectException(
                    "Error al conectarse con el servidor, porfavor revise " +
                            "su conexión a internet");
        if (throwable instanceof XmlPullParserException)
            return new ServerSideException();
        return DEFAULT_ERROR;
    }
}
