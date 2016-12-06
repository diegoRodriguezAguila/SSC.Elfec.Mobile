package com.elfec.ssc.presenter;

import com.elfec.ssc.business_logic.ContactManager;
import com.elfec.ssc.presenter.views.IContactsView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContactPresenter extends BasePresenter<IContactsView> {


    public ContactPresenter(IContactsView view) {
        super(view);
    }

    public void loadContact() {
        cancelSubscription();
        mSubscription = ContactManager.getDefaultContact()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mView::setContact, e -> {
                });
    }
}
