package com.elfec.ssc.security;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.elfec.ssc.model.security.SscCredential;

/**
 * Se encarga de la logica de generar credenciales de webservices
 *
 * @author drodriguez
 */
public class CredentialManager {

    private Context context;

    public CredentialManager(Context context) {
        this.context = context;
    }

    /**
     * Genera los credenciales necesarios para realizar la autenticación del usuario
     * y dispositivo con la API del servidor
     * @return credenciales
     */
    public SscCredential generateSscCredentials() {
        AuthenticationManager am = new AuthenticationManager(context);
        SignatureManager sm = new SignatureManager(context);
        return new SscCredential(getDeviceIdentifier(), sm.getSignatureString(), am.getSalt(), getVersionCode());
    }

    /**
     * Obtiene la versión de la aplicación
     *
     * @return entero representando la version en el manifest
     */
    private int getVersionCode() {
        try {
            PackageInfo packInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (NameNotFoundException ignored) {
        }
        return -1;
    }

    /**
     * Obtiene un identificador unico para el dispositivo
     * En la mayoría de dispositivos este es el IMEI, pero
     * en aquellos dispositivos sin chip se ocupa el android Id
     *
     * @return Identificador para el dispositivo
     */
    public String getDeviceIdentifier() {
        String imei = getImei();
        if (imei == null)
            return getAndroidId();
        return imei;
    }

    /**
     * Obtiene la versión de la aplicación
     *
     * @return entero representando la version en el manifest
     */
    private String getImei() {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
                .getDeviceId();
    }

    /**
     * Obtiene el Android Id del dispositivo
     *
     * @return Android Id
     */
    private String getAndroidId() {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
}
