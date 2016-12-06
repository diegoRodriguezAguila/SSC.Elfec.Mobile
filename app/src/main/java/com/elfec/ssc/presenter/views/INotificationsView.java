package com.elfec.ssc.presenter.views;

import com.elfec.ssc.model.Notification;

import java.util.List;

public interface INotificationsView extends IBaseView {

	/**
	 * Muestra la lista de notificaciones respectivas a cortes
	 * @param outageNotifications
	 */
	void setOutageList(List<Notification> outageNotifications);
	/**
	 * Muestra la lista de notificaciones respectivas a cuentas
	 * @param accountNotifications
	 */
	void setAccountsList(List<Notification> accountNotifications);
	/**
	 * Añade las notificaciones de cortes a la lista actual
	 * @param outageNotifications
	 */
	void addOutageNotifications(List<Notification> outageNotifications);
	/**
	 * Añade las notificaciones de cuentas a la lista actual
	 * @param accountNotifications
	 */
	void addAccountNotifications(List<Notification> accountNotifications);
	/**
	 * Indica si se habilita o no la opción de ver más en la lista de notificaciones de cortes
	 * @param enabled true si es que se lo debe habilitar
	 */
	void setMoreOutageNotificationsEnabled(boolean enabled);
	/**
	 * Indica si se habilita o no la opción de ver más en la lista de notificaciones de cuentas
	 * @param enabled true si es que se lo debe habilitar
	 */
	void setMoreAcccountNotificationsEnabled(boolean enabled);
	/**
	 * Avisa a la interfáz que se terminaron de realizar la carga de notificaciones de cortes
	 * ya sea de load more o de refresh
	 */
	void loadAndRefreshOutageFinished();
	/**
	 * Avisa a la interf�z que se terminaron de realizar la carga de notificaciones de cuentas 
	 * ya sea de load more o de refresh
	 */
	void loadAndRefreshAccountsFinished();
	/**
	 * Indica a la vista que debe dejar de mostrar la lista de
	 * notificaciones de cortes
	 */
	void hideOutageList();
	/**
	 * Indica a la vista que debe dejar de mostrar la lista de
	 * notificaciones de cuentas
	 */
	void hideAccountsList();
	/**
	 * Muestra una notificación de cortes que acaba de llegar
	 * @param notif
	 * @param removeLast
	 */
	void showNewOutageNotificationUpdate(Notification notif, boolean removeLast);
	/**
	 * Muestra una notificación de cuentas que acaba de llegar
	 * @param notif
	 * @param removeLast
	 */
	void showNewAccountNotificationUpdate(Notification notif, boolean removeLast);
}
