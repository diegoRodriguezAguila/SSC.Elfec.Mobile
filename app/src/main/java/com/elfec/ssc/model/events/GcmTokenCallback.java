package com.elfec.ssc.model.events;

import java.util.List;

/**
 * Callback para la invocación de token de google GCM
 */
public interface GcmTokenCallback {
    /**
     * Se llama cuando se obtiene el gcmToken exitosamente, nunca es nulo, si fuera nulo no se llama
     * se llama encambio a {@link #onGcmErrors(List)}
     * @param gcmToken gcm token
     */
	void onGcmTokenReceived(String gcmToken);

    /**
     * Se llama cuando ocurrieron errores al obtener el token de google GCM
     * @param errors errores
     */
    void onGcmErrors(List<Exception> errors);
}
