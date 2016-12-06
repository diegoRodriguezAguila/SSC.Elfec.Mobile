package com.elfec.ssc.local_storage;

import com.cesarferreira.rxpaper.RxPaper;
import com.elfec.ssc.model.Contact;

import rx.Observable;

/**
 * Created by Diego on 21/8/2016.
 * local storage for contacts
 */
public class ContactStorage {
    public static final String CONTACT_BOOK = "contact.book";
    private static final String ACTIVE_CONTACT_KEY = "active_contact";

    /**
     * Saves the current active contact to the database
     *
     * @param contact to save
     * @return Observable of contact
     */
    public Observable<Contact> saveContact(Contact contact) {
        return RxPaper.book(CONTACT_BOOK).write(ACTIVE_CONTACT_KEY, contact);
    }

    /**
     * Gets the current active contact from the database
     *
     * @return Observable of contact
     */
    public Observable<Contact> getContact() {
        return RxPaper.book(CONTACT_BOOK).read(ACTIVE_CONTACT_KEY);
    }
}
