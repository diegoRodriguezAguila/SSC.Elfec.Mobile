package com.elfec.ssc.presenter;

import java.util.List;

import android.os.Build;
import android.os.Looper;

import com.elfec.ssc.businesslogic.ElfecAccountsManager;
import com.elfec.ssc.businesslogic.FieldValidator;
import com.elfec.ssc.businesslogic.webservices.AccountWS;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.events.IWSFinishEvent;
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
						AccountWS accountWebService = new AccountWS();
						accountWebService.registerAccount(view.getAccountNumber(), view.getNUS(), client.getGmail(), view.getPhoneNumber(), 
								Build.BRAND , Build.MODEL, view.getIMEI(), "2131f1dsa13ffsddgh31vvasd", new IWSFinishEvent<Boolean>() {		
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
