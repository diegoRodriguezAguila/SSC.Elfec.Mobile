package com.elfec.ssc.presenter;

import android.os.Build;
import android.os.Looper;

import com.elfec.ssc.business_logic.ElfecAccountsManager;
import com.elfec.ssc.business_logic.FieldValidator;
import com.elfec.ssc.web_services.AccountWS;
import com.elfec.ssc.web_services.SscTokenRequester;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Client;
import com.elfec.ssc.model.events.GcmTokenCallback;
import com.elfec.ssc.model.events.IWSFinishEvent;
import com.elfec.ssc.model.events.SscTokenReceivedCallback;
import com.elfec.ssc.model.exceptions.MobileSideException;
import com.elfec.ssc.model.gcmservices.GcmTokenRequester;
import com.elfec.ssc.model.security.SscToken;
import com.elfec.ssc.model.validations.ValidationRulesFactory;
import com.elfec.ssc.model.validations.ValidationsAndParams;
import com.elfec.ssc.model.webservices.WSResponse;
import com.elfec.ssc.presenter.views.IRegisterAccount;
import com.elfec.ssc.security.AppPreferences;
import com.elfec.ssc.security.CredentialManager;

import java.util.List;

/**
 * Presenter para la vista {@link IRegisterAccount}
 * 
 * @author Diego
 *
 */
public class RegisterAccountPresenter {

	private IRegisterAccount view;
	private GcmTokenRequester mGcmTokenRequester;
    private SscTokenRequester mSscTokenRequester;
	public RegisterAccountPresenter(IRegisterAccount view) {
		this.view = view;
		mGcmTokenRequester = new GcmTokenRequester(AppPreferences.getApplicationContext());
        mSscTokenRequester = new SscTokenRequester();
	}

	/**
	 * Obtiene la información provista por el usuario y del dispositivo
	 */
	public void processAccountData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				boolean nusIsValid = validateNUS();
				boolean accountNumberIsValid = validateAccountNumber();
				if (nusIsValid && accountNumberIsValid) {
					final Client client = Client.getActiveClient();
					if (!client.hasAccount(view.getNus(),
							view.getAccountNumber())) {
						view.showWSWaiting();
                        mGcmTokenRequester
								.getTokenAsync(new GcmTokenCallback() {
									@Override
									public void onGcmTokenReceived(
											String gcmToken) {
                                        callRegisterWebService(client, gcmToken);
									}

                                    @Override
                                    public void onGcmErrors(List<Exception> errors) {
                                        view.hideWSWaiting();
                                        view.showRegistrationErrors(errors);
                                    }
                                });
					} else {
						view.notifyAccountAlreadyRegistered();
					}
				} else {
					view.notifyErrorsInFields();
				}
				Looper.loop();

			}
		}).start();

	}

	/**
	 * Invoca a las clases necesarias para registrar una cuenta via web services
	 * 
	 * @param client cliente
     * @param gcmToken token gcm
	 */
	private void callRegisterWebService(final Client client, final String gcmToken) {
		final String imei = new CredentialManager(AppPreferences.getApplicationContext())
                .getDeviceIdentifier();
        mSscTokenRequester.getTokenAsync(new SscTokenReceivedCallback() {
            @Override
            public void onSscTokenReceived(WSResponse<SscToken> wsTokenResult) {
                view.showRegistrationErrors(wsTokenResult.getErrors());
                if (wsTokenResult.getResult() != null) {
                    AccountWS accountWebService = new AccountWS(wsTokenResult
                            .getResult());
                    accountWebService.registerAccount(view.getAccountNumber(),
                            view.getNus(), client.getGmail(),
                            view.getPhoneNumber(), Build.BRAND, Build.MODEL,
                            imei, gcmToken,
                            new IWSFinishEvent<Account>() {
                                @Override
                                public void executeOnFinished(
                                        WSResponse<Account> result) {
                                    view.hideWSWaiting();
                                    if (result.getResult() != null) {
                                        ElfecAccountsManager
                                                .registerAccount(result
                                                        .getResult());
                                        view.notifyAccountSuccessfullyRegistered();
                                    } else if (!result.hasErrors())
                                        result.addError(new MobileSideException());
                                    view.showRegistrationErrors(result
                                            .getErrors());
                                }
                            });
                } else
                    view.hideWSWaiting();
            }
		});
	}

	/**
	 * Valida un campo con los parámetros pasados
	 */
	private List<String> validateField(String fieldName,
			boolean fieldIsMaleGender, String fieldValue, String validationRules) {
		ValidationsAndParams<String> validationRulesAndParams = ValidationRulesFactory
				.createValidationRulesWithParams(validationRules);
		return FieldValidator.validate(fieldName, fieldIsMaleGender,
				fieldValue, validationRulesAndParams.getValidationRules(),
				validationRulesAndParams.getParams());
	}

	/**
	 * Valida el campo del NUS
	 * 
	 * @return true si no tiene errores
	 */
	public boolean validateNUS() {
		List<String> validationErrors = validateField("NUS", true,
				view.getNus(), view.getNusValidationRules());
		view.setNusErrors(validationErrors);
		return validationErrors.size() == 0;
	}

	/**
	 * Validael campo del número de cuenta
	 * 
	 * @return true si no tiene errores
	 */
	public boolean validateAccountNumber() {
		List<String> validationErrors = validateField("cuenta", false,
				view.getAccountNumber(), view.getAccountNumberValidationRules());
		view.setAccountNumberErrors(validationErrors);
		return validationErrors.size() == 0;
	}
}
