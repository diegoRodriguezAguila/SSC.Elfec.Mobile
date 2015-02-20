package com.elfec.ssc.view;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.helpers.TextFormater;
import com.elfec.ssc.helpers.utils.AccountFormatter;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Debt;
import com.elfec.ssc.model.Usage;
import com.elfec.ssc.model.enums.AccountEnergySupplyStatus;
import com.elfec.ssc.presenter.ViewAccountDetailsPresenter;
import com.elfec.ssc.presenter.views.IViewAccountDetails;
import com.elfec.ssc.view.adapters.DebtAdapter;
import com.elfec.ssc.view.adapters.ViewUsageAdapter;
import com.elfec.ssc.view.controls.LayoutListView;

public class ViewAccountDetails extends ActionBarActivity implements IViewAccountDetails{

	public boolean horizontal;
	private ViewAccountDetailsPresenter presenter;
	private View accountSeparator;
	private LayoutListView LVAccountDebts;
	private NumberFormat nf;
	private LayoutListView usageList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_account_details);
		usageList=(LayoutListView)findViewById(R.id.list_usage);
		initializeDecimalFormater();
		LVAccountDebts = (LayoutListView) findViewById(R.id.listview_account_debts);
		presenter = new ViewAccountDetailsPresenter(this, (Account)getIntent().getSerializableExtra("SelectedAccount"));
		accountSeparator = findViewById(R.id.account_separator);
		setOrientation();
		presenter.setFields();
		presenter.getUsage();
	}

	private void initializeDecimalFormater() {
		nf = DecimalFormat.getInstance();
		DecimalFormatSymbols customSymbol = new DecimalFormatSymbols();
		customSymbol.setDecimalSeparator(',');
		customSymbol.setGroupingSeparator('.');
		((DecimalFormat)nf).setDecimalFormatSymbols(customSymbol);
		nf.setGroupingUsed(true);
	}
	
	/**
	 * Asigna el tipo de orientación para el detalle de las cuentas y de las deudas
	 */
	public void setOrientation()
	{
		LinearLayout l =(LinearLayout)findViewById(R.id.dynamic_layout);
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
			 LinearLayout.LayoutParams vParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)(2*displayMetrics.density));
			 vParams.setMargins(0, (int)(10*displayMetrics.density), 0, (int)(10*displayMetrics.density));
			 accountSeparator.setLayoutParams(vParams);
		}	
		else
		{
			 l.setOrientation(LinearLayout.HORIZONTAL);
			 LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) t1.getLayoutParams();
			 params.weight = 0.5f;
			 t1.setLayoutParams(params);
			 LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) t2.getLayoutParams();
			 params.weight = 0.5f;
			 t2.setLayoutParams(params1);
			 LinearLayout.LayoutParams vParams = new LinearLayout.LayoutParams((int)(2*displayMetrics.density), LinearLayout.LayoutParams.MATCH_PARENT);
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

	//#region Interface Methods
	
	@Override
	public void setAccountNumber(String accountNumber) {
		((TextView)findViewById(R.id.txt_account_number)).setText(AccountFormatter.formatAccountNumber(accountNumber));
	}

	@Override
	public void setNUS(String nus) {
		((TextView)findViewById(R.id.txt_nus)).setText(nus);
	}

	@Override
	public void setOwnerClient(String ownerClient) {
		((TextView)findViewById(R.id.txt_owner_client)).setText(TextFormater.capitalize(ownerClient));
	}

	@Override
	public void setEnergySupplyStatus(
			AccountEnergySupplyStatus energySuppluStatus) {
		((TextView)findViewById(R.id.txt_account_status)).setText(energySuppluStatus.toString());
	}

	@Override
	public void setTotalDebt(BigDecimal totalDebt) {
		TextView txtTotalAmount = ((TextView) findViewById(R.id.total_amount));
		if (totalDebt.compareTo(BigDecimal.ZERO) == 0)
		{
			((LinearLayout)findViewById(R.id.layout_decimal_debt))
				.setVisibility(View.GONE);
			txtTotalAmount.setTextSize(18);
			txtTotalAmount.setText(R.string.lbl_no_debts);
		}
		else
		{
			txtTotalAmount.setText(nf.format
					(totalDebt.toBigInteger().doubleValue()));
			
			((TextView) findViewById(R.id.total_amount_decimal))
			.setText((totalDebt.remainder(BigDecimal.ONE).multiply(new BigDecimal("100"))
					.setScale(0, RoundingMode.CEILING).toString()));
		}
	}

	@Override
	public void showUsage(final List<Usage> usage) {
		usageList.setAdapter(new ViewUsageAdapter(ViewAccountDetails.this, R.layout.usage_row, usage));
	}
	@Override
	public void showDebts(List<Debt> debts) {
		LVAccountDebts.setAdapter(new DebtAdapter(this, R.layout.debt_list_item, debts));
	}
	
	//#endregion
}
