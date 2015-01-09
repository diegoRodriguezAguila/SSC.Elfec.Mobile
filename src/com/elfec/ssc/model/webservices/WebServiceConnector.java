package com.elfec.ssc.model.webservices;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.exceptions.ServerSideException;

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
	private IWSFinishEvent<TResult> onFinishedEvent;
	private IWSResultConverter<TResult> converter;
	private WSResponse<TResult> resultWS;
	
	public WebServiceConnector(String url, String soapAction, String namespace, String methodName, IWSResultConverter<TResult> converter) 
	{
		super();
		this.url = url;
		this.soapAction = soapAction;
		this.namespace = namespace;
		this.methodName = methodName;
		this.converter = converter;
		this.resultWS = new WSResponse<TResult>();
	}
	
	
	public WebServiceConnector(String url, String soapAction, String namespace, String methodName 
			, IWSResultConverter<TResult> converter,IWSFinishEvent<TResult> onFinishedEvent) 
	{
		super();
		this.url = url;
		this.soapAction = soapAction;
		this.namespace = namespace;
		this.methodName = methodName;
		this.onFinishedEvent = onFinishedEvent;
		this.converter = converter;
		this.resultWS = new WSResponse<TResult>();
	}


	@Override
	protected  TResult doInBackground(WSParam... params) {
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
			List<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
			headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));
			ht.call(soapAction, envelope, headerPropertyArrayList);
			result = envelope.getResponse().toString();
		} 
		catch (HttpResponseException e) 
		{
			Log.d(methodName, e.toString());
		
			resultWS.addError(e);
		} 
		catch (ConnectException e)
		{
			Log.d(methodName, e.toString());
			resultWS.addError(new ConnectException("No fue posible conectarse con el servidor, porfavor revise su conexión a internet"));
			
		}
		catch (SocketTimeoutException e)
		{
			Log.d(methodName, e.toString());
			resultWS.addError(new SocketTimeoutException("No fue posible conectarse con el servidor, puede que el servidor no se encuentre disponible temporalmente, porfavor verifique su conexión a internet"));
		}
		catch (IOException e) 
		{
			Log.d(methodName, e.toString());
			resultWS.addError(e);
		} 
		catch (XmlPullParserException e) 
		{
			Log.d(methodName, e.toString());
			resultWS.addError(new ServerSideException());
		}
		return converter.convert(resultWS.convertErrors(result));
	}
	
	@Override
	protected void onPostExecute(TResult result)
	{
		if(onFinishedEvent!=null)
		{
			onFinishedEvent.executeOnFinished(resultWS.setResult(result));
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
	    HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY, url, 10000);
	    ht.debug = true;
	    ht.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
	    return ht;
	}
}
