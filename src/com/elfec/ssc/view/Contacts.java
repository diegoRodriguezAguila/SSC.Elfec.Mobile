package com.elfec.ssc.view;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.presenter.ContactPresenter;
import com.elfec.ssc.presenter.views.IContact;

public class Contacts extends AppCompatActivity implements IContact {

	ContactPresenter presenter;
	
	private Toolbar toolbar;
	private boolean isFirst = true;
	private String facebook;
	private String facebookId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		presenter=new ContactPresenter(this);
		toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar); 
        ((TextView)toolbar.findViewById(R.id.toolbar_title)).setText(R.string.contacts_title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
		presenter.setDefaultData();
		isFirst = true;
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		if(!isFirst)
			overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
		else isFirst = false;
	}
	
	@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
	
	@Override
	public void onBackPressed() {
	    finish();//go back to the previous Activity
	    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);  
	}

	@Override
	public void setData(final String phone,final String address,final String email,
			final String webPage,final String facebook, final String facebookId) {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				((TextView)findViewById(R.id.contact_phone)).setText(phone);
				((TextView)findViewById(R.id.contact_address)).setText(address);
				((TextView)findViewById(R.id.contact_email)).setText(email);
				((TextView)findViewById(R.id.contact_web)).setText(webPage);
				Contacts.this.facebook = facebook;
				Contacts.this.facebookId = facebookId;
			}
		});
		
	}
	
	public void contactPhoneClick(View view)
	{
		Intent dialIntent = new Intent(Intent.ACTION_DIAL);
		dialIntent.setData(Uri.parse("tel:"+((TextView)view).getText()));
        startActivity(dialIntent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
	}
	
	public void contactEmailClick(View view)
	{
		Intent webIntent = new Intent(Intent.ACTION_VIEW);
		webIntent.setData(Uri.parse(("mailto:"+((TextView)view).getText())));
        startActivity(webIntent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
	}
	
	public void contactWebClick(View view)
	{
		Intent webIntent = new Intent(Intent.ACTION_VIEW);
		webIntent.setData(Uri.parse(("http://"+((TextView)view).getText())));
        startActivity(webIntent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
	}
	
	public void contactFacebookClick(View view)
	{
		Intent facebookIntent = getFacebookIntent();
        startActivity(facebookIntent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
	}
	
	/**
	 * Obtiene el intent necesario para abrir la página de facebook, en caso de que no se tenga la aplicación
	 * la abre por defecto en un browser
	 * @return
	 */
	public Intent getFacebookIntent()
	{
		try {
		    this.getPackageManager().getPackageInfo("com.facebook.katana", 0);
		    return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/"+facebookId));
		   } catch (Exception e) {
		    return new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+facebook));
		   }
	}
	
}
