package com.elfec.ssc.web_services;

import android.util.Log;

import com.elfec.ssc.BuildConfig;
import com.elfec.ssc.helpers.utils.GsonUtils;
import com.elfec.ssc.model.security.SscToken;
import com.elfec.ssc.model.webservices.DataResult;
import com.elfec.ssc.model.webservices.DataResultParametrizedType;
import com.elfec.ssc.model.webservices.MarshalDouble;
import com.elfec.ssc.model.webservices.WSParam;
import com.elfec.ssc.security.AppPreferences;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsTransportSE;
import org.ksoap2.transport.ServiceConnection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;

/**
 * General object for web service connection.
 * This is an abstract class so it must be called with braces at the end:<br>
 * <code>new ServiceConnector(&hellip;){}</code>
 *
 * @param <T>
 * @author Diego
 */
public abstract class ServiceConnector<T> {
    public static final String TAG = "ServiceConnector";
    private static final String WS_SERVER = BuildConfig.WS_SERVER_URL;
    private static final String SOAP_ACTION = "";
    private static final String NAMESPACE = "ssc_elfec";
    private static final int TIMEOUT = 30000;

    private String url;
    private String methodName;
    private SscToken sscToken;
    private Type type;
    private transient HttpTransportSE ht;
    private transient boolean isUnsubscribed;

    /**
     * Construye un conector de webservice soap con los parámetros indicados y
     * con autenticación por sscToken.
     * This is an abstract class so it must be called with braces at the end:<br>
     * <code>new ServiceConnector(&hellip;){}</code>
     *
     * @param endpoint   endpoint
     * @param methodName soap method
     * @param sscToken   ssc token
     */
    public ServiceConnector(String endpoint, String methodName, SscToken sscToken) {
        this.url = WS_SERVER + endpoint;
        this.methodName = methodName;
        this.sscToken = sscToken;
        ParameterizedType c = (ParameterizedType) getClass().getGenericSuperclass();
        Type type = c.getActualTypeArguments()[0];
        this.type = new DataResultParametrizedType(type);
        SslConection.allowSelfSignedElfecSSL(AppPreferences.getApplicationContext());
    }

    /**
     * Construye un conector de webservice soap con los parámetros indicados y
     * sin autenticación.
     * This is an abstract class so it must be called with braces at the end:<br>
     * <code>new ServiceConnector(&hellip;){}</code>
     *
     * @param endpoint   endpoint
     * @param methodName soap method
     */
    public ServiceConnector(String endpoint, String methodName) {
        this(endpoint, methodName, null);
    }

    public Observable<T> execute(WSParam... params) {
        return Observable.create(subs -> {
            if (subs.isUnsubscribed()) return;
            try {
                isUnsubscribed = false;
                subs.add(new Subscription() {
                    @Override
                    public void unsubscribe() {
                        if (isUnsubscribed) return;
                        isUnsubscribed = true;
                        cancelRequest();
                    }

                    @Override
                    public boolean isUnsubscribed() {
                        return isUnsubscribed;
                    }
                });
                subs.onNext(request(params));
            } catch (Throwable e) {
                subs.onError(ServiceErrorFactory.fromThrowable(e));
            }
            subs.onCompleted();
        });
    }


    private T request(WSParam... params) throws Exception {
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        for (WSParam param : params) {
            request.addProperty(param.getKey(), param.getValue());
        }
        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);
        ht = getProperProtocolTransportSE();
        List<HeaderProperty> headerPropertyArrayList = new ArrayList<>();
        headerPropertyArrayList.add(new HeaderProperty("Connection",
                "close"));
        if (sscToken != null) {
            headerPropertyArrayList.add(new HeaderProperty("x-ws-token",
                    sscToken.toString()));
        }
        Log.d(TAG, "Call to url: " + url + ", method: " + methodName);
        @SuppressWarnings("unchecked")
        List<HeaderProperty> headers = ht.call(SOAP_ACTION, envelope,
                headerPropertyArrayList);
        checkHeaderStatus(headers);
        String response = envelope.getResponse().toString();
        response = patchResponse(response);
        Log.d(TAG, "Url: " + url + ", Response: " + response);
        Gson gson = GsonUtils.generateGson();
        DataResult<T> dataResult = gson.fromJson(response, type);
        if (dataResult.hasErrors())
            throw dataResult.getErrors().get(0);
        ht = null;
        return dataResult.getData();
    }

    private void cancelRequest() {
        if (ht == null) return;
        try {
            ht.reset();
            ServiceConnection connection = ht.getServiceConnection();
            if (connection == null) return;
            connection.disconnect();
            ht = null;
        } catch (Exception e) {
            //ignore error
        }
    }

    /**
     * Some of the responses are strings instead of arrays or objects, due
     * to bad server coding. Thus we should patch some responses.
     *
     * @param response response string
     * @return patched response
     */
    private String patchResponse(String response) {
        try {
            JSONObject object = new JSONObject(response);
            String respContent = object.optString("Response");
            //if response is no string then return, everything Ok
            if (respContent == null)
                return response;
            //is object
            if (respContent.charAt(0) == '{' && respContent.charAt(respContent.length() - 1) == '}') {
                JSONObject content = new JSONObject(respContent);
                object.put("Response", content);
                return object.toString();
            }
            //is array
            if (respContent.charAt(0) == '[' && respContent.charAt(respContent.length() - 1) == ']') {
                JSONArray content = new JSONArray(respContent);
                object.put("Response", content);
                return object.toString();
            }
        } catch (Exception e) {
            return response;
        }
        return response;
    }


    /**
     * Verifica que el codigo http response sea 200
     *
     * @param headers lista de headers
     * @throws HttpResponseException
     */
    private void checkHeaderStatus(List<HeaderProperty> headers)
            throws HttpResponseException {
        for (HeaderProperty header : headers) {
            if (header != null && header.getValue() != null
                    && header.getValue().contains("HTTP/1.1")) {
                int codeStatus = Integer
                        .parseInt(header.getValue().split(" ")[1]);
                if (codeStatus != 200)
                    throw new HttpResponseException(
                            "HTTP request failed, HTTP status: " + codeStatus,
                            codeStatus);
            }
        }
    }

    private SoapSerializationEnvelope getSoapSerializationEnvelope(
            SoapObject request) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = false;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.setOutputSoapObject(request);
        MarshalDouble md = new MarshalDouble();
        md.register(envelope);
        return envelope;
    }

    /**
     * Obtiene el tunel de transporte HTTP/HTTPS dependiendo de la URL
     *
     * @return HttpTransportSE/HttpsTransportSE
     * @throws MalformedURLException
     */
    private HttpTransportSE getProperProtocolTransportSE() throws MalformedURLException {
        URL urlObj = new URL(url);
        return urlObj.getProtocol().equals("http") ? getHttpTransportSE()
                : getHttpsTransportSE();
    }

    /**
     * Obtiene el tunel de transporte HTTP
     *
     * @return HttpTransportSE
     */
    private HttpTransportSE getHttpTransportSE() {
        HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY, url, TIMEOUT);
        ht.debug = false;
        ht.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
        return ht;
    }

    /**
     * Obtiene el tunel de transporte HTTPS
     *
     * @return HttpsTransportSE
     * @throws MalformedURLException
     */
    private HttpTransportSE getHttpsTransportSE() throws MalformedURLException {
        URL urlObj = new URL(url);
        int port = urlObj.getPort();
        HttpTransportSE ht = new HttpsTransportSE(urlObj.getHost(),
                port == -1 ? urlObj.getDefaultPort() : port, urlObj.getFile(),
                TIMEOUT);
        ht.debug = false;
        ht.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
        return ht;
    }
}
