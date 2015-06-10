package com.elfec.ssc.security;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

import com.elfec.ssc.model.security.WSCredential;
/**
 * Se encarga de la logica de generar credenciales de webservices
 * @author drodriguez
 *
 */
public class CredentialManager {
	
	private Context context;

	public CredentialManager(Context context) {
		this.context = context;
	}

	public WSCredential generateWSCredentials(){
		AuthenticationManager am = new AuthenticationManager(context);
		SignatureManager sm = new SignatureManager(context);
		return new WSCredential(getIMEI(), sm.getSignatureString(), am.getSalt(), getVersionCode());
	}
	
	/**
	 * Obtiene la versión de la aplicación
	 * @param context
	 * @return entero representando la version en el manifest
	 */
	private int getVersionCode(){
		try {
			PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pinfo.versionCode;
		} catch (NameNotFoundException e) {			
		}  
		return -1;
	}
	
	/**
	 * Obtiene la versión de la aplicación
	 * @param context
	 * @return entero representando la version en el manifest
	 */
	private String getIMEI(){
		return ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	}
}
