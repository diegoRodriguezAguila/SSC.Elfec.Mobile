package com.elfec.ssc.business_logic;

import com.elfec.ssc.R;
import com.elfec.ssc.local_storage.AccountStorage;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Debt;
import com.elfec.ssc.model.Device;
import com.elfec.ssc.helpers.ui.MessagesUtil;
import com.elfec.ssc.model.security.SscToken;
import com.elfec.ssc.web_services.AccountService;
import com.elfec.ssc.web_services.SscTokenRequester;

import org.joda.time.DateTime;

import java.util.List;

import rx.Observable;

/**
 * Se encarga de las distintas operaciones relacionadas con las cuentas de elfec
 *
 * @author Diego
 */
public class AccountManager {

    /**
     * Registra una cuenta de elfec en la base de datos asignando al ownerClient
     * como dueño de la cuenta y con el nus y numero de cuentas proporcionados y
     * con estado 1 por defecto
     *
     * @param account cuenta
     * @return retorna la cuenta retistrada
     */
    public static Account registerAccount(Account account) {
        Account newAccount = Account.findAccount(
                account.getClient().getGmail(), account.getNus());
        if (newAccount == null) {
            newAccount = account;
            newAccount.setInsertDate(DateTime.now());
        } else {
            //newAccount.removeAllDebts();
            newAccount.setStatus((short) 1);
            newAccount.copyAttributes(account);
        }
        //newAccount.save();
        List<Debt> accountDebts = newAccount.getDebts();
        for (Debt debt : accountDebts) {
            if (debt.getInsertDate() == null) {
                debt.setAccount(newAccount);
                debt.setInsertDate(DateTime.now());
            }
            debt.save();
        }
        return newAccount;
    }

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
     * Marca a la cuenta con el nus y cliente especificados como eliminada
     *
     * @return boolean, true si se logro eliminar
     */
    public static boolean deleteAccount(String gmail, String nus) {
        Account account = Account.findAccount(gmail, nus);
        if (account != null) {
            account.setStatus((short) 0);
            //return account.save() > 0;
        }
        return false;
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
