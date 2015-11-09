package com.elfec.ssc.security;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import org.joda.time.DateTime;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Se encarga de la autenticaci�n del dispositivo
 * @author drodriguez
 *
 */
class AuthenticationManager {
	/**
	 * Format para obtener el nombre del archivo
	 */
	private static final String fileNameFormat = "sk%s.ssc"; 
	private Context context;
	
	public AuthenticationManager(Context context) {
		this.context = context;
	}

	/**
	 * Obtiene salt que debe utilizarse para obtener un token
	 * @return salt
	 */
	String getSalt() {
		try {
			InputStream is = context.getAssets().open(generateFileName());
			DataInputStream dis = new DataInputStream(new BufferedInputStream(is));
			String salt = dis.readUTF();
			dis.close();
			SHA256 sha256 = new SHA256(); 
			return sha256.bytesToHex(sha256.getHash(salt));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Genera el nombre del archivo seg�n la fecha
	 * 
	 * @return nombre archivo salt
	 */
	private String generateFileName() {
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
			ZipFile zf = new ZipFile(ai.sourceDir);
			ZipEntry ze = zf.getEntry("classes.dex");
		    long time = ze.getTime();
			zf.close();
			return String.format(fileNameFormat, new DateTime(time).toString("yyMMdd"));
		} catch (NameNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
