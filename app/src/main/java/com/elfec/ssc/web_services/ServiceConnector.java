package com.elfec.ssc.web_services;

import com.elfec.ssc.BuildConfig;
import com.elfec.ssc.helpers.utils.GsonUtils;
import com.elfec.ssc.model.security.SscToken;
import com.elfec.ssc.model.webservices.DataResult;
import com.elfec.ssc.model.webservices.MarshalDouble;
import com.elfec.ssc.model.webservices.WSParam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsTransportSE;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Clase general para conexión con servicios web
 *
 * @param <T>
 * @author Diego
 */
public class ServiceConnector<T> {
    private static final String WS_SERVER = BuildConfig.WS_SERVER_URL;
    private static final String SOAP_ACTION = "";
    private static final String NAMESPACE = "ssc_elfec";

    private String url;
    private String methodName;
    private SscToken sscToken;

    /**
     * Construye un conector de webservice soap con los parámetros indicados y
     * con autenticación por sscToken
     *
     * @param endpoint   endpoint
     * @param methodName soap method
     * @param sscToken   ssc token
     */
    public ServiceConnector(String endpoint, String methodName, SscToken sscToken) {
        this.url = WS_SERVER + endpoint;
        this.methodName = methodName;
        this.sscToken = sscToken;
    }

    /**
     * Construye un conector de webservice soap con los parámetros indicados y
     * sin autenticación
     *
     * @param endpoint   endpoint
     * @param methodName soap method
     */
    public ServiceConnector(String endpoint, String methodName) {
        this.url = WS_SERVER + endpoint;
        this.methodName = methodName;
    }

    public Observable<T> execute(WSParam... params) {
        return Observable.create(subs -> {
            if (subs.isUnsubscribed()) return;
            try {
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
        HttpTransportSE ht = getProperProtocolTransportSE();
        List<HeaderProperty> headerPropertyArrayList = new ArrayList<>();
        headerPropertyArrayList.add(new HeaderProperty("Connection",
                "close"));
        if (sscToken != null)
            headerPropertyArrayList.add(new HeaderProperty("x-ws-token",
                    sscToken.toString()));
        @SuppressWarnings("unchecked")
        List<HeaderProperty> headers = ht.call(SOAP_ACTION, envelope,
                headerPropertyArrayList);
        checkHeaderStatus(headers);
        String result = envelope.getResponse().toString();
        Gson gson = GsonUtils.generateGson();
        Type collectionType = new TypeToken<DataResult<T>>() {}.getType();
        DataResult<T> dataResult = gson.fromJson(result, collectionType);
        if (dataResult.hasErrors())
            throw dataResult.getErrors().get(0);
        return dataResult.getData();
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
        HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY, url, 10000);
        ht.debug = true;
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
                10000);
        ht.debug = true;
        ht.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
        return ht;
    }
}
