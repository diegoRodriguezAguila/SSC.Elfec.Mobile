package com.elfec.ssc.presenter;

import com.elfec.ssc.helpers.threading.ThreadMutex;
import com.elfec.ssc.model.Contact;
import com.elfec.ssc.presenter.views.IContact;

public class ContactPresenter {

    private IContact view;

    public ContactPresenter(IContact view) {
        this.view = view;
    }

    public void setDefaultData() {
        ThreadMutex.instance("InsertContact").addOnThreadReleasedEvent(() ->
                new Thread(() -> {
            Contact defaultContact = Contact.getDefaultContact();
            view.setData(defaultContact.getPhone(), defaultContact.getAddress(),
                    defaultContact.getEmail(), defaultContact.getWebPage(),
                    defaultContact.getFacebook(),
                    defaultContact.getFacebookId());
        }).start());
    }
}
