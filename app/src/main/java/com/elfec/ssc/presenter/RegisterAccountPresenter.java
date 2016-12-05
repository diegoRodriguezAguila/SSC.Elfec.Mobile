package com.elfec.ssc.presenter;

import com.elfec.ssc.R;
import com.elfec.ssc.business_logic.AccountManager;
import com.elfec.ssc.business_logic.ClientManager;
import com.elfec.ssc.business_logic.FieldValidator;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.validations.ValidationRulesFactory;
import com.elfec.ssc.model.validations.ValidationsAndParams;
import com.elfec.ssc.presenter.views.IRegisterAccount;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Presenter para la vista {@link IRegisterAccount}
 *
 * @author Diego
 */
public class RegisterAccountPresenter extends BasePresenter<IRegisterAccount> {

    public RegisterAccountPresenter(IRegisterAccount view) {
        super(view);
    }

    /**
     * Registers the account
     */
    public void registerAccount() {
        boolean nusIsValid = validateNus();
        boolean accountNumberIsValid = validateAccountNumber();
        if (!nusIsValid | !accountNumberIsValid) {
            mView.notifyFieldErrors();
            return;
        }
        mView.onProcessing(R.string.msg_registering_account);
        ClientManager.activeClient()
                .flatMap(client -> AccountManager.registerAccount(client.getGmail(),
                        new Account(client, mView.getAccountNumber(), mView.getNus())))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mView::onSuccess, mView::onError);
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
    public boolean validateNus() {
        List<String> validationErrors = validateField("NUS", true,
                mView.getNus(), mView.getNusValidationRules());
        mView.setNusErrors(validationErrors);
        return validationErrors.size() == 0;
    }

    /**
     * Validael campo del número de cuenta
     *
     * @return true si no tiene errores
     */
    public boolean validateAccountNumber() {
        List<String> validationErrors = validateField("cuenta", false,
                mView.getAccountNumber(), mView.getAccountNumberValidationRules());
        mView.setAccountNumberErrors(validationErrors);
        return validationErrors.size() == 0;
    }
}
