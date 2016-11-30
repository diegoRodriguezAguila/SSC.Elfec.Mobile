package com.elfec.ssc.model.exceptions;

import android.support.annotation.StringRes;

/**
 * Excepción bósica de la api, provee de internacionalización para los mensajes
 */
public abstract class BaseApiException extends Exception {

    @StringRes
    public abstract int getMessageStringRes();
}
