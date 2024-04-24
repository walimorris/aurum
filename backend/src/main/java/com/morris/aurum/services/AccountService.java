package com.morris.aurum.services;

import com.morris.aurum.models.accounts.Account;
import com.morris.aurum.models.clients.Client;

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
     * Delete Account from {@link Client}.
     *
     * @param client {@link Client}
     * @return {@link Account}
     */
    Account deleteAccount(Client client);

    /**
     * Delete Account from account number.
     *
     * @param accountNumber {@link String}
     * @return {@link Account}
     */
    Account deleteAccount(String accountNumber);
}
