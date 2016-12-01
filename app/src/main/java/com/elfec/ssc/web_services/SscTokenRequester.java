package com.elfec.ssc.web_services;

import com.elfec.ssc.model.events.SscTokenReceivedCallback;
import com.elfec.ssc.model.security.SscToken;
import com.elfec.ssc.model.webservices.WSResponse;
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
     * Obtiene el token del dispositivo de forma asincrona, en caso de que el
     * token ya se tuviera guardado en los shared preferences directamente se
     * llama al <b>callback</b>, cuando se obtiene el token se lo guarda
     * automáticamente en las shared preferences
     *
     * @param callback callback
     */
    public void getTokenAsync(final SscTokenReceivedCallback callback) {
        if (currentToken != null && callback != null)
            callback.onSscTokenReceived(new WSResponse<SscToken>().setResult(currentToken));
        else {
            CredentialManager credentialManager = new CredentialManager(AppPreferences
                    .getApplicationContext());
            TokenService tokenWS = new TokenService(credentialManager.generateSscCredentials());
            tokenWS.requestSscToken(result -> {
                if (!result.hasErrors() && result.getResult() != null)
                    preferences.setSscToken(result.getResult());
                if (callback != null)
                    callback.onSscTokenReceived(result);
            });
        }
    }

    /**
     * Obtiene el token del dispositivo de forma asincrona, en caso de que el
     * token ya se tuviera guardado en los shared preferences directamente se retorna el valor
     * cuando se obtiene el token se lo guarda
     * automáticamente en shared preferences
     *
     * @return observable de token
     */
    public Observable<SscToken> getSscToken() {
        if (currentToken != null)
            return Observable.just(currentToken);
        return new TokenService(new CredentialManager(AppPreferences
                .getApplicationContext())
                .generateSscCredentials()).requestSscToken();
    }
}
