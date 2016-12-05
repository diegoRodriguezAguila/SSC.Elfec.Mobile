package com.elfec.ssc.presenter;


import com.elfec.ssc.presenter.views.IBaseView;

import java.io.Closeable;

/**
 * Created by drodriguez on 07/07/2016.
 * Base presenter
 */
public abstract class BasePresenter<T extends IBaseView> implements Closeable {

    protected T mView;
    public BasePresenter(T view){
        mView = view;
    }

    public void close() {
        mView = null;
    }
}