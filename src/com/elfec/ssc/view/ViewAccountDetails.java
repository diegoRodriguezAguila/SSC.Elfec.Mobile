package com.elfec.ssc.view;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import com.elfec.ssc.R;
import com.elfec.ssc.presenter.ViewAccountDetailsPresenter;
import com.elfec.ssc.presenter.views.IViewAccountDetails;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;

public class ViewAccountDetails extends ActionBarActivity implements IViewAccountDetails{

	private ViewAccountDetailsPresenter presenter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_account_details);
		presenter = new ViewAccountDetailsPresenter(this);
	}

	@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_account_details, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
	    finish();//go back to the previous Activity
	    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);  
	}
}
