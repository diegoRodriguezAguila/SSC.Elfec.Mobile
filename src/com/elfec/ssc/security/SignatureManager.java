package com.elfec.ssc.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Base64;

/**
 * Se encarga del manejo de firmas de la aplicación
 * 
 * @author drodriguez
 *
 */
class SignatureManager {
	
	private Context context;	

	public SignatureManager(Context context) {
		super();
		this.context = context;
	}

	/**
	 * Obtiene la firma de la aplicación en cadena Base64
	 * 
	 * @param context
	 * @return
	 */
	String getSignatureString() {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				return Base64.encodeToString(md.digest(), Base64.DEFAULT).replace("\n", "");
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
