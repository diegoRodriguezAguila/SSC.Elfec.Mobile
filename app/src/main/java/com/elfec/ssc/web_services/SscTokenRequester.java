package com.elfec.ssc.web_services;

import com.elfec.ssc.model.security.SscToken;
import com.elfec.ssc.security.AppPreferences;
import com.elfec.ssc.security.CredentialManager;

import rx.Observable;

/**
 * Clase cuyo objetivo es obtener el WS token del dispositivo
 *
 * @author drodriguez
 */
public class SscTokenRequester {
    private AppPreferences preferences;
    private SscToken currentToken;

    public SscTokenRequester() {
        this.preferences = AppPreferences.instance();
        this.currentToken = preferences.getSscToken();
    }

    /**
     * Obtiene el token del dispositivoa, en caso de que el
     * token ya se tuviera guardado en los shared preferences directamente se retorna el valor,
     * caso contrario lo obtiene de los web services. Una vez se obtiene el token se lo guarda
     * automáticamente en shared preferences
     *
     * @return observable de token
     */
    public Observable<SscToken> getSscToken() {
        if (currentToken != null)
            return Observable.just(currentToken);
        return new TokenService(new CredentialManager(AppPreferences
                .getApplicationContext())
                .generateSscCredentials()).requestSscToken()
                .map(sscToken -> {
                    AppPreferences.instance().setSscToken(sscToken);
                    return sscToken;
                });
    }
}
