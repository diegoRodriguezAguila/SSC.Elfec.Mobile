package com.elfec.ssc.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.helpers.GoogleMapsHelper;
import com.elfec.ssc.helpers.ui.ButtonClicksHelper;
import com.elfec.ssc.helpers.utils.AccountFormatter;
import com.elfec.ssc.helpers.utils.TextFormatter;
import com.elfec.ssc.helpers.utils.WordUtils;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Debt;
import com.elfec.ssc.model.Usage;
import com.elfec.ssc.model.enums.AccountEnergySupplyStatus;
import com.elfec.ssc.presenter.AccountDetailsPresenter;
import com.elfec.ssc.presenter.views.IAccountDetailsView;
import com.elfec.ssc.view.adapters.DebtAdapter;
import com.elfec.ssc.view.adapters.ViewUsageAdapter;
import com.elfec.ssc.view.controls.widget.LayoutListView;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AccountDetailsActivity extends AppCompatActivity implements
        IAccountDetailsView {

    public static final String SELECTED_ACCOUNT_ID = "SelectedAccountId";

    private AccountDetailsPresenter presenter;

    public boolean horizontal;
    protected
    @BindView(R.id.listview_account_debts)
    LayoutListView mListViewDebts;
    protected
    @BindView(R.id.list_usage)
    LayoutListView mListViewUsages;

    protected NumberFormat mNumFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account_details);
        ButterKnife.bind(this);
        initializeDecimalFormatter();
        presenter = new AccountDetailsPresenter(this, getIntent()
                .getLongExtra(SELECTED_ACCOUNT_ID, -1));
        presenter.setFields();
        presenter.getUsages();

    }

    private void initializeDecimalFormatter() {
        mNumFormat = DecimalFormat.getInstance();
        DecimalFormatSymbols customSymbol = new DecimalFormatSymbols();
        customSymbol.setDecimalSeparator(',');
        customSymbol.setGroupingSeparator('.');
        ((DecimalFormat) mNumFormat).setDecimalFormatSymbols(customSymbol);
        mNumFormat.setGroupingUsed(true);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        finish();// go back to the previous Activity
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    /**
     * Click de la direccion
     *
     * @param v view
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
    public void setNus(String nus) {
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
            AccountEnergySupplyStatus energySupplyStatus) {
        ((TextView) findViewById(R.id.txt_account_status))
                .setText(energySupplyStatus.toString());
    }

    @Override
    public void setTotalDebt(final BigDecimal totalDebt) {
        final TextView txtTotalAmount = ((TextView) findViewById(R.id.total_amount));
        runOnUiThread(() -> {
            if (totalDebt.compareTo(BigDecimal.ZERO) == 0) {
                (findViewById(R.id.layout_decimal_debt))
                        .setVisibility(View.GONE);
                (findViewById(R.id.lbl_debt_detail))
                        .setVisibility(View.GONE);
                txtTotalAmount.setTextSize(18);
                txtTotalAmount.setText(R.string.lbl_no_debts);
            } else {
                txtTotalAmount.setText(mNumFormat.format(totalDebt.toBigInteger()
                        .doubleValue()));

                String decimalPart = (totalDebt.remainder(BigDecimal.ONE)
                        .multiply(new BigDecimal("100"))
                        .setScale(0, RoundingMode.CEILING).toString());
                if (decimalPart.equals("0"))
                    decimalPart += "0";
                ((TextView) findViewById(R.id.total_amount_decimal))
                        .setText(decimalPart);
            }
        });
    }

    @Override
    public void showUsage(final List<Usage> usage) {
        runOnUiThread(() -> {
            if (usage == null || usage.size() == 0) return;
            mListViewUsages.setAdapter(new ViewUsageAdapter(
                    AccountDetailsActivity.this, R.layout.usage_row, usage));
        });

    }

    @Override
    public void showDebts(final List<Debt> debts) {
        runOnUiThread(() -> mListViewDebts
                .setAdapter(new DebtAdapter(AccountDetailsActivity.this,
                        R.layout.debt_list_item, debts)));
    }


    @Override
    public void navigateToAddress(Account account) {
        GoogleMapsHelper.launchGoogleMaps(this, account.getLatitude(),
                account.getLongitude(), account.getAddress());
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    @Override
    public void onError(Throwable e) {
        runOnUiThread(() -> SuperToast.create(AccountDetailsActivity.this,
                e.getMessage(), SuperToast.Duration.SHORT,
                Style.getStyle(Style.BLUE, SuperToast.Animations.FADE))
                .show());
    }

    // #endregion
}
