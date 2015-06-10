package com.elfec.ssc.view;


import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.alertdialogpro.AlertDialogPro;
import com.alertdialogpro.ProgressDialogPro;
import com.elfec.ssc.R;
import com.elfec.ssc.businesslogic.webservices.WSTokenRequester;
import com.elfec.ssc.helpers.utils.MessageListFormatter;
import com.elfec.ssc.model.gcmservices.GCMTokenRequester;
import com.elfec.ssc.presenter.RegisterAccountPresenter;
import com.elfec.ssc.presenter.views.IRegisterAccount;
import com.elfec.ssc.security.PreferencesManager;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

import de.keyboardsurfer.android.widget.crouton.Crouton;

public class RegisterAccount extends ActionBarActivity implements IRegisterAccount {

	private Toolbar toolbar;
	
	private RegisterAccountPresenter presenter;
	private EditText txtNUS;
	private EditText txtAccountNumber;
	private de.keyboardsurfer.android.widget.crouton.Style croutonStyle;
	private AlertDialog waitingWSDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_account);
		presenter = new RegisterAccountPresenter(this);
		toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar); 
        ((TextView)toolbar.findViewById(R.id.toolbar_title)).setText(R.string.register_account_title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
		txtNUS = (EditText) findViewById(R.id.txt_nus);		
		txtAccountNumber = (EditText) findViewById(R.id.txt_accountNumber);
		croutonStyle =  new de.keyboardsurfer.android.widget.crouton.Style.Builder().setFontName("fonts/segoe_ui_semilight.ttf").setTextSize(16)
				.setBackgroundColorValue(getResources().getColor(R.color.ssc_elfec_color_highlight)).build();
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
		return super.onOptionsItemSelected(item);
	}
	
    @Override
	public void onBackPressed() {
	    finish();//go back to the previous Activity
	    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);  
	}
    
    /**
     * Asigna los onFocusChange listeners del txtNus y el txtAccountNumber
     */
    public void setOnFocusChangedListeners()
    {
    	Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {
				txtNUS.setOnFocusChangeListener(new OnFocusChangeListener() {		
					@Override
					public void onFocusChange(View v, boolean gotFocus) {
						if(!gotFocus)
						{
							presenter.validateNUS();
						}
					}
				});
				txtAccountNumber.setOnFocusChangeListener(new OnFocusChangeListener() {		
					@Override
					public void onFocusChange(View v, boolean gotFocus) {
						if(!gotFocus)
						{
							presenter.validateAccountNumber();
						}
					}
				});
			}
		});
    	thread.start();
    }

    /**
     * Convierte una lista de errores en el formato html
     * necesario para mostrarlo en el metodo setError
     * @param validationErrors
     * @return
     */
	public String getHTMLListFromErrors(List<String> validationErrors)
	{
		StringBuilder str = new StringBuilder("<font color='#006086'><b>");
		int size = validationErrors.size();
		if(size==1)
			return str.append(validationErrors.get(0)).append("</b></font>").toString();
		for (int i = 0; i < size; i++) {
			str.append("● ").append(validationErrors.get(i));
			str.append((i<size-1?"<br/>":""));
		}
		str.append("</b></font>");
		return str.toString();
	}

	
	public void btnRegisterAccountClick(View view)
	{
		presenter.processAccountData();
	}
    
    //#region  Interface IRegisterAccount methods

	@Override
	public void setNUSErrors(final List<String> validationErrors) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				if(validationErrors.size()>0)
					txtNUS.setError(Html.fromHtml(getHTMLListFromErrors(validationErrors)));
				else txtNUS.setError(null);
			}
		});		
	}
	
	@Override
	public void setAccountNumberErrors(final List<String> validationErrors) {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				if(validationErrors.size()>0)
					txtAccountNumber.setError(Html.fromHtml(getHTMLListFromErrors(validationErrors)));
				else txtAccountNumber.setError(null);
			}
		});		
	}
	
	@Override
	public String getNUS() {
		return txtNUS.getText().toString();
	}

	@Override
	public String getAccountNumber() {
		return txtAccountNumber.getText().toString().replace("-", "").trim();
	}
	

	@Override
	public String getIMEI() {
		return ((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	}

	@Override
	public String getPhoneNumber() {
		String phoneNumber = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
		return phoneNumber==null?"":phoneNumber;
	}
	
	@Override
	public String getNUSValidationRules() {
		return txtNUS.getTag().toString();
	}

	@Override
	public String getAccountNumberValidationRules() {
		return txtAccountNumber.getTag().toString();
	}
	
	@Override
	public void notifyAccountSuccessfulyRegistered() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				SuperToast.create(RegisterAccount.this, R.string.account_successfully_reg, SuperToast.Duration.LONG, 
					    Style.getStyle(Style.BLUE, SuperToast.Animations.FADE)).show();
				onBackPressed();
			}
		});
	}
	
	@Override
	public void showWSWaiting() {
		runOnUiThread(new Runnable() {
			@Override
			public void run(){
				waitingWSDialog = new ProgressDialogPro(RegisterAccount.this, R.style.Theme_FlavoredMaterialLight);
				waitingWSDialog.setMessage(RegisterAccount.this.getResources().getString(R.string.waiting_msg));
				waitingWSDialog.setCancelable(false);
				waitingWSDialog.setCanceledOnTouchOutside(false);
				waitingWSDialog.show();
			}
		});
	}

	@Override
	public void hideWSWaiting() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(waitingWSDialog!=null)
					waitingWSDialog.dismiss();
			}
		});
	}
	


	@Override
	public void notifyErrorsInFields() {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				Crouton.clearCroutonsForActivity(RegisterAccount.this);
				Crouton.makeText(RegisterAccount.this, R.string.errors_in_fields, croutonStyle, R.id.view_content).show();
			}
		});
	}
	
	@Override
	public void notifyAccountAlreadyRegistered() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlertDialogPro.Builder builder = new AlertDialogPro.Builder(RegisterAccount.this);
				builder.setTitle(R.string.account_already_reg_title)
				.setMessage(R.string.account_already_reg_msg)
				.setPositiveButton(R.string.btn_ok, null)
				.show();
			}
		});
	}
	
	@Override
	public void showRegistrationErrors(final List<Exception> errors) {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				if(errors.size()>0)
				{
					AlertDialogPro.Builder builder = new AlertDialogPro.Builder(RegisterAccount.this);
					builder.setTitle(R.string.errors_on_register_title)
					.setMessage(MessageListFormatter.fotmatHTMLFromErrors(errors))
					.setPositiveButton(R.string.btn_ok, null)
					.show();
				}
			}
		});
	}
	
	@Override
	public PreferencesManager getPreferences() {
		return new PreferencesManager(getApplicationContext());
	}
	
	@Override
	public GCMTokenRequester getGCMTokenRequester() {
		return new GCMTokenRequester(this);
	}

	@Override
	public WSTokenRequester getWSTokenRequester() {
		return new WSTokenRequester(this);
	}

	
	//#endregion
}
