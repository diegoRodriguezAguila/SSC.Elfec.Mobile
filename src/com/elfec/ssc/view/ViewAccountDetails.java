package com.elfec.ssc.view;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import com.elfec.ssc.R;
import com.elfec.ssc.presenter.ViewAccountDetailsPresenter;
import com.elfec.ssc.presenter.views.IViewAccountDetails;

public class ViewAccountDetails extends ActionBarActivity implements IViewAccountDetails{

	public boolean horizontal;
	private ViewAccountDetailsPresenter presenter;
	private View accountSeparator;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_account_details);
		presenter = new ViewAccountDetailsPresenter(this);
		accountSeparator = findViewById(R.id.account_separator);
		setOrientation();
	}
	
	/**
	 * Asigna el tipo de orientación para el detalle de las cuentas y de las deudas
	 */
	public void setOrientation()
	{
		LinearLayout l =(LinearLayout)findViewById(R.id.test_layout);
		LinearLayout t1=(LinearLayout) findViewById(R.id.account_details_layout);
		LinearLayout t2=(LinearLayout) findViewById(R.id.account_debts_layout);
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		int dpWidth = (int) (displayMetrics.widthPixels/displayMetrics.density);
		if(dpWidth<550 || displayMetrics.widthPixels<=480)
		{
		
			l.setOrientation(LinearLayout.VERTICAL);
			 LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) t1.getLayoutParams();
			 params.weight = 0;
			 t1.setLayoutParams(params);
			 LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) t2.getLayoutParams();
			 params.weight = 0;
			 t2.setLayoutParams(params1);
			 LinearLayout.LayoutParams vParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
			 vParams.setMargins(0, (int)(10*displayMetrics.density), 0, (int)(10*displayMetrics.density));
			 accountSeparator.setLayoutParams(vParams);
		}	
		else
		{
			l.setOrientation(LinearLayout.HORIZONTAL);
			 LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) t1.getLayoutParams();
			 params.weight = 1.0f;
			 t1.setLayoutParams(params);
			 LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) t2.getLayoutParams();
			 params.weight = 1.0f;
			 t2.setLayoutParams(params1);
			 LinearLayout.LayoutParams vParams = new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT);
			 vParams.setMargins((int)(10*displayMetrics.density), 0, (int)(10*displayMetrics.density), 0);
			 accountSeparator.setLayoutParams(vParams);
		}		
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
