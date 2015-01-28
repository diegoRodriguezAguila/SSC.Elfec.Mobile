package com.elfec.ssc.presenter;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import android.os.Build;
import android.os.Looper;

import com.elfec.ssc.businesslogic.ElfecAccountsManager;
import com.elfec.ssc.businesslogic.FieldValidator;
import com.elfec.ssc.businesslogic.webservices.AccountWS;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.events.GCMTokenReceivedCallback;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.gcmservices.GCMTokenRequester;
import com.elfec.ssc.model.validations.ValidationRulesFactory;
import com.elfec.ssc.model.validations.ValidationsAndParams;
import com.elfec.ssc.model.webservices.WSResponse;
import com.elfec.ssc.presenter.views.IRegisterAccount;

/**
 * Presenter para la vista {@link IRegisterAccount}
 * @author Diego
 *
 */
public class RegisterAccountPresenter {

	private IRegisterAccount view;
	public RegisterAccountPresenter(IRegisterAccount view) {
		this.view = view;
	}

	/**
	 * Obtiene la información provista por el usuario y del dispositivo
	 */
	public void processAccountData()
	{		
		Thread thread = new Thread(new Runnable() {		
			@Override
			public void run() {
				Looper.prepare();
				boolean nusIsValid = validateNUS();
				boolean accountNumberIsValid = validateAccountNumber();
				if(nusIsValid && accountNumberIsValid)
				{
					final Client client = Client.getActiveClient();
					if(!client.hasAccount(view.getNUS(), view.getAccountNumber()))
					{
						view.showWSWaiting();						
						GCMTokenRequester gcmTokenRequester = view.getGCMTokenRequester();
						gcmTokenRequester.getTokenAsync(new GCMTokenReceivedCallback() {								
							@Override
							public void onGCMTokenReceived(String deviceToken) {
								if(deviceToken==null)
								{
									view.hideWSWaiting();
									List<Exception> errorsToShow = new ArrayList<Exception>();
									errorsToShow.add(new ConnectException("No fue posible conectarse con el servidor, porfavor revise su conexión a internet"));
									view.showRegistrationErrors(errorsToShow);
								}
								else
								{
									callRegisterWebService(client);
								}
							}
						});
					}
					else
					{
						view.notifyAccountAlreadyRegistered();
					}
				}
				else
				{
					view.notifyErrorsInFields();
				}
				Looper.loop();

			}
		});
		thread.start();
		
	}
	
	/**
	 * Invoca a las clases necesarias para registrar una cuenta via web services
	 * @param client
	 */
	private void callRegisterWebService(final Client client) {
		AccountWS accountWebService = new AccountWS();
		accountWebService.registerAccount(view.getAccountNumber(), view.getNUS(), client.getGmail(), view.getPhoneNumber(), 
				Build.BRAND , Build.MODEL, view.getIMEI(), view.getPreferences().getGCMToken() , new IWSFinishEvent<Boolean>() {		
				@Override
				public void executeOnFinished(WSResponse<Boolean> result) 
				{
					view.hideWSWaiting();
					if(result.getResult())
					{
						ElfecAccountsManager.RegisterAccount(client, view.getAccountNumber(), view.getNUS());
						view.notifyAccountSuccessfulyRegistered();
					}
					else
					{
						view.showRegistrationErrors(result.getErrors());
					}
				}
			});
	}
	
	/**
	 * Valida un campo con los parámetros pasados
	 */
	private List<String> validateField(String fieldName, boolean fieldIsMaleGender, String fieldValue, 
			String validationRules)
	{
		ValidationsAndParams<String> validationRulesAndParams = ValidationRulesFactory.createValidationRulesWithParams(validationRules);
		return FieldValidator.validate(fieldName, fieldIsMaleGender, fieldValue, 
				validationRulesAndParams.getValidationRules(), validationRulesAndParams.getParams());
	}
	
	/**
	 * Valida el campo del NUS
	 * @return true si no tiene errores
	 */
	public boolean validateNUS()
	{
		List<String> validationErrors = validateField("NUS", true, view.getNUS(), view.getNUSValidationRules());
		view.setNUSErrors(validationErrors);
		return validationErrors.size()==0;
	}
	
	/**
	 * Validael campo del número de cuenta
	 * @return true si no tiene errores
	 */
	public boolean validateAccountNumber()
	{
		List<String> validationErrors = validateField("cuenta", false, view.getAccountNumber(), view.getAccountNumberValidationRules());
		view.setAccountNumberErrors(validationErrors);
		return validationErrors.size()==0;
	}
}
