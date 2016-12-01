package com.elfec.ssc.model.webservices;

import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

/**
 * Extensión Marshal de KSoap para soportar servicios web que retornen Doubles
 * @author Diego
 *
 */
public class MarshalDouble implements Marshal 
{


    @Override
	public Object readInstance(XmlPullParser parser, String namespace, String name, 
            PropertyInfo expected) throws IOException, XmlPullParserException {
        
        return Double.parseDouble(parser.nextText());
    }


    @Override
	public void register(SoapSerializationEnvelope cm) {
         cm.addMapping(cm.xsd, "double", Double.class, this);
        
    }


    @Override
	public void writeInstance(XmlSerializer writer, Object obj) throws IOException {
           writer.text(obj.toString());
        }
    
}