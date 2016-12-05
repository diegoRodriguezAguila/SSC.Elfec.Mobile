package com.elfec.ssc.helpers.ui;

import android.support.annotation.StringRes;

import com.elfec.ssc.security.AppPreferences;

/**
 * Created by drodriguez on 05/12/2016.
 * util to get messages easily
 */

public class MessagesUtil {

    /**
     * Gets the string resource resolved
     * @param resId resource id
     * @return string
     */
    public static String getString(@StringRes int resId){
        return AppPreferences.getApplicationContext().getString(resId);
    }
}
