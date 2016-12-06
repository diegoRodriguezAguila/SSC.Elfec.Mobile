package com.elfec.ssc.business_logic;

import com.elfec.ssc.local_storage.ContactStorage;
import com.elfec.ssc.model.Contact;
import com.elfec.ssc.web_services.ContactService;
import com.elfec.ssc.web_services.SscTokenRequester;

import rx.Observable;

/**
 * Se encarga de las operaciones relacionadas a los contactos
 *
 * @author drodriguez
 */
public class ContactManager {

    public static Observable<Contact> getDefaultContact() {
        return new ContactStorage().getContact()
                .map(contact -> {
                    if (contact == null)
                        return Contact.defaultContact();
                    return contact;
                });
    }

    /**
     * Retrieves the contact from web services and saves it for further uses
     *
     * @return observable of contact list
     */
    public static Observable<Contact> syncContact() {
        return getContactFromWs()
                .flatMap(contact -> new ContactStorage()
                        .saveContact(contact));
    }

    /**
     * Gets the contact via web services
     *
     * @return observable of contact list
     */
    public static Observable<Contact> getContactFromWs() {
        return new SscTokenRequester().getSscToken()
                .flatMap(sscToken -> new ContactService(sscToken).getContact());
    }
}
