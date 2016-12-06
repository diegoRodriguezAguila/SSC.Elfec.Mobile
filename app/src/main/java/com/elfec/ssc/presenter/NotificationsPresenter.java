package com.elfec.ssc.presenter;

import com.elfec.ssc.local_storage.NotificationStorage;
import com.elfec.ssc.model.Notification;
import com.elfec.ssc.model.enums.NotificationType;
import com.elfec.ssc.presenter.views.INotificationsView;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NotificationsPresenter extends BasePresenter<INotificationsView> {

    private static final int LOAD_LIMIT = 10;
    private int currentOutageLimit = LOAD_LIMIT;
    private int currentAccountsLimit = LOAD_LIMIT;
    private NotificationStorage mStorage;
    private List<Notification> outageNotifications;
    private List<Notification> accountNotifications;

    public NotificationsPresenter(INotificationsView view) {
        super(view);
        mStorage = new NotificationStorage();
    }

    /**
     * Crea un hilo para cargar la información de ambas listas, de cuentas y de
     * cortes
     */
    public void loadNotifications() {
        loadOutageNotifications();
        loadAccountNotifications();
    }

    /**
     * Carga las notificaciones de cortes
     */
    public void loadOutageNotifications() {
        mStorage.getNotifications(NotificationType.OUTAGE_OR_INFO, currentOutageLimit + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(notifications -> {
                    outageNotifications = notifications;
                    int outageSize = outageNotifications.size();
                    if (outageSize > currentOutageLimit)
                        outageNotifications.remove(outageSize - 1);
                    return outageSize;
                })
                .subscribe(size -> {
                    mView.loadAndRefreshOutageFinished();
                    mView.setOutageList(outageNotifications);
                    mView.setMoreOutageNotificationsEnabled(size > currentOutageLimit);
                }, e -> {
                });
    }

    /**
     * Carga las notificaciones de cuentas
     */
    public void loadAccountNotifications() {
        mStorage.getNotifications(NotificationType.ACCOUNT, currentAccountsLimit + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(notifications -> {
                    accountNotifications = notifications;
                    int accountsSize = accountNotifications.size();
                    if (accountsSize > currentAccountsLimit)
                        accountNotifications.remove(accountsSize - 1);
                    return accountsSize;
                })
                .subscribe(size -> {
                    mView.loadAndRefreshAccountsFinished();
                    mView.setAccountsList(accountNotifications);
                    mView.setMoreAcccountNotificationsEnabled(size > currentAccountsLimit);
                }, e -> {
                });
    }

    /**
     * Vuelve a cargar la lista de notificaciones de cortes, tomando en cuenta
     * el límite actual
     */
    public void refreshOutageNotifications() {
        new Thread(this::loadOutageNotifications).start();
    }

    /**
     * Vuelve a cargar la lista de notificaciones de cuentas, tomando en cuenta
     * el límite actual
     */
    public void refreshAccountNotifications() {
        new Thread(this::loadAccountNotifications).start();
    }

    /**
     * Carga mas items a la lista de notificaciones de cortes, auentando el
     * límite actual, en un hilo
     */
    public void loadMoreOutageNotifications() {
        currentOutageLimit += LOAD_LIMIT;
        loadOutageNotifications();
    }

    /**
     * Carga mas items a la lista de notificaciones de cuentas, auentando el
     * límite actual, en un hilo
     */
    public void loadMoreAccountNotifications() {
        currentAccountsLimit += LOAD_LIMIT;
        loadAccountNotifications();
    }

    /**
     * Elimina las notificaciones de cortes de la base de datos y le dice a la
     * vista que limpie su lista
     */
    public void clearOutageNotifications() {
        mStorage.removeNotifications(NotificationType.OUTAGE_OR_INFO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(notifications -> {
                    outageNotifications.clear();
                    currentOutageLimit = LOAD_LIMIT;
                    mView.hideOutageList();
                    mView.setOutageList(outageNotifications);
                    mView.setMoreOutageNotificationsEnabled(false);
                }, e -> {
                });
    }

    /**
     * Elimina las notificaciones de cuentas de la base de datos y le dice a la
     * vista que limpie su lista
     */
    public void clearAccountNotifications() {
        mStorage.removeNotifications(NotificationType.ACCOUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(notifications -> {
                    accountNotifications.clear();
                    currentAccountsLimit = LOAD_LIMIT;
                    mView.hideAccountsList();
                    mView.setAccountsList(outageNotifications);
                    mView.setMoreAcccountNotificationsEnabled(false);
                }, e -> {
                });
    }

    /**
     * Cuando llega una notificación de cortes y se quiere aumentar al principio
     * de la lista se debe utilizar este metodo
     *
     * @param notif
     */
    public void addNewOutageNotificationUpdate(Notification notif) {
        int outSize = outageNotifications.size() + 1;
        boolean hasToRemove = outSize > currentOutageLimit;
        if (hasToRemove) {
            mView.setMoreOutageNotificationsEnabled(true);
        }
        mView.showNewOutageNotificationUpdate(notif, hasToRemove);
    }

    /**
     * Cuando llega una notificación de cuentas y se quiere aumentar al
     * principio de la lista se debe utilizar este metodo
     *
     * @param notif
     */
    public void addNewAccountNotificationUpdate(Notification notif) {
        int accSize = accountNotifications.size() + 1;
        boolean hasToRemove = accSize > currentAccountsLimit;
        if (hasToRemove) {
            mView.setMoreAcccountNotificationsEnabled(true);
        }
        mView.showNewAccountNotificationUpdate(notif, hasToRemove);
    }

}
