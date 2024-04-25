package com.morris.aurum.services;

import com.morris.aurum.models.accounts.Account;
import com.morris.aurum.models.clients.Client;

import java.util.Map;

public interface AccountService {

    /**
     * Create SavingAccount {@link com.morris.aurum.models.accounts.SavingAccount}
     *
     * @param client {@link Client}
     * @return {@link Account}
     */
    Account createSavingAccount(Client client);

    /**
     * Create new CheckingAccount {@link com.morris.aurum.models.accounts.CheckingAccount}
     *
     * @param client {@link Client}
     * @return {@link Account}
     */
    Account createCheckingAccount(Client client);

    /**
     * Delete Account by client and account number.
     *
     * @param client {@link Client}
     * @param accountNumber {@link String}
     *
     * @return {@link Account}
     */
    Map<String, Object> deleteAccount(Client client, String accountNumber);
}
