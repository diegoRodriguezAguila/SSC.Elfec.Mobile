package com.elfec.ssc.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.helpers.HtmlCompat;
import com.elfec.ssc.helpers.ui.ButtonClicksHelper;
import com.elfec.ssc.helpers.ui.KeyboardHelper;
import com.elfec.ssc.helpers.utils.MessageListFormatter;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.presenter.RegisterAccountPresenter;
import com.elfec.ssc.presenter.views.IRegisterAccount;
import com.elfec.ssc.view.controls.ProgressDialogService;
import com.elfec.ssc.view.controls.RegisterInformationDialogService;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;

public class RegisterAccountActivity extends BaseActivity implements
        IRegisterAccount {

    /**
     * Codigo para startActivityForResult
     */
    public static final int REGISTER_REQUEST_CODE = 213;
    /**
     * Constante para obtener el resultado de bundle de registrar
     */
    public static final String REGISTER_SUCCESS = "AccountRegistered";

    private RegisterAccountPresenter mPresenter;
    protected
    @BindView(R.id.txt_nus)
    EditText mTxtNus;
    protected
    @BindView(R.id.txt_accountNumber)
    EditText mTxtAccountNumber;
    protected
    @BindView(R.id.view_content)
    RelativeLayout mRootLayout;
    private de.keyboardsurfer.android.widget.crouton.Style mCroutonStyle;
    private ProgressDialogService mWaitingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);
        mPresenter = new RegisterAccountPresenter(this);
        ButterKnife.bind(this);
        mTxtAccountNumber
                .setOnEditorActionListener((v, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        KeyboardHelper.hideKeyboard(mRootLayout);
                        btnRegisterAccountClick(mRootLayout);
                    }
                    return false;
                });
        mCroutonStyle = new de.keyboardsurfer.android.widget.crouton.Style.Builder()
                .setFontName("fonts/segoe_ui_semilight.ttf")
                .setTextSize(16)
                .setBackgroundColorValue(
                        ContextCompat.getColor(RegisterAccountActivity.this,
                                R.color.ssc_elfec_color_highlight)).build();
        setOnFocusChangedListeners();
        mWaitingDialog = new ProgressDialogService(RegisterAccountActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menu_information: {
                if (ButtonClicksHelper.canClickButton()) {
                    new RegisterInformationDialogService(this).show();
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void releasePresenter() {
        mPresenter.close();
        mPresenter = null;
    }

    /**
     * Asigna los onFocusChange listeners del mTxtNus y el mTxtAccountNumber
     */
    public void setOnFocusChangedListeners() {
        new Thread(() -> {
            mTxtNus.setOnFocusChangeListener((v, gotFocus) -> {
                if (!gotFocus) {
                    mPresenter.validateNus();
                }
            });
            mTxtAccountNumber
                    .setOnFocusChangeListener((v, gotFocus) -> {
                        if (!gotFocus) {
                            mPresenter.validateAccountNumber();
                        }
                    });
        }).start();
    }

    /**
     * Convierte una lista de errores en el formato html necesario para
     * mostrarlo en el metodo setError
     *
     * @param validationErrors errors
     * @return cadena con mensajes formateados y color
     */
    public String getHTMLListFromErrors(List<String> validationErrors) {
        return "<font color='#006086'><b>" +
                MessageListFormatter.formatHTMLFromStringList(validationErrors).toString() + "</b></font>";
    }

    public void btnRegisterAccountClick(View view) {
        if (ButtonClicksHelper.canClickButton())
            mPresenter.registerAccount();
    }


    public void hideWaiting() {
        if (mWaitingDialog != null)
            mWaitingDialog.dismiss();
    }

    //region Interface IRegisterAccount methods

    @Override
    public void setNusErrors(final List<String> validationErrors) {
        runOnUiThread(() -> {
            if (validationErrors.size() > 0)
                mTxtNus.setError(HtmlCompat
                        .fromHtml(getHTMLListFromErrors(validationErrors)));
            else
                mTxtNus.setError(null);
        });
    }

    @Override
    public void setAccountNumberErrors(final List<String> validationErrors) {
        runOnUiThread(() -> {
            if (validationErrors.size() > 0)
                mTxtAccountNumber.setError(HtmlCompat
                        .fromHtml(getHTMLListFromErrors(validationErrors)));
            else
                mTxtAccountNumber.setError(null);
        });
    }

    @Override
    public String getNus() {
        return mTxtNus.getText().toString();
    }

    @Override
    public String getAccountNumber() {
        return mTxtAccountNumber.getText().toString().replace("-", "").trim();
    }

    @Override
    public String getNusValidationRules() {
        return mTxtNus.getTag().toString();
    }

    @Override
    public String getAccountNumberValidationRules() {
        return mTxtAccountNumber.getTag().toString();
    }


    @Override
    public void notifyFieldErrors() {
        runOnUiThread(() -> {
            Crouton.clearCroutonsForActivity(RegisterAccountActivity.this);
            Crouton.makeText(RegisterAccountActivity.this,
                    R.string.errors_in_fields, mCroutonStyle, mRootLayout)
                    .show();
        });
    }

    @Override
    public void onProcessing(@StringRes int message) {
        runOnUiThread(() -> mWaitingDialog
                .setMessage(RegisterAccountActivity.this.getString(message))
                .setCancelable(false)
                .setCanceledOnTouchOutside(false).show());
    }

    @Override
    public void onError(Throwable e) {
        hideWaiting();
        AlertDialog.Builder builder = new AlertDialog.Builder(
                RegisterAccountActivity.this);
        builder.setTitle(R.string.errors_on_register_title)
                .setMessage(e.getMessage())
                .setPositiveButton(R.string.btn_ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView txtMsg = (TextView) dialog.findViewById(android.R.id.message);
        if (txtMsg != null) {
            txtMsg.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    @Override
    public void onSuccess(Account account) {
        hideWaiting();
        SuperToast.create(RegisterAccountActivity.this,
                getString(R.string.account_successfully_reg, account.getNus()),
                SuperToast.Duration.LONG,
                Style.getStyle(Style.BLUE, SuperToast.Animations.FADE)).show();
        Intent returnIntent = new Intent();
        returnIntent.putExtra(REGISTER_SUCCESS, true);
        setResult(RESULT_OK, returnIntent);
        onBackPressed();
    }

    //endregion
}
