package com.elfec.ssc.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.helpers.HtmlCompat;
import com.elfec.ssc.helpers.ViewPresenterManager;
import com.elfec.ssc.helpers.ui.ButtonClicksHelper;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.presenter.AccountsPresenter;
import com.elfec.ssc.presenter.views.IAccountsView;
import com.elfec.ssc.view.adapters.AccountAdapter;
import com.elfec.ssc.view.controls.xlistview.XListView;
import com.elfec.ssc.view.controls.xlistview.XListView.IXListViewListener;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountsActivity extends BaseActivity implements IAccountsView {

    private AccountsPresenter mPresenter;

    protected
    @BindView(R.id.accounts_list)
    XListView mAccountList;
    protected
    @BindView(R.id.layout_loading_accounts)
    View mLayoutLoadingAccounts;
    protected
    @BindView(R.id.layout_how_to_add_accounts)
    ScrollView mLayoutHowToAddAccounts;
    protected
    @BindView(R.id.lbl_no_accounts_registered)
    TextView mTxtNoAccounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_accounts);
        mPresenter = new AccountsPresenter(this);
        ButterKnife.bind(this);
        mAccountList.setPullLoadEnable(false);
        mAccountList.setPullRefreshEnable(true);
        mAccountList.setXListViewListener(new IXListViewListener() {
            @Override
            public void onRefresh() {
                mPresenter.refreshAccounts();
            }

            @Override
            public void onLoadMore() {

            }
        });
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
        mAccountList.setOnItemLongClickListener((adapter, view, position, arg3) -> {
            dialogRemove(position);
            return true;
        });
        mAccountList.setOnItemClickListener((adapter, view, pos, arg3) -> {
            if (!ButtonClicksHelper.canClickButton()) return;
            Intent i = new Intent(AccountsActivity.this,
                    AccountDetailsActivity.class);
            i.putExtra(AccountDetailsActivity.SELECTED_ACCOUNT_NUS,
                    ((Account) adapter.getItemAtPosition(pos)).getNus());
            startActivity(i);
            overridePendingTransition(R.anim.slide_left_in,
                    R.anim.slide_left_out);
        });

        mPresenter.loadAccounts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewPresenterManager.setPresenter(mPresenter);
    }

    @Override
    public void releasePresenter() {
        mPresenter.close();
        mPresenter = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewPresenterManager.setPresenter(null);
    }

    public void hideWaiting() {
        runOnUiThread(() -> {
            if (isDestroyed()) return;
            mAccountList.stopRefresh();
            mLayoutLoadingAccounts.setVisibility(
                    View.GONE);
        });
    }

    public void btnRegisterAccountClick(View view) {
        if (ButtonClicksHelper.canClickButton()) {
            startActivityForResult(new Intent(AccountsActivity.this, RegisterAccountActivity.class),
                    RegisterAccountActivity.REGISTER_REQUEST_CODE);
            overridePendingTransition(R.anim.slide_left_in,
                    R.anim.slide_left_out);
        }
    }

    public void dialogRemove(final int position) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_account_title)
                .setMessage(R.string.delete_account_msg)
                .setPositiveButton(R.string.btn_confirm, (dialog, which) -> {
                    mPresenter.removeAccount((Account) mAccountList
                            .getAdapter().getItem(position));
                }).setNegativeButton(R.string.btn_cancel, null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RegisterAccountActivity.REGISTER_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                boolean updateList = data.getBooleanExtra(RegisterAccountActivity.REGISTER_SUCCESS, false);
                if (updateList) {
                    mPresenter.loadAccounts();
                }

            }
        }
    }

    //region interface Methods

    @Override
    public void onLoaded(List<Account> accounts) {
        runOnUiThread(() -> {
            if (isDestroyed()) return;
            hideWaiting();
            if (accounts != null && accounts.size() > 0) {
                mAccountList.setAdapter(new AccountAdapter(
                        AccountsActivity.this, R.layout.account_list_item,
                        accounts));
                mLayoutHowToAddAccounts.setVisibility(View.GONE);
                mTxtNoAccounts.setVisibility(View.GONE);
                mAccountList.setVisibility(View.VISIBLE);
            } else {
                mLayoutHowToAddAccounts.setVisibility(View.VISIBLE);
                mTxtNoAccounts.setVisibility(View.VISIBLE);
                mAccountList.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onLoading(@StringRes int message) {

    }

    @Override
    public void onProcessing(@StringRes int message) {

    }

    @Override
    public void onError(Throwable e) {
        runOnUiThread(() -> {
            if (isDestroyed()) return;
            hideWaiting();
            AlertDialog dialog = new AlertDialog.Builder(
                    AccountsActivity.this)
                    .setTitle(R.string.errors_on_download_accounts_title)
                    .setMessage(HtmlCompat.fromHtml(e.getMessage()))
                    .setPositiveButton(R.string.btn_ok, null).create();
            dialog.show();
            TextView txtMessage = (TextView) dialog.findViewById(android.R.id.message);
            if (txtMessage != null) {
                txtMessage.setMovementMethod(LinkMovementMethod.getInstance());
            }
        });
    }

    @Override
    public void onSuccess(Account account) {
        if (isDestroyed()) return;
        SuperToast.cancelAllSuperToasts();
        SuperToast.create(
                AccountsActivity.this,
                getString(R.string.account_successfully_deleted, account.getNus()),
                SuperToast.Duration.LONG,
                Style.getStyle(Style.BLUE,
                        SuperToast.Animations.FADE)).show();
    }

    //endregion

}
