package com.elfec.ssc.business_logic;

import com.elfec.ssc.R;
import com.elfec.ssc.helpers.ui.MessagesUtil;
import com.elfec.ssc.local_storage.AccountStorage;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Device;
import com.elfec.ssc.model.security.SscToken;
import com.elfec.ssc.security.AppPreferences;
import com.elfec.ssc.security.CredentialManager;
import com.elfec.ssc.web_services.AccountService;
import com.elfec.ssc.web_services.SscTokenRequester;

import java.util.List;

import rx.Observable;

/**
 * Se encarga de las distintas operaciones relacionadas con las cuentas de elfec
 *
 * @author Diego
 */
public class AccountManager {

    /**
     * Registers an account within web services and saves it locally, validates
     * if the account already exists
     *
     * @param gmail   gmail of the client which this account is
     * @param account account
     * @return observable of account
     */
    public static Observable<Account> registerAccount(String gmail, Account account) {
        AccountStorage storage = new AccountStorage();
        return storage.getAccount(gmail, account.getNus()).map(acc -> {
            if (acc != null)
                throw new RuntimeException(MessagesUtil.getString(R.string.account_already_reg_msg));
            return null;
        }).flatMap(n -> Observable.zip(new SscTokenRequester().getSscToken(),
                DeviceManager.getCurrentDevice(), Tokens::new))
                .flatMap(t -> new AccountService(t.sscToken)
                        .registerAccount(account.getAccountNumber(),
                                account.getNus(), gmail, t.device)
                        .flatMap(student -> new AccountStorage()
                                .saveAccount(gmail, account)));
    }

    /**
     * Deletes the relation of this account with the client
     * @param account to remove
     * @return observable of the account or null if it wasn't properly removed
     */
    public static Observable<Account> removeAccount(String gmail, Account account) {
        String imei = new CredentialManager(AppPreferences.getApplicationContext())
                .getDeviceIdentifier();
        return Observable.zip(new SscTokenRequester().getSscToken(),
                DeviceManager.getCurrentDevice(), Tokens::new)
                .flatMap(t -> new AccountService(t.sscToken)
                        .removeAccount(gmail, account.getNus(), imei))
                .flatMap(b -> {
                    if (!b) return Observable.just(null);
                    return new AccountStorage().removeAccount(gmail, account);
                });
    }

    /**
     * Retrieves the accounts of a client from web services and saves them for further uses
     *
     * @param gmail client's gmail
     * @return observable of account list
     */
    public static Observable<List<Account>> syncAccounts(String gmail) {
        return getAccountsFromWs(gmail)
                .flatMap(accounts -> new AccountStorage()
                        .saveAccounts(gmail, accounts));
    }

    /**
     * Gets the accounts of an account via web services
     *
     * @param gmail client's gmail
     * @return observable of account list
     */
    public static Observable<List<Account>> getAccountsFromWs(String gmail) {
        return Observable.zip(new SscTokenRequester().getSscToken(),
                DeviceManager.getCurrentDevice(), Tokens::new)
                .flatMap(t -> new AccountService(t.sscToken).getAccounts(gmail, t.device));
    }

    private static class Tokens {
        SscToken sscToken;
        Device device;

        public Tokens(SscToken sscToken, Device device) {
            this.sscToken = sscToken;
            this.device = device;
        }
    }

}
