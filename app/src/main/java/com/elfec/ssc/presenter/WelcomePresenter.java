package com.elfec.ssc.presenter;

import com.elfec.ssc.business_logic.ClientManager;
import com.elfec.ssc.helpers.threading.ThreadMutex;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.Contact;
import com.elfec.ssc.model.enums.ClientStatus;
import com.elfec.ssc.presenter.views.IWelcomeView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WelcomePresenter extends BasePresenter<IWelcomeView> {

    public WelcomePresenter(IWelcomeView view) {
        super(view);
    }

    public void handlePickedGmailAccount(String gmail) {
        ClientManager.registerActiveClient(new Client(gmail, ClientStatus.ACTIVE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(client -> {
                    mView.goToMainMenu();
                }, e -> {
                });
    }

    public void insertContact() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                if (Contact.getAll(Contact.class).size() == 0) {
                    ThreadMutex.instance("InsertContact").setBusy();
                    Contact.createDefaultContact();
                    ThreadMutex.instance("InsertContact").setFree();
                }
            }
        });
        thread.start();
    }
}
