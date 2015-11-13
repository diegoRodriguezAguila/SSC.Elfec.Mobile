package com.elfec.ssc.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.helpers.ViewPresenterManager;
import com.elfec.ssc.helpers.ui.ButtonClicksHelper;
import com.elfec.ssc.model.Notification;
import com.elfec.ssc.presenter.ViewNotificationsPresenter;
import com.elfec.ssc.presenter.views.IViewNotifications;
import com.elfec.ssc.view.adapters.NotificationAdapter;
import com.elfec.ssc.view.animations.HeightAnimation;
import com.elfec.ssc.view.controls.xlistview.XListView;
import com.elfec.ssc.view.controls.xlistview.XListView.IXListViewListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ViewNotifications extends AppCompatActivity implements
        IViewNotifications {

    public enum ExpandStatus {
        COLLAPSED, HALF, FULL
    }

    private ViewNotificationsPresenter presenter;

    private ExpandStatus outageStatus;
    private ExpandStatus accountsStatus;
    private int halfSize = -1;
    private int fullSize = -1;

    protected @Bind(R.id.outage_group) CheckBox mCheckOutageGroup;
    protected @Bind(R.id.outage_listview) XListView outageListView;
    protected @Bind(R.id.accounts_group) CheckBox mCheckAccountsGroup;
    protected @Bind(R.id.accounts_listview) XListView accountsListView;
    protected @Bind(R.id.lbl_no_outage_notifications) TextView mLblNoOutageNotifications;
    protected @Bind(R.id.lbl_no_account_notifications) TextView mLblNoAccountNotifications;
    protected @Bind(R.id.outage_listlayout) LinearLayout outageLayout;
    protected @Bind(R.id.accounts_listlayout) LinearLayout accountsLayout;

    private NotificationAdapter mOutagesAdapter;
    private NotificationAdapter mAccountsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        presenter = new ViewNotificationsPresenter(this);
        ButterKnife.bind(this);
        outageStatus = ExpandStatus.COLLAPSED;
        accountsStatus = ExpandStatus.COLLAPSED;
        outageListView.setPullLoadEnable(false);
        accountsListView.setPullLoadEnable(false);
        initializeListListeners();
    }

    @Override
    public void onBackPressed() {
        finish();// go back to the previous Activity
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewPresenterManager.setPresenter(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadNotifications();
        ViewPresenterManager.setPresenter(presenter);
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
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                setOnCheckListeners();
                setOnRefreshAndLoadListeners();
            }
        });
        thread.start();
    }

    /**
     * Pone los check listeners para los cortes y las cuentas
     */
    private void setOnCheckListeners() {
        OnCheckedChangeListener checkListener = new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                redefineViewSizes();
            }
        };
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
                    .setPositiveButton(R.string.btn_ok, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            presenter.clearOutageNotifications();
                        }
                    }).setNegativeButton(R.string.btn_cancel, null).show();
        }
    }

    public void btnDeleteAccountNotificationsClick(View view) {
        if (ButtonClicksHelper.canClickButton()) {
            (new AlertDialog.Builder(this))
                    .setTitle(R.string.delete_notifications_title)
                    .setMessage(R.string.delete_account_notifications_msg)
                    .setPositiveButton(R.string.btn_ok, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            presenter.clearAccountNotifications();
                        }
                    }).setNegativeButton(R.string.btn_cancel, null).show();
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
                presenter.refreshOutageNotifications();
            }

            @Override
            public void onLoadMore() {
                presenter.loadMoreOutageNotifications();
            }
        });
        accountsListView.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
                presenter.refreshAccountNotifications();
            }

            @Override
            public void onLoadMore() {
                presenter.loadMoreAccountNotifications();
            }
        });
    }

    // #region Interface Methods
    @Override
    public void setOutageList(final List<Notification> outageNotifications) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLblNoOutageNotifications.setVisibility(outageNotifications
                        .size() == 0 ? View.VISIBLE : View.GONE);
                mOutagesAdapter = new NotificationAdapter(
                        ViewNotifications.this,
                        R.layout.notification_list_item, outageNotifications);
                outageListView.setAdapter(mOutagesAdapter);
            }
        });
    }

    @Override
    public void setAccountsList(final List<Notification> accountNotifications) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLblNoAccountNotifications.setVisibility(accountNotifications
                        .size() == 0 ? View.VISIBLE : View.GONE);
                mAccountsAdapter = new NotificationAdapter(
                        ViewNotifications.this,
                        R.layout.notification_list_item, accountNotifications);
                accountsListView.setAdapter(mAccountsAdapter);
            }
        });
    }

    @Override
    public void addOutageNotifications(
            final List<Notification> outageNotifications) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mOutagesAdapter == null)
                    setOutageList(outageNotifications);
                else
                    mOutagesAdapter.addAll(outageNotifications);
            }
        });
    }

    @Override
    public void addAccountNotifications(
            final List<Notification> accountNotifications) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mAccountsAdapter == null)
                    setAccountsList(accountNotifications);
                else
                    mAccountsAdapter.addAll(accountNotifications);
            }
        });
    }

    @Override
    public void setMoreOutageNotificationsEnabled(final boolean enabled) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                outageListView.setPullLoadEnable(enabled);
            }
        });
    }

    @Override
    public void setMoreAcccountNotificationsEnabled(final boolean enabled) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                accountsListView.setPullLoadEnable(enabled);
            }
        });
    }

    @Override
    public void loadAndRefreshOutageFinished() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                outageListView.stopRefresh();
                outageListView.stopLoadMore();
            }
        });
        System.gc();
    }

    @Override
    public void loadAndRefreshAccountsFinished() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                accountsListView.stopRefresh();
                accountsListView.stopLoadMore();
            }
        });
        System.gc();
    }

    @Override
    public void hideOutageList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCheckOutageGroup.setChecked(false);
            }
        });
    }

    @Override
    public void hideAccountsList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCheckAccountsGroup.setChecked(false);
            }
        });
    }

    @Override
    public void showNewOutageNotificationUpdate(final Notification notif,
                                                final boolean removeLast) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mOutagesAdapter.addNewNotificationUpdate(notif, removeLast);
                mLblNoOutageNotifications.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void showNewAccountNotificationUpdate(final Notification notif,
                                                 final boolean removeLast) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAccountsAdapter.addNewNotificationUpdate(notif, removeLast);
                mLblNoAccountNotifications.setVisibility(View.GONE);
            }
        });
    }

    // #endregion
}
