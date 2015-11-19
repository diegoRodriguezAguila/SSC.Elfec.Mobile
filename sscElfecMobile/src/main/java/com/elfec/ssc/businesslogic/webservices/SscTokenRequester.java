package com.elfec.ssc.businesslogic.webservices;

import android.content.Context;

import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.events.SscTokenReceivedCallback;
import com.elfec.ssc.model.security.SscToken;
import com.elfec.ssc.model.webservices.WSResponse;
import com.elfec.ssc.security.CredentialManager;
import com.elfec.ssc.security.AppPreferences;

/**
 * Clase cuyo objetivo es obtener el WS token del dispositivo
 * 
 * @author drodriguez
 *
 */
public class SscTokenRequester {
	private Context context;
	private AppPreferences preferences;
	private SscToken currentTokenOnPreferences;

	public SscTokenRequester(Context context) {
		this.context = context;
		this.preferences = AppPreferences.instance();
		this.currentTokenOnPreferences = preferences.getWSToken();
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
		if (currentTokenOnPreferences != null && callback!=null)
			callback.onSscTokenReceived(new WSResponse<SscToken>().setResult(currentTokenOnPreferences));
		else {
			CredentialManager credentialManager = new CredentialManager(context);
			TokenWS tokenWS = new TokenWS(credentialManager.generateSscCredentials());
			tokenWS.requestSscToken(new IWSFinishEvent<SscToken>() {
				@Override
				public void executeOnFinished(WSResponse<SscToken> result) {
					if (!result.hasErrors() && result.getResult() != null)
						preferences.setWSToken(result.getResult());
					if (callback != null)
						callback.onSscTokenReceived(result);
				}
			});
		}
	}
}
