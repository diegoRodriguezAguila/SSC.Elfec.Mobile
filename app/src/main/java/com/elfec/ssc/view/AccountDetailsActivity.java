package com.elfec.ssc.view;

import android.os.Bundle;
import android.support.annotation.StringRes;
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

public class AccountDetailsActivity extends BaseActivity implements
        IAccountDetailsView {

    public static final String SELECTED_ACCOUNT_NUS = "selected_account_nus";

    @BindView(R.id.txt_account_number)
    protected TextView mTxtAccountNumber;
    @BindView(R.id.txt_nus)
    protected TextView mTxtNus;
    @BindView(R.id.txt_owner_client)
    protected TextView mTxtOwner;
    @BindView(R.id.txt_client_address)
    protected TextView mTxtAddress;
    @BindView(R.id.txt_account_status)
    protected TextView mTxtAccountStatus;
    @BindView(R.id.total_amount)
    protected TextView txtTotalAmount;
    @BindView(R.id.total_amount_decimal)
    protected TextView txtTotalAmountDecimal;
    @BindView(R.id.listview_account_debts)
    protected LayoutListView mDebtList;
    @BindView(R.id.list_usage)
    protected LayoutListView mUsageList;
    protected NumberFormat mNumFormat;

    private AccountDetailsPresenter mPresenter;
    public boolean horizontal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account_details);
        ButterKnife.bind(this);
        initializeDecimalFormatter();
        mPresenter = new AccountDetailsPresenter(this);
        Bundle extras = getIntent().getExtras();
        if (extras == null) return;
        String nus = getIntent()
                .getStringExtra(SELECTED_ACCOUNT_NUS);
        if (nus == null) return;
        mPresenter.loadAccount(nus);
        mPresenter.loadUsages(nus);
    }

    @Override
    public void releasePresenter() {
        mPresenter.close();
        mPresenter = null;
    }

    private void initializeDecimalFormatter() {
        mNumFormat = DecimalFormat.getInstance();
        DecimalFormatSymbols customSymbol = new DecimalFormatSymbols();
        customSymbol.setDecimalSeparator(',');
        customSymbol.setGroupingSeparator('.');
        ((DecimalFormat) mNumFormat).setDecimalFormatSymbols(customSymbol);
        mNumFormat.setGroupingUsed(true);
    }

    public void showDebts(final List<Debt> debts) {
        runOnUiThread(() -> mDebtList
                .setAdapter(new DebtAdapter(AccountDetailsActivity.this,
                        R.layout.debt_list_item, debts)));
    }

    public void setTotalDebt(final BigDecimal totalDebt) {
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
                txtTotalAmountDecimal.setText(decimalPart);
            }
        });
    }

    /**
     * Click de la direccion
     *
     * @param v view
     */
    public void goToAddressClick(View v) {
        if (ButtonClicksHelper.canClickButton()) {
            mPresenter.goToAddress();
        }
    }

    // #region Interface Methods

    @Override
    public void onUsageLoaded(final List<Usage> usage) {
        runOnUiThread(() -> {
            if (usage == null || usage.size() == 0) return;
            mUsageList.setAdapter(new ViewUsageAdapter(
                    AccountDetailsActivity.this, R.layout.usage_row, usage));
        });

    }

    @Override
    public void navigateToAddress(Account account) {
        GoogleMapsHelper.launchGoogleMaps(this, account.getLatitude(),
                account.getLongitude(), account.getAddress());
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    @Override
    public void onLoading(@StringRes int message) {

    }

    @Override
    public void onError(Throwable e) {
        runOnUiThread(() -> SuperToast.create(AccountDetailsActivity.this,
                e.getMessage(), SuperToast.Duration.SHORT,
                Style.getStyle(Style.BLUE, SuperToast.Animations.FADE))
                .show());
    }

    @Override
    public void onLoaded(Account account) {
        runOnUiThread(() -> {
            mTxtAccountNumber.setText(AccountFormatter
                    .formatAccountNumber(account.getAccountNumber()));
            mTxtNus.setText(account.getNus());
            mTxtOwner.setText(TextFormatter
                    .capitalize(account.getAccountOwner()));
            mTxtAddress.setText(WordUtils
                    .capitalizeFully(account.getAddress(), new char[]{'.', ' '}));
            mTxtAccountStatus
                    .setText(account.getEnergySupplyStatus().toString());
            showDebts(account.getDebts());
            setTotalDebt(account.getTotalDebt());
        });
    }

    // #endregion
}
