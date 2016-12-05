package com.elfec.ssc.presenter;


import com.elfec.ssc.presenter.views.IBaseView;

import java.io.Closeable;

import rx.Subscription;

/**
 * Created by drodriguez on 07/07/2016.
 * Base presenter
 */
public abstract class BasePresenter<T extends IBaseView> implements Closeable {
    protected Subscription mSubscription;
    protected T mView;

    public BasePresenter(T view) {
        mView = view;
    }

    public void close() {
        mView = null;
        cancelSubscription();
    }

    protected void cancelSubscription() {
        if (mSubscription != null && !mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }
}