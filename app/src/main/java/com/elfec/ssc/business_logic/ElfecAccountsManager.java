package com.elfec.ssc.business_logic;

import android.os.Build;

import com.elfec.ssc.local_storage.AccountStorage;
import com.elfec.ssc.model.Account;
import com.elfec.ssc.model.Debt;
import com.elfec.ssc.model.security.SscToken;
import com.elfec.ssc.security.AppPreferences;
import com.elfec.ssc.security.CredentialManager;
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
public class ElfecAccountsManager {

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
            newAccount.removeAllDebts();
            newAccount.setStatus((short) 1);
            newAccount.copyAttributes(account);
        }
        newAccount.save();
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
     * Marca a la cuenta con el nus y cliente especificados como eliminada
     *
     * @return boolean, true si se logro eliminar
     */
    public static boolean deleteAccount(String gmail, String nus) {
        Account account = Account.findAccount(gmail, nus);
        if (account != null) {
            account.setStatus((short) 0);
            return account.save() > 0;
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
        String imei = new CredentialManager(AppPreferences.getApplicationContext())
                .getDeviceIdentifier();
        return Observable.zip(new SscTokenRequester().getSscToken(),
                DeviceManager.getGcmToken(), Tokens::new).flatMap(t ->
                new AccountService(t.sscToken).getAccounts(gmail,
                        Build.BRAND, Build.MODEL, imei, t.gcmToken));
    }

    private static class Tokens {
        SscToken sscToken;
        String gcmToken;

        public Tokens(SscToken sscToken, String gcmToken) {
            this.sscToken = sscToken;
            this.gcmToken = gcmToken;
        }
    }

}
