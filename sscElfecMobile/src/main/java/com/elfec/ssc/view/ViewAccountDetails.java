package com.elfec.ssc.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.businesslogic.webservices.WSTokenRequester;
import com.elfec.ssc.helpers.GoogleMapsHelper;
import com.elfec.ssc.helpers.ui.ButtonClicksHelper;
import com.elfec.ssc.helpers.utils.AccountFormatter;
import com.elfec.ssc.helpers.utils.TextFormatter;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Debt;
import com.elfec.ssc.model.Usage;
import com.elfec.ssc.model.enums.AccountEnergySupplyStatus;
import com.elfec.ssc.presenter.ViewAccountDetailsPresenter;
import com.elfec.ssc.presenter.views.IViewAccountDetails;
import com.elfec.ssc.view.adapters.DebtAdapter;
import com.elfec.ssc.view.adapters.ViewUsageAdapter;
import com.elfec.ssc.view.controls.widget.LayoutListView;

import org.apache.commons.lang3.text.WordUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ViewAccountDetails extends AppCompatActivity implements
		IViewAccountDetails {

	public static final String SELECTED_ACCOUNT_ID = "SelectedAccountId";

	private ViewAccountDetailsPresenter presenter;

	public boolean horizontal;
	private Toolbar toolbar;
	private LayoutListView LVAccountDebts;
	private NumberFormat nf;
	private LayoutListView usageList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_account_details);
		usageList = (LayoutListView) findViewById(R.id.list_usage);
		LVAccountDebts = (LayoutListView) findViewById(R.id.listview_account_debts);
		initializeDecimalFormater();
		// Attaching the layout to the toolbar object
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		((TextView) toolbar.findViewById(R.id.toolbar_title))
				.setText(R.string.view_account_details_title);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		presenter = new ViewAccountDetailsPresenter(this, getIntent()
				.getLongExtra(SELECTED_ACCOUNT_ID, -1));
		presenter.setFields();
		presenter.getUsage();

	}

	private void initializeDecimalFormater() {
		nf = DecimalFormat.getInstance();
		DecimalFormatSymbols customSymbol = new DecimalFormatSymbols();
		customSymbol.setDecimalSeparator(',');
		customSymbol.setGroupingSeparator('.');
		((DecimalFormat) nf).setDecimalFormatSymbols(customSymbol);
		nf.setGroupingUsed(true);
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
		finish();// go back to the previous Activity
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
	}

	/**
	 * Click de la direccion
	 * 
	 * @param v
	 */
	public void goToAddressClick(View v) {
		if (ButtonClicksHelper.canClickButton()) {
			presenter.goToAddress();
		}
	}

	// #region Interface Methods

	@Override
	public void setAccountNumber(String accountNumber) {
		((TextView) findViewById(R.id.txt_account_number))
				.setText(AccountFormatter.formatAccountNumber(accountNumber));
	}

	@Override
	public void setNUS(String nus) {
		((TextView) findViewById(R.id.txt_nus)).setText(nus);
	}

	@Override
	public void setOwnerClient(String ownerClient) {
		((TextView) findViewById(R.id.txt_owner_client)).setText(TextFormatter
				.capitalize(ownerClient));
	}

	@Override
	public void setClientAddress(String address) {
		((TextView) findViewById(R.id.txt_client_address)).setText(WordUtils
				.capitalizeFully(address, new char[]{'.', ' '}));
	}

	@Override
	public void setEnergySupplyStatus(
			AccountEnergySupplyStatus energySuppluStatus) {
		((TextView) findViewById(R.id.txt_account_status))
				.setText(energySuppluStatus.toString());
	}

	@Override
	public void setTotalDebt(final BigDecimal totalDebt) {
		final TextView txtTotalAmount = ((TextView) findViewById(R.id.total_amount));
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (totalDebt.compareTo(BigDecimal.ZERO) == 0) {
					(findViewById(R.id.layout_decimal_debt))
							.setVisibility(View.GONE);
					(findViewById(R.id.lbl_debt_detail))
							.setVisibility(View.GONE);
					txtTotalAmount.setTextSize(18);
					txtTotalAmount.setText(R.string.lbl_no_debts);
				} else {
					txtTotalAmount.setText(nf.format(totalDebt.toBigInteger()
							.doubleValue()));

					String decimalPart = (totalDebt.remainder(BigDecimal.ONE)
							.multiply(new BigDecimal("100"))
							.setScale(0, RoundingMode.CEILING).toString());
					if (decimalPart.equals("0"))
						decimalPart += "0";
					((TextView) findViewById(R.id.total_amount_decimal))
							.setText(decimalPart);
				}
			}
		});
	}

	@Override
	public void showUsage(final List<Usage> usage) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				usageList.setAdapter(new ViewUsageAdapter(
						ViewAccountDetails.this, R.layout.usage_row, usage));
			}
		});

	}

	@Override
	public void showDebts(final List<Debt> debts) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				LVAccountDebts
						.setAdapter(new DebtAdapter(ViewAccountDetails.this,
								R.layout.debt_list_item, debts));
			}
		});
	}

	@Override
	public WSTokenRequester getWSTokenRequester() {
		return new WSTokenRequester(this);
	}

	@Override
	public void navigateToAddress(Account account) {
		GoogleMapsHelper.launchGoogleMaps(this, account.getLatitude(),
				account.getLongitude(), account.getAddress());
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
	}

	// #endregion
}
