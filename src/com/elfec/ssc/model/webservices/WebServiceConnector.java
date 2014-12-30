package com.elfec.ssc.model.webservices;

import java.io.IOException;
import java.net.Proxy;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.elfec.ssc.model.events.WSFinishEvent;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Clase general para conexión con servicios web
 * @author Diego
 *
 * @param <TResult>
 */
public class WebServiceConnector<TResult> extends AsyncTask<WSParam, TResult, TResult>
{
	private String url;// = "http://192.168.30.44:8080/ws_lecturas.cfc?wsdl";
	private String soapAction; //= "";  
	private String namespace;// = "http://DefaultNamespace";
	private String methodName;
	private WSFinishEvent<TResult> onFinishedEvent;
	private IWSResultConverter<TResult> converter;
	
	public WebServiceConnector(String url, String soapAction, String namespace, String methodName, IWSResultConverter<TResult> converter) 
	{
		super();
		this.url = url;
		this.soapAction = soapAction;
		this.namespace = namespace;
		this.methodName = methodName;
		this.converter = converter;
	}
	
	
	public WebServiceConnector(String url, String soapAction, String namespace, String methodName, 
			WSFinishEvent<TResult> onFinishedEvent, IWSResultConverter<TResult> converter) 
	{
		super();
		this.url = url;
		this.soapAction = soapAction;
		this.namespace = namespace;
		this.methodName = methodName;
		this.onFinishedEvent = onFinishedEvent;
		this.converter = converter;
	}


	@Override
	protected TResult doInBackground(WSParam... params) {
		SoapObject request = new SoapObject(namespace, methodName);
		String result="";
		for (int i = 0; i < params.length; i++) 
		{
				request.addProperty(params[i].getKey(), params[i].getValue());
		}
		SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);
		HttpTransportSE ht = getHttpTransportSE();
		try 
		{
			ht.call(soapAction, envelope);
			result = envelope.getResponse().toString();
		} 
		catch (HttpResponseException e) 
		{
			Log.d(methodName, e.toString());
		} 
		catch (IOException e) 
		{
			Log.d(methodName, e.toString());
		} 
		catch (XmlPullParserException e) 
		{
			Log.d(methodName, e.toString());
		}
		return converter.convert(result);
	}
	
	@Override
	protected void onPostExecute(TResult result)
	{
		if(onFinishedEvent!=null)
		{
			onFinishedEvent.executeOnFinished(result);
		}
	}
	
	private SoapSerializationEnvelope getSoapSerializationEnvelope(SoapObject request) {
	    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    envelope.dotNet = false;
	    envelope.implicitTypes = true;
	    envelope.setAddAdornments(false);
	    envelope.setOutputSoapObject(request); 
	    MarshalDouble md = new MarshalDouble();
	    md.register(envelope);
	    return envelope;
	}
	
	private HttpTransportSE getHttpTransportSE() {
	    HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY, url, 15000);
	    ht.debug = true;
	    ht.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
	    return ht;
	}
}
