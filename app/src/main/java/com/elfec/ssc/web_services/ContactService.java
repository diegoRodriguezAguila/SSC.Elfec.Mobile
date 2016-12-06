package com.elfec.ssc.web_services;

import com.elfec.ssc.model.Contact;
import com.elfec.ssc.model.security.SscToken;

import rx.Observable;

/**
 * Se encarga de la conexión a los servicios web para contactos
 *
 * @author Diego
 */
public class ContactService {

    private SscToken sscToken;

    public ContactService(SscToken sscToken) {
        this.sscToken = sscToken;
    }

    /**
     * Obtiene una actualización de la información de contacto
     */
    public Observable<Contact> getContact() {
        return new ServiceConnector<Contact>("ContactWS.php?wsdl",
                "GetContactUpdate", sscToken) {
        }.execute();
    }
}
