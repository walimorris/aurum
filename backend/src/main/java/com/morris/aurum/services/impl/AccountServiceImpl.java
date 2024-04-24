package com.morris.aurum.services.impl;

import com.morris.aurum.models.accounts.Account;
import com.morris.aurum.models.accounts.CheckingAccount;
import com.morris.aurum.models.clients.Client;
import com.morris.aurum.models.properties.CountryCodeCurrencyProperties;
import com.morris.aurum.models.types.AccountType;
import com.morris.aurum.models.types.ActiveType;
import com.morris.aurum.models.types.CurrencyType;
import com.morris.aurum.repositories.AccountRepository;
import com.morris.aurum.services.AccountService;
import com.morris.aurum.utils.BankingUtil;
import org.bson.BsonDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

@Service
public class AccountServiceImpl implements AccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository accountRepository;
    private final CountryCodeCurrencyProperties countryCodeProperties;
    private final BankingUtil bankingUtil;

    private static final String ROUTING_NUMBER = "223332111";

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, CountryCodeCurrencyProperties countryCodeProperties,
                              BankingUtil bankingUtil) {

        this.accountRepository = accountRepository;
        this.countryCodeProperties = countryCodeProperties;
        this.bankingUtil = bankingUtil;
    }

    @Override
    public Account createCheckingAccount(Client client) {
        CurrencyType currencyType = matchCountryCodeToCurrency(client);
        String accountNumber = bankingUtil.generateHashId(client.getClientId() + client.getAccounts().size());
        BsonDateTime now = new BsonDateTime(new Date().getTime());

        CheckingAccount checkingAccount = CheckingAccount.builder()
                .currencyType(currencyType)
                .balance(BigDecimal.ZERO)
                .accountType(AccountType.CHECKING)
                .accountNumber(accountNumber)
                .routingNumber(ROUTING_NUMBER)
                .activeType(ActiveType.ACTIVE)
                .transactions(new ArrayList<>())
                .creationDate(now)
                .debitCards(new ArrayList<>())
                .build();
        return accountRepository.insert(checkingAccount);
    }

    @Override
    public Account createSavingAccount(Client client) {
        return null;
    }

    @Override
    public Account deleteAccount(Client client) {
        return null;
    }

    @Override
    public Account deleteAccount(String accountNumber) {
        return null;
    }

    private CurrencyType matchCountryCodeToCurrency(Client client) {
        return countryCodeProperties.codes().get(client.getAddress().getCountryCode());
    }
}
