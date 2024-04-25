package com.morris.aurum.services.impl;

import com.mongodb.ClientSessionOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.TransactionBody;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.lang.NonNull;
import com.morris.aurum.models.accounts.Account;
import com.morris.aurum.models.accounts.CheckingAccount;
import com.morris.aurum.models.accounts.SavingAccount;
import com.morris.aurum.models.clients.Client;
import com.morris.aurum.models.properties.CountryCodeCurrencyProperties;
import com.morris.aurum.models.types.AccountType;
import com.morris.aurum.models.types.ActiveType;
import com.morris.aurum.models.types.CurrencyType;
import com.morris.aurum.repositories.AccountRepository;
import com.morris.aurum.services.AccountService;
import com.morris.aurum.utils.BankingUtil;
import org.bson.BsonDateTime;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

@Service
public class AccountServiceImpl implements AccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final MongoTemplate mongoTemplate;
    private final AccountRepository accountRepository;
    private final CountryCodeCurrencyProperties countryCodeProperties;
    private final BankingUtil bankingUtil;
    private final CodecRegistry codecRegistry;

    private static final String DATABASE = "aurum_bank";
    private static final String CLIENT_COLLECTION = "clients";
    private static final String ACCOUNT_COLLECTION = "accounts";
    private static final String ROUTING_NUMBER = "223332111";
    private static final BigDecimal SAVINGS_INTEREST_RATE = BigDecimal.valueOf(0.01);

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, CountryCodeCurrencyProperties countryCodeProperties,
                              BankingUtil bankingUtil, MongoTemplate mongoTemplate, CodecRegistry codecRegistry) {

        this.accountRepository = accountRepository;
        this.countryCodeProperties = countryCodeProperties;
        this.bankingUtil = bankingUtil;
        this.mongoTemplate = mongoTemplate;
        this.codecRegistry = codecRegistry;
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
        CurrencyType currencyType = matchCountryCodeToCurrency(client);
        String accountNumber = bankingUtil.generateHashId(client.getClientId() + client.getAccounts().size());
        BsonDateTime now = new BsonDateTime(new Date().getTime());

        SavingAccount savingAccount = SavingAccount.builder()
                .currencyType(currencyType)
                .balance(BigDecimal.ZERO)
                .accountType(AccountType.SAVING)
                .accountNumber(accountNumber)
                .routingNumber(ROUTING_NUMBER)
                .activeType(ActiveType.ACTIVE)
                .transactions(new ArrayList<>())
                .creationDate(now)
                .interestRate(SAVINGS_INTEREST_RATE)
                .build();
        return accountRepository.insert(savingAccount);
    }

    @Override
    public Map<String, Object> deleteAccount(Client client, String accountNumber) {
        Map<String, Object> results = new HashMap<>();
        TransactionBody<Map<String, Object>> transactionBody = new TransactionBody<>() {
            @NonNull
            @Override
            public Map<String, Object> execute() {
                MongoDatabase db = mongoTemplate.getDb().withCodecRegistry(codecRegistry);
                MongoCollection<Client> clientCollection = db.getCollection(CLIENT_COLLECTION, Client.class);
                MongoCollection<Account> accountCollection = db.getCollection(ACCOUNT_COLLECTION, Account.class);

                // remove account from client entity in ClientCollection
                Bson fromClient = eq("clientId", client.getClientId());
                Bson removeAccount = Updates.pull("accounts", accountNumber);

                // delete account from accounts collection
                Bson deleteAccount = eq("accountNumber", accountNumber);

                UpdateResult clientUpdateResult = clientCollection.updateOne(fromClient, removeAccount);
                DeleteResult accountDeleteResult = accountCollection.deleteOne(deleteAccount);
                results.put("update-result", clientUpdateResult);
                results.put("delete-result", accountDeleteResult);

                return results;
            }
        };

        try {
            ClientSessionOptions options = ClientSessionOptions.builder()
                    .causallyConsistent(true)
                    .build();

            mongoTemplate.getMongoDatabaseFactory().getSession(options)
                    .withTransaction(transactionBody);

        } catch (RuntimeException e) {
            LOGGER.error("Transaction Error deleting account '{}': {}", accountNumber, e.getMessage());
        }
        return results;
    }

    private CurrencyType matchCountryCodeToCurrency(Client client) {
        return countryCodeProperties.codes().get(client.getAddress().getCountryCode());
    }
}
