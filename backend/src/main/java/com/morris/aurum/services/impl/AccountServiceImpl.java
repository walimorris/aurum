package com.morris.aurum.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ClientSessionOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.TransactionBody;
import com.mongodb.client.model.UnwindOptions;
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
import com.morris.aurum.repositories.ClientRepository;
import com.morris.aurum.services.AccountService;
import com.morris.aurum.utils.BankingUtil;
import org.bson.BsonDateTime;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

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
                              BankingUtil bankingUtil, MongoTemplate mongoTemplate, CodecRegistry codecRegistry,
                              ClientRepository clientRepository) {

        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.countryCodeProperties = countryCodeProperties;
        this.bankingUtil = bankingUtil;
        this.mongoTemplate = mongoTemplate;
        this.codecRegistry = codecRegistry;
    }

    @Override
    public Account createCheckingAccount(Client client) {
        if (client.getClientId() != null) {
            if (client.getAddress() == null) {
                // We use synchronize here to ensure that clientRepository can not be used and
                // ensuring thread safety, and no modifications on repository concurrently.
                // This reduces the chance of race conditions and preventing other threads
                // from modifying the repository.

                synchronized (clientRepository) {
                    String clientId = client.getClientId();
                    try {
                        client = clientRepository.findByClientId(clientId);
                        while (client == null) {
                            clientRepository.wait();
                            client = clientRepository.findByClientId(clientId);
                        }
                    } catch (InterruptedException e) {
                        LOGGER.error("Error locking on client fetch: {}", e.getMessage());
                        return null;
                    }
                }
            }
        }

        CurrencyType currencyType = matchCountryCodeToCurrency(client);
        int accountSize = client.getAccounts() == null ? 0 : client.getAccounts().size();
        String accountNumber = bankingUtil.generateHashId(client.getClientId() + accountSize);
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
    public Account getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
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
