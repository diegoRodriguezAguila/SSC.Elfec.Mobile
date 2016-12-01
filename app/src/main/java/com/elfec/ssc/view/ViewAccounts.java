package com.elfec.ssc.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.helpers.ViewPresenterManager;
import com.elfec.ssc.helpers.ui.ButtonClicksHelper;
import com.elfec.ssc.helpers.utils.MessageListFormatter;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.presenter.ViewAccountsPresenter;
import com.elfec.ssc.presenter.views.IViewAccounts;
import com.elfec.ssc.view.adapters.AccountAdapter;
import com.elfec.ssc.view.controls.xlistview.XListView;
import com.elfec.ssc.view.controls.xlistview.XListView.IXListViewListener;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ViewAccounts extends AppCompatActivity implements IViewAccounts {

    private ViewAccountsPresenter presenter;

    private boolean mIsDestroyed;

    protected @BindView(R.id.accounts_list) XListView mListViewAccounts;
    protected @BindView(R.id.layout_loading_accounts) View mLayoutLoadingAccounts;
    protected @BindView(R.id.layout_how_to_add_accounts)
    ScrollView mLayoutHowToAddAccounts;
    protected @BindView(R.id.lbl_no_accounts_registered) TextView mTxtNoAccounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_accounts);
        mIsDestroyed = false;
        presenter = new ViewAccountsPresenter(this);
        ButterKnife.bind(this);
        mListViewAccounts.setPullLoadEnable(false);
        mListViewAccounts.setPullRefreshEnable(true);
        mListViewAccounts.setXListViewListener(new IXListViewListener() {
            @Override
            public void onRefresh() {
                presenter.refreshAccountsRemotely();
            }

            @Override
            public void onLoadMore() {

            }
        });
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
        mListViewAccounts
                .setOnItemLongClickListener((adapter, view, position, arg3) -> {
                    dialogRemove(position);
                    return true;
                });
        mListViewAccounts.setOnItemClickListener((adapter, view, pos, arg3) -> {
            if (ButtonClicksHelper.canClickButton()) {
                Intent i = new Intent(ViewAccounts.this,
                        ViewAccountDetails.class);
                Long id = ((Account) adapter.getItemAtPosition(pos))
                        .getId();
                i.putExtra(ViewAccountDetails.SELECTED_ACCOUNT_ID, id);
                startActivity(i);
                overridePendingTransition(R.anim.slide_left_in,
                        R.anim.slide_left_out);
            }
        });

        presenter.loadAccounts(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewPresenterManager.setPresenter(presenter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsDestroyed = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewPresenterManager.setPresenter(null);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void btnRegisterAccountClick(View view) {
        if (ButtonClicksHelper.canClickButton()) {
            startActivityForResult(new Intent(ViewAccounts.this, RegisterAccount.class),
                    RegisterAccount.REGISTER_REQUEST_CODE);
            overridePendingTransition(R.anim.slide_left_in,
                    R.anim.slide_left_out);
        }
    }

    @Override
    public void onBackPressed() {
        finish();// go back to the previous Activity
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RegisterAccount.REGISTER_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                boolean updateList = data.getBooleanExtra(RegisterAccount.REGISTER_SUCCESS, false);
                if(updateList){
                    presenter.loadAccounts(true);
                }

            }
        }
    }

    //region interface Methods

    @Override
    public void showAccounts(final List<Account> accounts) {
        runOnUiThread(() -> {
            if (!mIsDestroyed) {
                if (accounts != null && accounts.size() > 0) {
                    mListViewAccounts.setAdapter(new AccountAdapter(
                            ViewAccounts.this, R.layout.account_list_item,
                            accounts));
                    mLayoutHowToAddAccounts.setVisibility(View.GONE);
                    mTxtNoAccounts.setVisibility(View.GONE);
                    mListViewAccounts.setVisibility(View.VISIBLE);
                } else {
                    mLayoutHowToAddAccounts.setVisibility(View.VISIBLE);
                    mTxtNoAccounts.setVisibility(View.VISIBLE);
                    mListViewAccounts.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void showAccountDeleted() {
        runOnUiThread(() -> {
            if (!mIsDestroyed) {
                SuperToast.cancelAllSuperToasts();
                SuperToast.create(
                        ViewAccounts.this,
                        R.string.account_successfully_deleted,
                        SuperToast.Duration.LONG,
                        Style.getStyle(Style.BLUE,
                                SuperToast.Animations.FADE)).show();
            }
        });
    }

    @Override
    public void displayErrors(final List<Exception> errors) {
        runOnUiThread(() -> {
            if (!mIsDestroyed && errors.size() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        ViewAccounts.this);
                builder.setTitle(R.string.errors_on_delete_account_title)
                        .setMessage(
                                MessageListFormatter
                                        .formatHTMLFromErrors(ViewAccounts.this, errors))
                        .setPositiveButton(R.string.btn_ok, null).show();
            }
        });
    }

    @Override
    public void dialogRemove(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.delete_account_title)
                .setMessage(R.string.delete_account_msg)
                .setPositiveButton(R.string.btn_confirm, (dialog, which) -> {
                    Account account = (Account) mListViewAccounts
                            .getAdapter().getItem(position);
                    showWaiting();
                    presenter.removeAccount(account.getNus());
                }).setNegativeButton(R.string.btn_cancel, null);
        AlertDialog dialog = builder.create();
        dialog.show();
        ((TextView)dialog.findViewById(android.R.id.message))
                .setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void showWaiting() {

    }

    @Override
    public void hideWaiting() {
        runOnUiThread(() -> {
            if (!mIsDestroyed) {
                mListViewAccounts.stopRefresh();
                mLayoutLoadingAccounts.setVisibility(
                        View.GONE);
            }

        });
    }

    @Override
    public void showViewAccountsErrors(final List<Exception> errors) {
        runOnUiThread(() -> {
            if (!mIsDestroyed && errors.size() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        ViewAccounts.this);
                builder.setTitle(R.string.errors_on_download_accounts_title)
                        .setMessage(
                                MessageListFormatter
                                        .formatHTMLFromErrors(ViewAccounts.this, errors))
                        .setPositiveButton(R.string.btn_ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
                ((TextView)dialog.findViewById(android.R.id.message))
                        .setMovementMethod(LinkMovementMethod.getInstance());
            }
        });
    }

    //endregion

}