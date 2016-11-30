package com.elfec.ssc.view.adapters.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.helpers.utils.AccountFormatter;
import com.elfec.ssc.helpers.utils.TextFormatter;
import com.elfec.ssc.helpers.utils.WordUtils;
import com.elfec.ssc.model.Account;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ViewHolder para cuentas
 */
public class AccountViewHolder {
    protected @BindView(R.id.row_account_value) TextView mTxtAccountNumber;
    protected @BindView(R.id.row_nus_value) TextView mTxtNus;
    protected @BindView(R.id.row_name_value) TextView mTxtAccountOwner;
    protected @BindView(R.id.row_address_value) TextView mTxtAddress;
    protected @BindView(R.id.total_amount) TextView mTxtDebtTotalAmount;
    protected @BindView(R.id.total_amount_decimal) TextView mTxtDebtDecimalAmount;
    protected @BindView(R.id.layout_decimal_debt)
    LinearLayout mLayoutDebtDecimal;

    public AccountViewHolder(View convertView) {
        ButterKnife.bind(this, convertView);
    }

    /**
     * Asigna la información a los campos de la vista
     * @param account
     * @param numberFormat
     */
    public void bindAccount(Account account, NumberFormat numberFormat) {

        mTxtAccountNumber.setText(AccountFormatter.formatAccountNumber(account
                .getAccountNumber()));
        mTxtNus.setText(account.getNUS());
        mTxtAccountOwner.setText(TextFormatter.capitalize(account.getAccountOwner()));
        mTxtAddress.setText(WordUtils.capitalizeFully(account.getAddress(),
                new char[]{'.', ' '}));
        setAccountBalanceInformation(account, numberFormat);
    }

    private void setAccountBalanceInformation(Account account, NumberFormat numberFormat) {
        BigDecimal totalDebt = account.getTotalDebt();
        if (totalDebt.compareTo(BigDecimal.ZERO) == 0) {
            mLayoutDebtDecimal.setVisibility(View.GONE);
            mTxtDebtTotalAmount.setTextSize(18);
            mTxtDebtTotalAmount.setText(R.string.lbl_no_debts);
        } else {
            mLayoutDebtDecimal.setVisibility(View.VISIBLE);
            mTxtDebtTotalAmount.setTextSize(22);
            mTxtDebtTotalAmount.setText(numberFormat.format(account.getTotalDebt()
                    .toBigInteger().doubleValue()));

            String decimalPart = (account.getTotalDebt()
                    .remainder(BigDecimal.ONE).multiply(new BigDecimal("100"))
                    .setScale(0, RoundingMode.CEILING).toString());
            if (decimalPart.equals("0"))
                decimalPart += "0";
            mTxtDebtDecimalAmount.setText(decimalPart);
        }
    }
}
