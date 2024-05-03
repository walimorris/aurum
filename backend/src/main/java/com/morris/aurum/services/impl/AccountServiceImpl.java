package com.morris.aurum.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UnwindOptions;
import com.morris.aurum.models.accounts.Account;
import com.morris.aurum.models.accounts.CheckingAccount;
import com.morris.aurum.models.accounts.SavingAccount;
import com.morris.aurum.models.clients.Client;
import com.morris.aurum.models.types.AccountType;
import com.morris.aurum.models.types.ActiveType;
import com.morris.aurum.models.types.CurrencyType;
import com.morris.aurum.repositories.AccountRepository;
import com.morris.aurum.repositories.ClientRepository;
import com.morris.aurum.services.AccountService;
import com.morris.aurum.utils.BankingUtil;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.SessionSynchronization;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;
import static java.util.Arrays.asList;

@Service
public class AccountServiceImpl implements AccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final MongoTemplate mongoTemplate;
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final CodecRegistry codecRegistry;
    private static final String CLIENT_COLLECTION = "clients";
    private static final String ACCOUNT_COLLECTION = "accounts";
    private static final String ROUTING_NUMBER = "223332111";
    private static final BigDecimal SAVINGS_INTEREST_RATE = BigDecimal.valueOf(0.01);

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, ClientRepository clientRepository,
                              MongoTemplate mongoTemplate, CodecRegistry codecRegistry) {

        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.mongoTemplate = mongoTemplate;
        this.codecRegistry = codecRegistry;

        this.mongoTemplate.setSessionSynchronization(SessionSynchronization.ALWAYS);
    }

    @Override
    @Transactional
    public CheckingAccount createCheckingAccount(Client client) {
        if (client == null) {
            return null;
        }
        return (CheckingAccount) createAccount(client, AccountType.CHECKING);
    }

    @Override
    @Transactional
    public SavingAccount createSavingAccount(Client client) {
        if (client == null) {
            return null;
        }
        return (SavingAccount) createAccount(client, AccountType.SAVING);
    }

    @Transactional
    @Override
    public boolean deleteAccount(Client client, String accountNumber) {
        if (client == null) {
            return false;
        }
        client.getAccounts().remove(accountNumber);
        Client savedClient;
        long deletedAccount;

        try {
            savedClient = clientRepository.updateClientByClientId(client.getClientId());
            deletedAccount = accountRepository.deleteAccountByAccountNumber(accountNumber);
        } catch (RuntimeException e) {
            LOGGER.error("Error deleting account with account_number '{}': {}", accountNumber, e.getMessage());
            return false;
        }
        return savedClient != null && deletedAccount > 0;
    }

    @Override
    public Account getAccount(String accountNumber) {
        if (accountNumber.isEmpty()) {
            return null;
        }
        Account account;
        try {
            account = accountRepository.findByAccountNumber(accountNumber);
        } catch (RuntimeException e) {
            LOGGER.error("Error fetching account with accountNumber '{}': {}", accountNumber, e.getMessage());
            return null;
        }
        return account;
    }

    @Override
    public List<Account> getAllAccountsForClient(String clientId) throws JsonProcessingException {
        MongoDatabase db = mongoTemplate.getDb().withCodecRegistry(codecRegistry);
        MongoCollection<Document> clientCollection = db.getCollection(CLIENT_COLLECTION);

        /**
         * TODO: Update to create a type reference to hold a list of Accounts
         * Match: Filters the documents in the clients collection by clientId.
         *
         * Lookup: Joins the filtered documents with the accounts collection
         * based on the accounts field in the client documents and the accountNumber
         * field in the accounts' collection.
         *
         * Unwind: Deconstructs the accounts array to output one document for each
         * element of the array.
         *
         * Project: Reshapes the output to include only the accountNumber field.
         *
         * The result of the aggregation is a list of documents, each containing
         * the accountNumber field from the accounts collection associated with the
         * specified client ID. The results will ensure that we can pull the actual
         * Account object will all fields.
         */
        Bson match = match(eq("clientId", clientId));
        Bson lookup = lookup("accounts", "accounts", "accountNumber", "accounts");
        Bson unwind = unwind("$accounts", new UnwindOptions().preserveNullAndEmptyArrays(true));
        List<Bson> pipeline = asList(match, lookup, unwind,
                project(fields(excludeId(),
                        include("clientId"),
                        include("firstName"),
                        include("lastName"),
                        include("emailAddress"),
                        include("address"),
                        include("contacts"),
                        include("accounts"),
                        include("clientType"),
                        include("activeType"),
                        computed("accountNumber", "$accounts.accountNumber"))));

        List<Document> clientResultsWithAccountsList = clientCollection.aggregate(pipeline)
                .into(new ArrayList<>());

        if (!clientResultsWithAccountsList.isEmpty()) {
            Document result = (Document) clientResultsWithAccountsList.get(0).get("accounts");
            if (result != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    Account accounts = objectMapper.readValue(result.toJson(), new TypeReference<>() {});
                    LOGGER.info("accounts: " + accounts);
                    return List.of(accounts);
                } catch (Exception e) {
                    LOGGER.error("Error converting accounts: " + e.getMessage());
                }
            }
        }
        return new ArrayList<>();
    }

    private Account createAccount(Client client, AccountType accountType) {
        Client updatedClient = fetchClientWithAddress(client);

        if (updatedClient != null) {
            CurrencyType currencyType = BankingUtil.matchCountryCodeToCurrency(updatedClient);
            int accountSize = getAccountSize(updatedClient);
            String generationKey = updatedClient.getClientId() + accountSize;
            String accountNumber = BankingUtil.generateHashId(generationKey);

            Account account;
            if (accountType == AccountType.CHECKING) {
                account = CheckingAccount.builder()
                        .currencyType(currencyType)
                        .balance(BigDecimal.ZERO)
                        .accountType(accountType)
                        .accountNumber(accountNumber)
                        .routingNumber(ROUTING_NUMBER)
                        .activeType(ActiveType.ACTIVE)
                        .transactions(new ArrayList<>())
                        .creationDate(BankingUtil.now())
                        .debitCards(new ArrayList<>())
                        .build();
            } else {
                account = SavingAccount.builder()
                        .currencyType(currencyType)
                        .balance(BigDecimal.ZERO)
                        .accountType(accountType)
                        .accountNumber(accountNumber)
                        .routingNumber(ROUTING_NUMBER)
                        .activeType(ActiveType.ACTIVE)
                        .transactions(new ArrayList<>())
                        .creationDate(BankingUtil.now())
                        .interestRate(SAVINGS_INTEREST_RATE)
                        .build();
            }

            if (account != null) {
                try {
                    Account savedAccount = accountRepository.save(account);
                    updatedClient.getAccounts().add(savedAccount.getAccountNumber());
                    clientRepository.save(updatedClient);
                    return savedAccount;
                } catch (RuntimeException e) {
                    LOGGER.error("Failed to save created Account: {}", e.getMessage());
                    return null;
                }
            }
        }
        return null;
    }

    private int getAccountSize(Client client) {
        if (client.getAccounts() == null) {
            return 0;
        } else {
            return client.getAccounts().size() + 1;
        }
    }

    private Client fetchClientWithAddress(Client client) {
        if (client.getClientId() != null && client.getAddress() == null) {
            synchronized (clientRepository) {
                String clientId = client.getClientId();
                try {
                    client = clientRepository.findByClientId(clientId);
                    while (client == null || client.getAddress() == null) {
                        clientRepository.wait();
                        client = clientRepository.findByClientId(clientId);
                    }
                } catch (InterruptedException e) {
                    LOGGER.error("Error locking on client fetch: {}", e.getMessage());
                    return null;
                }
            }
        }
        return client;
    }
}
