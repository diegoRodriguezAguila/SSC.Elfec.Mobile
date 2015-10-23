package com.elfec.ssc.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * Se encarga de el hash SHA256
 * @author drodriguez
 *
 */
public class SHA256 {
	/**
	 * Obtiene el hash sha256 de la cadena provista
	 * @param str
	 * @return array de bytes equivalentes al hash
	 */
	public byte[] getHash(String str)
	{
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(str.getBytes("UTF-8"));
			byte[] digest = md.digest();
			return digest;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	/**
	 * Convierte un conjunto de bytes a una cadena hexadecimal
	 * @param bytes
	 * @return cadena hex
	 */
	public String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte byt : bytes) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }
}
