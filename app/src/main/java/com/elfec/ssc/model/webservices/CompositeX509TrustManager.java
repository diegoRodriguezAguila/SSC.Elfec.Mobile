package com.elfec.ssc.model.webservices;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

/**
 * Represents an ordered list of {@link X509TrustManager}s with additive trust.
 * If any one of the composed managers trusts a certificate chain, then it is
 * trusted by the composite manager.
 * <p>
 * This is necessary because of the fine-print on {@link SSLContext#init}: Only
 * the first instance of a particular key and/or trust manager implementation
 * type in the array is used. (For example, only the first
 * javax.net.ssl.X509KeyManager in the array will be used.)
 *
 * @author codyaray
 * @since 4/22/2013
 * http://stackoverflow.com/questions/1793979/registering-multiple-keystores
 * -in-jvm
 */
public class CompositeX509TrustManager implements X509TrustManager {

    private final List<X509TrustManager> trustManagers;

    public CompositeX509TrustManager(X509TrustManager... trustManagers) {
        this.trustManagers = Arrays.asList(trustManagers);
    }

    public CompositeX509TrustManager(List<X509TrustManager> trustManagers) {
        this.trustManagers = trustManagers;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        for (X509TrustManager trustManager : trustManagers) {
            try {
                trustManager.checkClientTrusted(chain, authType);
                return; // someone trusts them. success!
            } catch (CertificateException e) {
                // maybe someone else will trust them
            }
        }
        throw new CertificateException(
                "None of the TrustManagers trust this certificate chain");
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        for (X509TrustManager trustManager : trustManagers) {
            try {
                trustManager.checkServerTrusted(chain, authType);
                return; // someone trusts them. success!
            } catch (CertificateException e) {
                // maybe someone else will trust them
            }
        }
        throw new CertificateException(
                "None of the TrustManagers trust this certificate chain");
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        List<X509Certificate> certificates = new ArrayList<>();
        for (X509TrustManager trustManager : trustManagers) {
            Collections.addAll(certificates, trustManager.getAcceptedIssuers());
        }
        return certificates.toArray(new X509Certificate[certificates.size()]);
    }

}