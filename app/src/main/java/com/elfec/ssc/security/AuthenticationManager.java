package com.elfec.ssc.security;

import android.content.Context;

import org.joda.time.DateTime;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Se encarga de la autenticaci�n del dispositivo
 *
 * @author drodriguez
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
     *
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
     * Genera el nombre del archivo según la fecha
     *
     * @return nombre archivo salt
     */
    private String generateFileName() {
        try {
            DateTime dateTime = new DateTime(com.elfec.ssc.BuildConfig.buildTime);
            return String.format(fileNameFormat, dateTime.toString("yyMMdd"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
