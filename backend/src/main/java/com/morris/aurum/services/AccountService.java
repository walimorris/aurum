package com.morris.aurum.services;

import com.morris.aurum.models.accounts.Account;
import com.morris.aurum.models.clients.Client;

public interface AccountService {
    Account createSavingAccount(Client client);
    Account createCheckingAccount(Client client);
    Account deleteAccount(Client client);
    Account deleteAccount(String accountNumber);
}
