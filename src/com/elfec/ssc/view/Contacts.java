package com.elfec.ssc.view;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.presenter.ContactPresenter;
import com.elfec.ssc.presenter.views.IContact;

public class Contacts extends ActionBarActivity implements IContact {

	ContactPresenter presenter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		presenter=new ContactPresenter(this);
		presenter.setDefaultData();
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
			final String webPage,final String facebook) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				((TextView)findViewById(R.id.txt_marker_phone)).setText(phone);
				((TextView)findViewById(R.id.txt_marker_address)).setText(address);
			}
		});
		
	}

	
}
