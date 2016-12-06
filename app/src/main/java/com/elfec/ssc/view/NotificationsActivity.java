package com.elfec.ssc.view;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.helpers.ViewPresenterManager;
import com.elfec.ssc.helpers.ui.ButtonClicksHelper;
import com.elfec.ssc.model.Notification;
import com.elfec.ssc.presenter.NotificationsPresenter;
import com.elfec.ssc.presenter.views.INotificationsView;
import com.elfec.ssc.view.adapters.NotificationAdapter;
import com.elfec.ssc.view.animations.HeightAnimation;
import com.elfec.ssc.view.controls.xlistview.XListView;
import com.elfec.ssc.view.controls.xlistview.XListView.IXListViewListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsActivity extends BaseActivity implements
        INotificationsView {

    public enum ExpandStatus {
        COLLAPSED, HALF, FULL
    }

    private NotificationsPresenter mPresenter;

    private ExpandStatus outageStatus;
    private ExpandStatus accountsStatus;
    private int halfSize = -1;
    private int fullSize = -1;

    protected @BindView(R.id.outage_group) CheckBox mCheckOutageGroup;
    protected @BindView(R.id.outage_listview) XListView outageListView;
    protected @BindView(R.id.accounts_group) CheckBox mCheckAccountsGroup;
    protected @BindView(R.id.accounts_listview) XListView accountsListView;
    protected @BindView(R.id.lbl_no_outage_notifications) TextView mLblNoOutageNotifications;
    protected @BindView(R.id.lbl_no_account_notifications) TextView mLblNoAccountNotifications;
    protected @BindView(R.id.outage_listlayout) LinearLayout outageLayout;
    protected @BindView(R.id.accounts_listlayout) LinearLayout accountsLayout;

    private NotificationAdapter mOutagesAdapter;
    private NotificationAdapter mAccountsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        mPresenter = new NotificationsPresenter(this);
        ButterKnife.bind(this);
        outageStatus = ExpandStatus.COLLAPSED;
        accountsStatus = ExpandStatus.COLLAPSED;
        outageListView.setPullLoadEnable(false);
        accountsListView.setPullLoadEnable(false);
        initializeListListeners();
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

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadNotifications();
        ViewPresenterManager.setPresenter(mPresenter);
    }

    /**
     * Define las constantes de halfSize y fullSize para las listas
     */
    private void defineSizes() {
        if (halfSize == -1 || fullSize == -1) {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            ActionBar actionBar = getSupportActionBar();
            int screenHeight = displayMetrics.heightPixels;
            int actionBarHeight = actionBar.getHeight();
            float paddingTop = (6 * displayMetrics.density);
            float paddingBottom = (16 * displayMetrics.density);
            float extraMarginFull = (15 * displayMetrics.density);
            halfSize = (int) (((screenHeight - actionBarHeight - paddingBottom - paddingTop) / 2) - ((mCheckOutageGroup
                    .getHeight() * 1.5)));
            fullSize = (int) ((screenHeight - actionBarHeight - paddingBottom - paddingTop)
                    - (mCheckOutageGroup.getHeight() * 2.5) - extraMarginFull);
        }
    }

    /**
     * Inicia un hilo para asignar los listeners a ambas listas
     */
    private void initializeListListeners() {
        new Thread(() -> {
            setOnCheckListeners();
            setOnRefreshAndLoadListeners();
        }).start();
    }

    /**
     * Pone los check listeners para los cortes y las cuentas
     */
    private void setOnCheckListeners() {
        OnCheckedChangeListener checkListener = (buttonView, isChecked) -> redefineViewSizes();
        mCheckOutageGroup.setOnCheckedChangeListener(checkListener);
        mCheckAccountsGroup.setOnCheckedChangeListener(checkListener);
    }

    /**
     * según los checkbox marcados distribuye enl a pantalla las dos listas de
     * notificaciones
     */
    public void redefineViewSizes() {
        defineSizes();
        if (mCheckOutageGroup.isChecked() && mCheckAccountsGroup.isChecked()) {
            outageStatus = ExpandStatus.HALF;
            accountsStatus = ExpandStatus.HALF;
        }
        if (!mCheckOutageGroup.isChecked() && mCheckAccountsGroup.isChecked()) {
            outageStatus = ExpandStatus.COLLAPSED;
            accountsStatus = ExpandStatus.FULL;
        }
        if (mCheckOutageGroup.isChecked() && !mCheckAccountsGroup.isChecked()) {
            outageStatus = ExpandStatus.FULL;
            accountsStatus = ExpandStatus.COLLAPSED;
        }
        if (!mCheckOutageGroup.isChecked() && !mCheckAccountsGroup.isChecked()) {
            outageStatus = ExpandStatus.COLLAPSED;
            accountsStatus = ExpandStatus.COLLAPSED;
        }
        changeLinearLayoutHeight(outageLayout, outageStatus);
        changeLinearLayoutHeight(accountsLayout, accountsStatus);
    }

    public void btnDeleteOutageNotificationsClick(View view) {
        if (ButtonClicksHelper.canClickButton()) {
            (new AlertDialog.Builder(this))
                    .setTitle(R.string.delete_notifications_title)
                    .setMessage(R.string.delete_outage_notifications_msg)
                    .setPositiveButton(R.string.btn_ok, (dialog, arg1) ->
                            mPresenter.clearOutageNotifications())
                    .setNegativeButton(R.string.btn_cancel, null).show();
        }
    }

    public void btnDeleteAccountNotificationsClick(View view) {
        if (ButtonClicksHelper.canClickButton()) {
            (new AlertDialog.Builder(this))
                    .setTitle(R.string.delete_notifications_title)
                    .setMessage(R.string.delete_account_notifications_msg)
                    .setPositiveButton(R.string.btn_ok, (dialog, arg1) ->
                            mPresenter.clearAccountNotifications())
                    .setNegativeButton(R.string.btn_cancel, null).show();
        }
    }

    /**
     * Crea una animación para el layout del parámetro
     *
     * @param wichLayout
     * @param expandSize
     */
    public void changeLinearLayoutHeight(final LinearLayout wichLayout,
                                         final ExpandStatus expandSize) {
        int selectedSize = -1;
        if (expandSize == ExpandStatus.COLLAPSED)
            selectedSize = 0;
        if (expandSize == ExpandStatus.HALF)
            selectedSize = halfSize;
        if (expandSize == ExpandStatus.FULL)
            selectedSize = fullSize;
        HeightAnimation heightAnim = new HeightAnimation(wichLayout,
                wichLayout.getHeight(), selectedSize);
        heightAnim.setDuration(400);
        enableHWAceleration(wichLayout, heightAnim);

        wichLayout.startAnimation(heightAnim);
    }

    @SuppressLint({"InlinedApi", "NewApi"})
    private void enableHWAceleration(final LinearLayout wichLayout,
                                     HeightAnimation heightAnim) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            wichLayout.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            heightAnim.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    wichLayout.setLayerType(View.LAYER_TYPE_NONE, null);
                }
            });
        }
    }

    /**
     * Asigna los refresh y load more listeners para ambas listas
     */
    private void setOnRefreshAndLoadListeners() {
        outageListView.setXListViewListener(new IXListViewListener() {
            @Override
            public void onRefresh() {
                mPresenter.refreshOutageNotifications();
            }

            @Override
            public void onLoadMore() {
                mPresenter.loadMoreOutageNotifications();
            }
        });
        accountsListView.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
                mPresenter.refreshAccountNotifications();
            }

            @Override
            public void onLoadMore() {
                mPresenter.loadMoreAccountNotifications();
            }
        });
    }

    // #region Interface Methods
    @Override
    public void setOutageList(final List<Notification> outageNotifications) {
        runOnUiThread(() -> {
            mLblNoOutageNotifications.setVisibility(outageNotifications
                    .size() == 0 ? View.VISIBLE : View.GONE);
            mOutagesAdapter = new NotificationAdapter(
                    NotificationsActivity.this,
                    R.layout.notification_list_item, outageNotifications);
            outageListView.setAdapter(mOutagesAdapter);
        });
    }

    @Override
    public void setAccountsList(final List<Notification> accountNotifications) {
        runOnUiThread(() -> {
            mLblNoAccountNotifications.setVisibility(accountNotifications
                    .size() == 0 ? View.VISIBLE : View.GONE);
            mAccountsAdapter = new NotificationAdapter(
                    NotificationsActivity.this,
                    R.layout.notification_list_item, accountNotifications);
            accountsListView.setAdapter(mAccountsAdapter);
        });
    }

    @Override
    public void addOutageNotifications(
            final List<Notification> outageNotifications) {
        runOnUiThread(() -> {
            if (mOutagesAdapter == null)
                setOutageList(outageNotifications);
            else
                mOutagesAdapter.addAll(outageNotifications);
        });
    }

    @Override
    public void addAccountNotifications(
            final List<Notification> accountNotifications) {
        runOnUiThread(() -> {
            if (mAccountsAdapter == null)
                setAccountsList(accountNotifications);
            else
                mAccountsAdapter.addAll(accountNotifications);
        });
    }

    @Override
    public void setMoreOutageNotificationsEnabled(final boolean enabled) {
        runOnUiThread(() -> outageListView.setPullLoadEnable(enabled));
    }

    @Override
    public void setMoreAcccountNotificationsEnabled(final boolean enabled) {
        runOnUiThread(() -> accountsListView.setPullLoadEnable(enabled));
    }

    @Override
    public void loadAndRefreshOutageFinished() {
        runOnUiThread(() -> {
            outageListView.stopRefresh();
            outageListView.stopLoadMore();
        });
        System.gc();
    }

    @Override
    public void loadAndRefreshAccountsFinished() {
        runOnUiThread(() -> {
            accountsListView.stopRefresh();
            accountsListView.stopLoadMore();
        });
        System.gc();
    }

    @Override
    public void hideOutageList() {
        runOnUiThread(() -> mCheckOutageGroup.setChecked(false));
    }

    @Override
    public void hideAccountsList() {
        runOnUiThread(() -> mCheckAccountsGroup.setChecked(false));
    }

    @Override
    public void showNewOutageNotificationUpdate(final Notification notif,
                                                final boolean removeLast) {
        runOnUiThread(() -> {
            mOutagesAdapter.addNewNotificationUpdate(notif, removeLast);
            mLblNoOutageNotifications.setVisibility(View.GONE);
        });
    }

    @Override
    public void showNewAccountNotificationUpdate(final Notification notif,
                                                 final boolean removeLast) {
        runOnUiThread(() -> {
            mAccountsAdapter.addNewNotificationUpdate(notif, removeLast);
            mLblNoAccountNotifications.setVisibility(View.GONE);
        });
    }

    // #endregion
}
