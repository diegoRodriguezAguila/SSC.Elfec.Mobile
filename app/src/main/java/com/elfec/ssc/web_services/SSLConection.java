package com.elfec.ssc.web_services;

import android.content.Context;
import android.util.Log;

import com.elfec.ssc.R;
import com.elfec.ssc.model.webservices.CompositeX509TrustManager;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.security.KeyStore;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Clase que permite la habilitación del certificado autofirmado de elfec
 *
 * @author drodriguez
 */
public class SslConection {

    private static WeakReference<Boolean> isAllowed;

    /**
     * Habilita el reconocimiento SSL del certificado auto firmado de Elfec,
     * añadiendolo a los TrustManager por defecto
     *
     * @param context context
     */
    public static void allowSelfSignedElfecSSL(Context context) {
        if (isAllowed!=null && isAllowed.get()!=null && isAllowed.get()) return;
        try {
            // Get an instance of the Bouncy Castle KeyStore format
            KeyStore trusted = KeyStore.getInstance("BKS");// put BKS literal
            // Get the raw resource, which contains the keystore with
            // your trusted certificates (root and any intermediate certs)
            InputStream caInput = context.getResources().openRawResource(
                    R.raw.ssc_elfec_keystore);
            try {
                trusted.load(caInput, "Elfec2015".toCharArray());

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    caInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(trusted);
            TrustManager[] trustManagers = tmf.getTrustManagers();

            tmf = TrustManagerFactory.getInstance("X509");
            tmf.init((KeyStore) null);
            TrustManager[] defaultTrustManagers = tmf.getTrustManagers();

            javax.net.ssl.SSLContext sslContext = javax.net.ssl.SSLContext
                    .getInstance("TLS");
            sslContext.init(null,
                    new TrustManager[]{new CompositeX509TrustManager(
                            getX509TrustManager(trustManagers),
                            getX509TrustManager(defaultTrustManagers))}, null);

            javax.net.ssl.HttpsURLConnection
                    .setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            isAllowed = new WeakReference<>(true);
        } catch (Exception e) {
            Log.e("allowSelfSignedElfecSSL", e.toString());
        }
    }

    /**
     * Obtiene el trustmanager de X509
     *
     * @param trustManagers trust managers
     * @return trust manager merged
     */
    private static X509TrustManager getX509TrustManager(
            TrustManager[] trustManagers) {
        for (TrustManager tm : trustManagers) {
            if (tm instanceof X509TrustManager) {
                return (X509TrustManager) tm;
            }
        }
        return null;
    }
}
