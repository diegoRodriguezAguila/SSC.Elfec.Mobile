package com.elfec.ssc.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.elfec.ssc.R;
import com.elfec.ssc.helpers.ui.ButtonClicksHelper;
import com.elfec.ssc.helpers.ui.KeyboardHelper;
import com.elfec.ssc.helpers.utils.MessageListFormatter;
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
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterAccount extends AppCompatActivity implements
        IRegisterAccount {

    /**
     * Codigo para startActivityForResult
     */
    public static final int REGISTER_REQUEST_CODE = 213;
    /**
     * Constante para obtener el resultado de bundle de registrar
     */
    public static final String REGISTER_SUCCESS = "AccountRegistered";

    private RegisterAccountPresenter presenter;
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
        presenter = new RegisterAccountPresenter(this);
        ButterKnife.bind(this);
        mTxtAccountNumber
                .setOnEditorActionListener(new OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            KeyboardHelper.hideKeyboard(mRootLayout);
                            btnRegisterAccountClick(mRootLayout);
                        }
                        return false;
                    }
                });
        mCroutonStyle = new de.keyboardsurfer.android.widget.crouton.Style.Builder()
                .setFontName("fonts/segoe_ui_semilight.ttf")
                .setTextSize(16)
                .setBackgroundColorValue(
                        ContextCompat.getColor(RegisterAccount.this,
                                R.color.ssc_elfec_color_highlight)).build();
        setOnFocusChangedListeners();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
    public void onBackPressed() {
        finish();// go back to the previous Activity
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    /**
     * Asigna los onFocusChange listeners del mTxtNus y el mTxtAccountNumber
     */
    public void setOnFocusChangedListeners() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mTxtNus.setOnFocusChangeListener(new OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean gotFocus) {
                        if (!gotFocus) {
                            presenter.validateNUS();
                        }
                    }
                });
                mTxtAccountNumber
                        .setOnFocusChangeListener(new OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean gotFocus) {
                                if (!gotFocus) {
                                    presenter.validateAccountNumber();
                                }
                            }
                        });
            }
        });
        thread.start();
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
        presenter.processAccountData();
    }

    //region Interface IRegisterAccount methods

    @Override
    public void setNusErrors(final List<String> validationErrors) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (validationErrors.size() > 0)
                    mTxtNus.setError(Html
                            .fromHtml(getHTMLListFromErrors(validationErrors)));
                else
                    mTxtNus.setError(null);
            }
        });
    }

    @Override
    public void setAccountNumberErrors(final List<String> validationErrors) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (validationErrors.size() > 0)
                    mTxtAccountNumber.setError(Html
                            .fromHtml(getHTMLListFromErrors(validationErrors)));
                else
                    mTxtAccountNumber.setError(null);
            }
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
    public String getPhoneNumber() {
        String phoneNumber = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE))
                .getLine1Number();
        return phoneNumber == null ? "" : phoneNumber;
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
    public void notifyAccountSuccessfullyRegistered() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SuperToast.create(RegisterAccount.this,
                        R.string.account_successfully_reg,
                        SuperToast.Duration.LONG,
                        Style.getStyle(Style.BLUE, SuperToast.Animations.FADE))
                        .show();
                Intent returnIntent = new Intent();
                returnIntent.putExtra(REGISTER_SUCCESS, true);
                setResult(RESULT_OK, returnIntent);
                onBackPressed();
            }
        });
    }

    @Override
    public void showWSWaiting() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWaitingDialog = new ProgressDialogService(RegisterAccount.this);
                mWaitingDialog.setMessage(RegisterAccount.this.getResources()
                        .getString(R.string.waiting_msg));
                mWaitingDialog.setCancelable(false);
                mWaitingDialog.setCanceledOnTouchOutside(false);
                mWaitingDialog.show();
            }
        });
    }

    @Override
    public void hideWSWaiting() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mWaitingDialog != null)
                    mWaitingDialog.dismiss();
            }
        });
    }

    @Override
    public void notifyErrorsInFields() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Crouton.clearCroutonsForActivity(RegisterAccount.this);
                Crouton.makeText(RegisterAccount.this,
                        R.string.errors_in_fields, mCroutonStyle, mRootLayout)
                        .show();
            }
        });
    }

    @Override
    public void notifyAccountAlreadyRegistered() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        RegisterAccount.this);
                builder.setTitle(R.string.account_already_reg_title)
                        .setMessage(R.string.account_already_reg_msg)
                        .setPositiveButton(R.string.btn_ok, null).show();
            }
        });
    }

    @Override
    public void showRegistrationErrors(final List<Exception> errors) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (errors.size() > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            RegisterAccount.this);
                    builder.setTitle(R.string.errors_on_register_title)
                            .setMessage(
                                    MessageListFormatter
                                            .formatHTMLFromErrors(RegisterAccount
                                                    .this, errors))
                            .setPositiveButton(R.string.btn_ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    ((TextView)dialog.findViewById(android.R.id.message))
                            .setMovementMethod(LinkMovementMethod.getInstance());
                }
            }
        });
    }

    //endregion
}
