package com.elfec.ssc.view;

import java.util.Currency;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import com.elfec.ssc.R;
import com.elfec.ssc.presenter.ViewAccountDetailsPresenter;
import com.elfec.ssc.presenter.views.IViewAccountDetails;

import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewAccountDetails extends ActionBarActivity implements IViewAccountDetails{

	public boolean horizontal;
	private ViewAccountDetailsPresenter presenter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_account_details);
		presenter = new ViewAccountDetailsPresenter(this);
		LinearLayout l =(LinearLayout)findViewById(R.id.test_layout);
		l.setOrientation(LinearLayout.HORIZONTAL);
		horizontal=true;
	}
	
	public void pruebita(View view)
	{
		LinearLayout l =(LinearLayout)findViewById(R.id.test_layout);
		TextView t1=(TextView) findViewById(R.id.prueba1);
		TextView t2=(TextView) findViewById(R.id.prueba2);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		int width = display.getWidth();  // deprecated
		int height = display.getHeight();  // deprecated
		TelephonyManager manager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        if(manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE)
		t1.setText("Soy una tablet");
        else
        	
		t1.setText("Soy un fono");
		if(horizontal)
		{
		
			l.setOrientation(LinearLayout.VERTICAL);
			 LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) t1.getLayoutParams();
			 params.weight = 0;
			 t1.setLayoutParams(params);
			 LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) t2.getLayoutParams();
			 params.weight = 0;
			 t2.setLayoutParams(params);
		}	
		else
		{
			l.setOrientation(LinearLayout.HORIZONTAL);
			 LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) t1.getLayoutParams();
			 params.weight = 1.0f;
			 t1.setLayoutParams(params);
			 LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) t2.getLayoutParams();
			 params.weight = 1.0f;
			 t2.setLayoutParams(params);
		}
		horizontal=!horizontal;
		
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
