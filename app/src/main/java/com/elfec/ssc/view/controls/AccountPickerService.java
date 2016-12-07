package com.elfec.ssc.view.controls;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.elfec.ssc.R;
import com.elfec.ssc.view.controls.events.AccountPickListener;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.Closeable;

/**
 * Provides a service for gmail account picking. Use the {@link #instance(FragmentActivity)} method
 * in order to get an instance. You may also set a listener with
 * {@link #setAccountPickListener(AccountPickListener)}.
 * You also must call {@link #handleResult(int, Intent)} in your activity's onResult, in order
 * to be able to process the result of the picking
 * Don't forget to {@link #close()} on the activity's onDestroy, to prevent memory leaks.
 */
public class AccountPickerService implements Closeable {
    public static final String TAG = "AccountPicker";
    private static final int RC_SIGN_IN = 44;

    private GoogleApiClient mGoogleApiClient;
    private AccountPickListener mListener;
    private FragmentActivity mActivity;

    /**
     * Creates an instance of this service
     *
     * @param activity activity
     * @return new instance
     */
    public static AccountPickerService instance(FragmentActivity activity) {
        return new AccountPickerService(activity);
    }

    /**
     * Private constructor should use {@link #instance(FragmentActivity)} method instead
     *
     * @param activity fragment activity
     */
    private AccountPickerService(FragmentActivity activity) {
        mActivity = activity;
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mActivity.getString(R.string.default_web_client_id))
                .requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .enableAutoManage(mActivity, connectionResult -> {
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
    }

    /**
     * Sets the account pick listener
     *
     * @param listener listener
     * @return this instance
     */
    public AccountPickerService setAccountPickListener(AccountPickListener listener) {
        this.mListener = listener;
        return this;
    }

    public void show() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.clearDefaultAccountAndReconnect();
        }
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        mActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Handles the activity's on result for this operation
     *
     * @param requestCode code
     * @param data        data
     */
    public void handleResult(int requestCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult: " + result.getStatus().getStatusCode());
        if (!result.isSuccess()) {
            SuperToast.create(mActivity, R.string.sign_in_failed, SuperToast.Duration.LONG,
                    Style.getStyle(Style.BLUE, SuperToast.Animations.FADE)).show();
            return;
        }
        // Signed in successfully, show authenticated UI.
        GoogleSignInAccount acct = result.getSignInAccount();
        if (acct != null && mListener != null)
            mListener.onAccountPicked(acct.getEmail());
    }

    @Override
    public void close() {
        mGoogleApiClient = null;
        mListener = null;
        mActivity = null;
    }
}
