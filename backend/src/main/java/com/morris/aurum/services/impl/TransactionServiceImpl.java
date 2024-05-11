package com.morris.aurum.services.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import com.morris.aurum.models.accounts.Account;
import com.morris.aurum.models.transactions.DepositTransaction;
import com.morris.aurum.models.transactions.Transaction;
import com.morris.aurum.models.types.TransactionType;
import com.morris.aurum.repositories.AccountRepository;
import com.morris.aurum.repositories.TransactionRepository;
import com.morris.aurum.services.TransactionService;
import com.morris.aurum.utils.BankingUtil;
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

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;

@Service
public class TransactionServiceImpl implements TransactionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final MongoTemplate mongoTemplate;
    private final CodecRegistry codecRegistry;

    private static final String TRANSACTIONS_COLLECTION = "clients";
    private static final String ACCOUNTS_COLLECTION = "accounts";

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository,
                                  MongoTemplate mongoTemplate, CodecRegistry codecRegistry) {

        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.mongoTemplate = mongoTemplate;
        this.codecRegistry = codecRegistry;

        this.mongoTemplate.setSessionSynchronization(SessionSynchronization.ALWAYS);
    }

    @Override
    public List<Transaction> getMostRecentTransactions(String accountNumber) {
        Account account = accountRepository.findAllRecentTransactionsByAccountNumber(accountNumber);
        return account.getTransactions();
    }

    @Override
    public List<Transaction> getMostRecentDeposits(String accountNumber) {
        Account account = accountRepository.findAllRecentTransactionsByAccountNumber(accountNumber);
        List<Transaction> transactions = account.getTransactions();
        return getTransactionTypeFromTransactions(transactions, TransactionType.DEPOSIT);
    }

    @Override
    public List<Transaction> getMostRecentWithdraws(String accountNumber) {
        Account account = accountRepository.findAllRecentTransactionsByAccountNumber(accountNumber);
        List<Transaction> transactions = account.getTransactions();
        return getTransactionTypeFromTransactions(transactions, TransactionType.WITHDRAW);
    }

    @Override
    public List<Transaction> getMostRecentTransfers(String accountNumber) {
        Account account = accountRepository.findAllRecentTransactionsByAccountNumber(accountNumber);
        List<Transaction> transactions = account.getTransactions();
        return getTransactionTypeFromTransactions(transactions, TransactionType.TRANSFER);
    }

    private List<Transaction> getTransactionTypeFromTransactions(List<Transaction> transactions, TransactionType type) {
        List<Transaction> transactionTypeList = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionType() == type) {
                transactionTypeList.add(transaction);
            }
        }
        return transactionTypeList;
    }

    @Transactional
    @Override
    public Transaction createDeposit(String toAccountNumber, BigDecimal depositAmount) {
        if (BankingUtil.isNullOrEmpty(toAccountNumber) || depositAmount.compareTo(new BigDecimal(0)) <= 0) {
            return null;
        }
        // This operation influences two collection: accounts and transactions. A single document from accounts will
        // contain that account's 10 most recent transactions. So, not only do we need to update the account document
        // to remove the latest transaction and add this newest transactions. We also need to update the account balance
        // and ensure that the transaction is added to the transactions' collection.
        //
        // 1. Find the latest transaction document embedded on this account, delete it from this account's
        //    transactions array, and insert it into the transaction collection.
        //
        // 2. Set an embedded transaction document for this newest transaction onto this account.
        //
        // 3. Update this account's account balance.
        //
        // This will all be done in a single isolated and ACID compliant transaction. It should be noted that, we aren't
        // making any JOIN operations. There is no data be joined, and only a series of FIND, DELETE, INSERT, UPDATE ops.
        Account updatedAccount = fetchAccountWithAddress(toAccountNumber);
        if (updatedAccount != null) {
            boolean needToPullLatestTransaction;
            Transaction latestTransaction;

            // The latest 15 transactions will be embedded on the account.
            if (updatedAccount.getTransactions() != null && updatedAccount.getTransactions().size() == 15) {
                latestTransaction = getLatestTransaction(updatedAccount.getTransactions());
                needToPullLatestTransaction = true;
            } else {
                latestTransaction = null;
                needToPullLatestTransaction = false;
            }
            BigDecimal updatedBalance = calculateBalanceWithDeposit(updatedAccount, depositAmount);
            String transactionId = BankingUtil.generateTransactionHash(updatedAccount.getAccountNumber() + BankingUtil.now());

            // let's get the transaction ready to go.
            Transaction depositTransaction = DepositTransaction.builder()
                    .transactionId(transactionId)
                    .currencyType(updatedAccount.getCurrencyType())
                    .creationDate(BankingUtil.now())
                    .transactionType(TransactionType.DEPOSIT)
                    .depositAmount(depositAmount)
                    .toAccount(updatedAccount.getAccountNumber())
                    .endingBalance(updatedBalance)
                    .build();

            // let's conduct our transaction
            boolean result = false;

            MongoDatabase db = mongoTemplate.getDb().withCodecRegistry(codecRegistry);
            MongoCollection<Account> accountsCollection = db.getCollection(ACCOUNTS_COLLECTION, Account.class);

            Bson account = eq("accountNumber", toAccountNumber);
            Bson pushTheDepositOnAccount = push("transactions", depositTransaction);
            Bson updateAccountBalance = set("balance", updatedBalance);

            // 1. Find the latest transaction document embedded on this account, delete it from this account's
            //    transactions array, and insert it into the transaction collection.
            UpdateResult updateLatestTransactionOnAccountResult = null;
            if (needToPullLatestTransaction && latestTransaction != null) {
                Bson pullTheLastTransaction = pull("transactions", latestTransaction.getTransactionId());
                updateLatestTransactionOnAccountResult = accountsCollection.updateOne(account, pullTheLastTransaction);
            }
            Transaction insertedLatestTransaction = transactionRepository.insert(depositTransaction);

            // 2. Set an embedded transaction document for this newest transaction onto this account.
            UpdateResult updatePushDepositOnAccountResult = accountsCollection.updateOne(account, pushTheDepositOnAccount);
            // 3. Update this account's account balance
            UpdateResult updateAccountBalanceResult = accountsCollection.updateOne(account, updateAccountBalance);

            // build our results
            boolean updateLatestTransactionAcknowledgement = false;
            if (!needToPullLatestTransaction) {
                updateLatestTransactionAcknowledgement = true;
            } else {
                if (updateLatestTransactionOnAccountResult != null) {
                    updateLatestTransactionAcknowledgement = updateLatestTransactionOnAccountResult.wasAcknowledged();
                }
            }
            result = updateLatestTransactionAcknowledgement && !insertedLatestTransaction.getTransactionId().isEmpty()
                        && updatePushDepositOnAccountResult.wasAcknowledged() && updateAccountBalanceResult.wasAcknowledged();

            if (result) {
                return insertedLatestTransaction;
            }
        }
        return null;
    }

    private BigDecimal calculateBalanceWithDeposit(Account account, BigDecimal depositAmount) {
        return account.getBalance().add(depositAmount);
    }

    private Transaction getLatestTransaction(List<Transaction> transactions) {
        Transaction latest = transactions.get(0);
        for (int i = 1; i < transactions.size(); i++) {
            Transaction currentDateObject = transactions.get(i);
            if (currentDateObject.getCreationDate().compareTo(latest.getCreationDate()) > 0 ) {
                // compared date is greater than latest
                latest = currentDateObject;
            }
        }
        return latest;
    }

    private Account fetchAccountWithAddress(String accountNumber) {
        Account account;
        synchronized (accountRepository) {
            try {
                account = accountRepository.findByAccountNumber(accountNumber);
                while (account == null) {
                    accountRepository.wait();
                    account = accountRepository.findByAccountNumber(accountNumber);
                }
            } catch (InterruptedException e) {
                LOGGER.error("Error locking on account fetch: {}", e.getMessage());
                return null;
            }
        }
        return account;
    }
}
