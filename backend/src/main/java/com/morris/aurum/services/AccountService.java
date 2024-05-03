package com.morris.aurum.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.morris.aurum.models.accounts.Account;
import com.morris.aurum.models.accounts.CheckingAccount;
import com.morris.aurum.models.accounts.SavingAccount;
import com.morris.aurum.models.clients.Client;

import java.util.List;
import java.util.Map;

public interface AccountService {

    /**
     * Create SavingAccount {@link com.morris.aurum.models.accounts.SavingAccount}
     *
     * @param client {@link Client}
     * @return {@link Account}
     */
    SavingAccount createSavingAccount(Client client);

    /**
     * Create new CheckingAccount {@link com.morris.aurum.models.accounts.CheckingAccount}
     *
     * @param client {@link Client}
     * @return {@link Account}
     */
    CheckingAccount createCheckingAccount(Client client);

    /**
     * Delete Account by client and account number.
     *
     * @param client {@link Client}
     * @param accountNumber {@link String}
     *
     * @return {@link Account}
     */
    boolean deleteAccount(Client client, String accountNumber);

    /**
     * Get Account.
     *
     * @param accountNumber accountNumber
     * @return {@link Account}
     */
    Account getAccount(String accountNumber);

    /**
     * Get all accounts for client.
     *
     * @param clientId clientId
     * @return {@link List<Account>}
     */
    List<Account> getAllAccountsForClient(String clientId) throws JsonProcessingException;
}
