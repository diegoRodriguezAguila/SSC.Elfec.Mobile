package com.elfec.ssc.model.webservices;

import android.os.AsyncTask;
import android.util.Log;

import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.exceptions.OutdatedAppException;
import com.elfec.ssc.model.exceptions.ServerSideException;
import com.elfec.ssc.model.security.WSToken;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase general para conexión con servicios web
 * 
 * @author Diego
 *
 * @param <TResult>
 */
public class WebServiceConnector<TResult> extends
		AsyncTask<WSParam, TResult, TResult> {
	private final String WS_SERVER = "https://ssc.elfec.bo:4343/";// "http://192.168.50.56/SSC.Elfec/web_services/";//
	private String url;
	private String soapAction; // = "";
	private String namespace;// = "http://DefaultNamespace";
	private String methodName;
	private WSToken wsToken;
	private IWSFinishEvent<TResult> onFinishedEvent;
	private IWSResultConverter<TResult> converter;
	private WSResponse<TResult> resultWS;

	/**
	 * Construye un conector de webservice soap con los parámetros indicados y
	 * con autenticación por wsToken
	 * 
	 * @param url
	 * @param soapAction
	 * @param namespace
	 * @param methodName
	 * @param wsToken
	 * @param converter
	 */
	public WebServiceConnector(String url, String soapAction, String namespace,
			String methodName, WSToken wsToken,
			IWSResultConverter<TResult> converter) {
		super();
		this.url = WS_SERVER + url;
		this.soapAction = soapAction;
		this.namespace = namespace;
		this.methodName = methodName;
		this.wsToken = wsToken;
		this.converter = converter;
		this.resultWS = new WSResponse<>();
	}

	/**
	 * Construye un conector de webservice soap con los parámetros indicados y
	 * con autenticación por wsToken
	 * 
	 * @param url
	 * @param soapAction
	 * @param namespace
	 * @param methodName
	 * @param wsToken
	 * @param converter
	 * @param onFinishedEvent
	 */
	public WebServiceConnector(String url, String soapAction, String namespace,
			String methodName, WSToken wsToken,
			IWSResultConverter<TResult> converter,
			IWSFinishEvent<TResult> onFinishedEvent) {
		super();
		this.url = WS_SERVER + url;
		this.soapAction = soapAction;
		this.namespace = namespace;
		this.methodName = methodName;
		this.wsToken = wsToken;
		this.onFinishedEvent = onFinishedEvent;
		this.converter = converter;
		this.resultWS = new WSResponse<>();
	}

	/**
	 * Construye un conector de webservice soap con los parámetros indicados sin
	 * autenticación
	 * 
	 * @param url
	 * @param soapAction
	 * @param namespace
	 * @param methodName
	 * @param converter
	 */
	public WebServiceConnector(String url, String soapAction, String namespace,
			String methodName, IWSResultConverter<TResult> converter) {
		super();
		this.url = WS_SERVER + url;
		this.soapAction = soapAction;
		this.namespace = namespace;
		this.methodName = methodName;
		this.converter = converter;
		this.resultWS = new WSResponse<>();
	}

	/**
	 * Construye un conector de webservice soap con los parámetros indicados sin
	 * autenticaci+on
	 * 
	 * @param url
	 * @param soapAction
	 * @param namespace
	 * @param methodName
	 * @param converter
	 * @param onFinishedEvent
	 */
	public WebServiceConnector(String url, String soapAction, String namespace,
			String methodName, IWSResultConverter<TResult> converter,
			IWSFinishEvent<TResult> onFinishedEvent) {
		super();
		this.url = WS_SERVER + url;
		this.soapAction = soapAction;
		this.namespace = namespace;
		this.methodName = methodName;
		this.onFinishedEvent = onFinishedEvent;
		this.converter = converter;
		this.resultWS = new WSResponse<>();
	}

	@Override
	protected TResult doInBackground(WSParam... params) {
		SoapObject request = new SoapObject(namespace, methodName);
		String result = "";
		for (WSParam param : params) {
			request.addProperty(param.getKey(), param.getValue());
		}
		SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);
		try {
			HttpTransportSE ht = getProperProtocolTransportSE();
			List<HeaderProperty> headerPropertyArrayList = new ArrayList<>();
			headerPropertyArrayList.add(new HeaderProperty("Connection",
					"close"));
			if (wsToken != null)
				headerPropertyArrayList.add(new HeaderProperty("x-ws-token",
						wsToken.toString()));
			@SuppressWarnings("unchecked")
			List<HeaderProperty> headers = ht.call(soapAction, envelope,
					headerPropertyArrayList);
			checkHeaderStatus(headers);
			result = envelope.getResponse().toString();
		} catch (HttpResponseException e) {
			Log.d(methodName, "Error in url: " + url + " " + e.getMessage());
			if (e.getStatusCode() == 403) // aplicación ya no es válida
				resultWS.addError(new OutdatedAppException());
			else
				resultWS.addError(e);
		} catch (ConnectException e) {
			Log.d(methodName, e.toString());
			resultWS.addError(new ConnectException(
					"No fue posible conectarse con el servidor, porfavor revise su conexión a internet"));

		} catch (SocketTimeoutException e) {
			Log.d(methodName, e.toString());
			resultWS.addError(new SocketTimeoutException(
					"No fue posible conectarse con el servidor, puede que el servidor no se encuentre disponible temporalmente, porfavor verifique su conexión a internet"));
		} catch (IOException e) {
			Log.d(methodName, e.toString());
			resultWS.addError(new ConnectException(
					"Ocurrió un problema al conectarse con el servidor, porfavor revise su conexión a internet"));
		} catch (XmlPullParserException e) {
			Log.d(methodName, e.toString());
			resultWS.addError(new ServerSideException());
		} catch (Exception e) {
			Log.d(methodName, e.toString());
			resultWS.addError(new Exception(
					"Ocurrió un error inesperado al conectarse al servicio, lamentamos las molestias, inténtelo de nuevo mas tarde."));
		}
		return converter.convert(resultWS.convertErrors(result));
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

	@Override
	protected void onPostExecute(TResult result) {
		if (onFinishedEvent != null) {
			onFinishedEvent.executeOnFinished(resultWS.setResult(result));
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
	HttpTransportSE getProperProtocolTransportSE() throws MalformedURLException {
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
