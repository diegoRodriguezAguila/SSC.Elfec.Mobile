package com.elfec.ssc.presenter.views;

import android.support.annotation.StringRes;

/**
 * Created by drodriguez on 07/07/2016.
 * View abstraction of a proces where implies a procesing step,
 * on error and on success. It extends from {@link IBaseView}, so
 * is not necessary to extend it in its child interfaces.
 */
public interface IProcessView<T> extends IBaseView{
    /**
     * Executed when a processing action starts
     * @param message message for processing status
     */
    void onProcessing(@StringRes int message);

    /**
     * Called when an error while processing ocurred
     * @param e error
     */
    void onError(Throwable e);

    /**
     * Called when the processing finished successfully
     * @param result result of processing
     */
    void onSuccess(T result);
}
