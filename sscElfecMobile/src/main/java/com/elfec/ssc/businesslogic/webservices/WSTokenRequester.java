package com.elfec.ssc.businesslogic.webservices;

import android.content.Context;

import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.events.WSTokenReceivedCallback;
import com.elfec.ssc.model.security.WSToken;
import com.elfec.ssc.model.webservices.WSResponse;
import com.elfec.ssc.security.CredentialManager;
import com.elfec.ssc.security.PreferencesManager;

/**
 * Clase cuyo objetivo es obtener el WS token del dispositivo
 * 
 * @author drodriguez
 *
 */
public class WSTokenRequester {
	private Context context;
	private PreferencesManager preferences;
	private WSToken currentTokenOnPreferences;

	public WSTokenRequester(Context context) {
		this.context = context;
		this.preferences = new PreferencesManager(context);
		this.currentTokenOnPreferences = preferences.getWSToken();
	}

	/**
	 * Obtiene el token del dispositivo de forma asincrona, en caso de que el
	 * token ya se tuviera guardado en los shared preferences directamente se
	 * llama al <b>callback</b>, cuando se obtiene el token se lo guarda
	 * automáticamente en las shared preferences
	 * 
	 * @param callback
	 */
	public void getTokenAsync(final WSTokenReceivedCallback callback) {
		if (currentTokenOnPreferences != null && callback!=null)
			callback.onWSTokenReceived(new WSResponse<WSToken>().setResult(currentTokenOnPreferences));
		else {
			CredentialManager credentialManager = new CredentialManager(context);
			TokenWS tokenWS = new TokenWS(credentialManager.generateWSCredentials());
			tokenWS.requestWSToken(new IWSFinishEvent<WSToken>() {
				@Override
				public void executeOnFinished(WSResponse<WSToken> result) {
					if (!result.hasErrors() && result.getResult() != null)
						preferences.setWSToken(result.getResult());
					if (callback != null)
						callback.onWSTokenReceived(result);
				}
			});
		}
	}
}
