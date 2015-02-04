package com.elfec.ssc.presenter;

import java.util.List;

import com.elfec.ssc.businesslogic.ElfecNotificationManager;
import com.elfec.ssc.model.Notification;
import com.elfec.ssc.model.enums.NotificationType;
import com.elfec.ssc.presenter.views.IViewNotifications;

public class ViewNotificationsPresenter {
	
	private IViewNotifications view;
	private final int LOAD_LIMIT = 10;
	private int currentOutageLimit = LOAD_LIMIT;
	private int currentAccountsLimit = LOAD_LIMIT;
	private List<Notification> outageNotifications;
	private List<Notification> accountNotifications;
	
	public ViewNotificationsPresenter(IViewNotifications view) {
		this.view = view;
	}
	
	/**
	 * Crea un hilo para cargar la información de ambas listas, de cuentas y de cortes
	 */
	public void loadNotifications()
	{
		Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {				
				loadOutageNotifications();
				loadAccountNotifications();
			}
		});
		thread.start();
	}
	
	/**
	 * Carga las notificaciones de cortes
	 */
	public void loadOutageNotifications()
	{
		outageNotifications = Notification.getNotificationsByType(NotificationType.OUTAGE, currentOutageLimit+1);
		int outageSize = outageNotifications.size();
		if(outageSize>currentOutageLimit)
			outageNotifications.remove(outageSize-1);	
		view.loadAndRefreshOutageFinished();
		view.setOutageList(outageNotifications);
		view.setMoreOutageNotificationsEnabled(outageSize>currentOutageLimit);
	}
	/**
	 * Carga las notificaciones de cuentas
	 */
	public void loadAccountNotifications()
	{
		accountNotifications = Notification.getNotificationsByType(NotificationType.ACCOUNT, currentAccountsLimit+1);			
		int accountsSize = accountNotifications.size();		
		if(accountsSize>currentAccountsLimit)
			accountNotifications.remove(accountsSize-1);
		view.loadAndRefreshAccountsFinished();
		view.setAccountsList(accountNotifications);		
		view.setMoreAcccountNotificationsEnabled(accountsSize>currentAccountsLimit);
	}
	
	/**
	 * Vuelve a cargar la lista de notificaciones de cortes, tomando en cuenta el límite actual
	 */
	public void refreshOutageNotifications()
	{
		Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {
				loadOutageNotifications();
			}
		});
		thread.start();
	}
	
	/**
	 * Vuelve a cargar la lista de notificaciones de cuentas, tomando en cuenta el límite actual
	 */
	public void refreshAccountNotifications()
	{
		Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {
				loadAccountNotifications();
			}
		});
		thread.start();
	}
	
	/**
	 * Carga mas items a la lista de notificaciones de cortes, auentando el límite actual, en un hilo
	 */
	public void loadMoreOutageNotifications()
	{
		Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {
				currentOutageLimit+=LOAD_LIMIT;
				List<Notification> newOutageNotifications = Notification.getNotificationsByType(NotificationType.OUTAGE, currentOutageLimit+1);			
				int outageSize = newOutageNotifications.size();		
				if(outageSize>currentOutageLimit)
					newOutageNotifications.remove(outageSize-1);	
				newOutageNotifications.removeAll(outageNotifications);
				view.addOutageNotifications(newOutageNotifications);
				view.loadAndRefreshOutageFinished();
				outageNotifications.addAll(newOutageNotifications);	
				view.setMoreOutageNotificationsEnabled(outageSize>currentOutageLimit);
			}
		});
		thread.start();
	}
	
	/**
	 * Carga mas items a la lista de notificaciones de cuentas, auentando el límite actual, en un hilo
	 */
	public void loadMoreAccountNotifications()
	{
		Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {
				currentAccountsLimit+=LOAD_LIMIT;
				List<Notification> newAccountNotifications = Notification.getNotificationsByType(NotificationType.ACCOUNT, currentAccountsLimit+1);			
				int accountsSize = newAccountNotifications.size();		
				if(accountsSize>currentAccountsLimit)
					newAccountNotifications.remove(accountsSize-1);	
				newAccountNotifications.removeAll(accountNotifications);
				view.addAccountNotifications(newAccountNotifications);
				view.loadAndRefreshAccountsFinished();
				accountNotifications.addAll(newAccountNotifications);	
				view.setMoreAcccountNotificationsEnabled(accountsSize>currentAccountsLimit);
			}
		});
		thread.start();
	}
	
	/**
	 * Elimina las notificaciones de cortes de la base de datos y le dice a la vista que limpie su lista
	 */
	public void clearOutageNotifications()
	{
		Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {
				ElfecNotificationManager.removeAllNotifications(NotificationType.OUTAGE);
				loadOutageNotifications();
				view.hideOutageList();
			}
		});
		thread.start();
	}
	
	/**
	 * Elimina las notificaciones de cuentas de la base de datos y le dice a la vista que limpie su lista
	 */
	public void clearAccountNotifications()
	{
		Thread thread = new Thread(new Runnable() {			
			@Override
			public void run() {
				ElfecNotificationManager.removeAllNotifications(NotificationType.ACCOUNT);
				loadAccountNotifications();
				view.hideAccountsList();
			}
		});
		thread.start();
	}

}
