package com.elfec.ssc.model.security;
/**
 * Almacena todos los credenciales necesarios
 * para obtener un token de ws
 * @author drodriguez
 *
 */
public class SscCredential {
	private String imei;
	private String signature;
	private String salt;
	private int versionCode;
	
	public SscCredential(String imei, String signature, String salt,
						 int versionCode) {
		this.imei = imei;
		this.signature = signature;
		this.salt = salt;
		this.versionCode = versionCode;
	}

	public String getImei() {
		return imei;
	}

	public String getSignature() {
		return signature;
	}

	public String getSalt() {
		return salt;
	}

	public int getVersionCode() {
		return versionCode;
	}
	
}
