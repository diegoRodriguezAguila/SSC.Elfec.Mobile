package com.elfec.ssc.local_storage;

import com.cesarferreira.rxpaper.RxPaper;
import com.elfec.ssc.helpers.utils.CollectionUtils;
import com.elfec.ssc.helpers.utils.ObjectsCompat;
import com.elfec.ssc.model.Account;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by drodriguez on 01/12/2016.
 * AccountStorage
 */

public class AccountStorage {

    private static final String ACCOUNTS_BOOK = "accounts.book";

    /**
     * Saves the accounts to the database with all its subclasses, it doesn't execute
     * inmediately,
     * it creates an observable to be execute in the future
     *
     * @param gmail    Client's gmail
     * @param accounts to save
     * @return Observable of accounts
     */
    public Observable<List<Account>> saveAccounts(String gmail, List<Account> accounts) {
        return RxPaper.book(ACCOUNTS_BOOK)
                .write(gmail, accounts);
    }

    /**
     * Gets an account from the database
     *
     * @param gmail Account's NUS
     * @return Observable of account
     */
    public Observable<Account> getAccount(String gmail, String nus) {
        return getAccounts(gmail)
                .map(accounts -> {
                    if (accounts == null)
                        return null;
                    return CollectionUtils.find(accounts, item ->
                            ObjectsCompat.equals(nus, item.getNus()));
                });
    }

    /**
     * Saves an account to the database
     *
     * @param gmail   Account's NUS
     * @param account to save
     * @return Observable of account
     */
    public Observable<Account> saveAccount(String gmail, Account account) {
        return getAccounts(gmail)
                .flatMap(accounts -> {
                    if (accounts == null)
                        accounts = new ArrayList<>();
                    if (!accounts.contains(account))
                        accounts.add(account);
                    return saveAccounts(gmail, accounts);
                }).map(m -> account);
    }

    /**
     * Removes an account from the database
     *
     * @param gmail   Account's NUS
     * @param account to remove
     * @return Observable of account
     */
    public Observable<Account> removeAccount(String gmail, Account account) {
        return getAccounts(gmail)
                .flatMap(accounts -> {
                    if (accounts == null)
                        accounts = new ArrayList<>();
                    if (!accounts.contains(account))
                        accounts.remove(account);
                    return saveAccounts(gmail, accounts);
                }).map(m -> account);
    }

    /**
     * Retrieves accounts from database
     *
     * @param gmail Client's gmail
     * @return Observable of a Accounts
     */
    public Observable<List<Account>> getAccounts(String gmail) {
        return RxPaper.book(ACCOUNTS_BOOK).read(gmail);
    }
}
