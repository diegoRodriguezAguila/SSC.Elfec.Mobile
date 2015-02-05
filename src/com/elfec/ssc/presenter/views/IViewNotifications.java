package com.elfec.ssc.presenter.views;

import java.util.List;

import com.elfec.ssc.model.Notification;

public interface IViewNotifications {

	/**
	 * Muestra la lista de notificaciones respectivas a cortes
	 * @param notifications
	 */
	public void setOutageList(List<Notification> outageNotifications);
	/**
	 * Muestra la lista de notificaciones respectivas a cuentas
	 * @param notifications
	 */
	public void setAccountsList(List<Notification> accountNotifications);
	/**
	 * Añade las notificaciones de cortes a la lista actual
	 * @param notifications
	 */
	public void addOutageNotifications(List<Notification> outageNotifications);
	/**
	 * Añade las notificaciones de cuentas a la lista actual
	 * @param notifications
	 */
	public void addAccountNotifications(List<Notification> accountNotifications);
	/**
	 * Indica si se habilita o no la opción de ver más en la lista de notificaciones de cortes
	 * @param enabled true si es que se lo debe habilitar
	 */
	public void setMoreOutageNotificationsEnabled(boolean enabled);
	/**
	 * Indica si se habilita o no la opción de ver más en la lista de notificaciones de cuentas
	 * @param enabled true si es que se lo debe habilitar
	 */
	public void setMoreAcccountNotificationsEnabled(boolean enabled);
	/**
	 * Avisa a la interfáz que se terminaron de realizar la carga de notificaciones de cortes 
	 * ya sea de load more o de refresh
	 */
	public void loadAndRefreshOutageFinished();
	/**
	 * Avisa a la interfáz que se terminaron de realizar la carga de notificaciones de cuentas 
	 * ya sea de load more o de refresh
	 */
	public void loadAndRefreshAccountsFinished();
	/**
	 * Indica a la vista que debe dejar de mostrar la lista de
	 * notificaciones de cortes
	 */
	public void hideOutageList();
	/**
	 * Indica a la vista que debe dejar de mostrar la lista de
	 * notificaciones de cuentas
	 */
	public void hideAccountsList();
	/**
	 * Muestra una notificación de cortes que acaba de llegar
	 * @param notif
	 */
	public void showNewOutageNotificationUpdate(Notification notif, boolean removeLast);
	/**
	 * Muestra una notificación de cuentas que acaba de llegar
	 * @param notif
	 */
	public void showNewAccountNotificationUpdate(Notification notif, boolean removeLast);
}
