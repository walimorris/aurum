package com.morris.aurum.services.impl;

import com.morris.aurum.TestHelper;
import com.morris.aurum.models.accounts.CheckingAccount;
import com.morris.aurum.repositories.AccountRepository;
import com.morris.aurum.repositories.TransactionRepository;
import org.bson.codecs.configuration.CodecRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = {"coverage"})
class TransactionServiceImplTest {
    private final AccountRepository accountRepositoryMock = Mockito.mock(AccountRepository.class);
    private final TransactionRepository transactionRepositoryMock = Mockito.mock(TransactionRepository.class);
    private final MongoTemplate mongoTemplateMock = Mockito.mock(MongoTemplate.class);
    private final CodecRegistry codecRegistryMock = Mockito.mock(CodecRegistry.class);

    private TransactionServiceImpl transactionService;

    private static final String ACCOUNT_WITH_TRANSACTIONS = "backend/src/test/java/resources/transactions/account_with_transactions.json";

    @BeforeEach
    void setUp() {
        // This is constructor injection and manually providing mock objects. This method has shown to be more
        // reliable than other ways of dependency injection. All tests should be made this way.
        transactionService = new TransactionServiceImpl(transactionRepositoryMock, accountRepositoryMock, mongoTemplateMock, codecRegistryMock);
    }

    @Test
    @Disabled
    void getMostRecentTransactions() throws IOException {
        CheckingAccount account = TestHelper.convertModelFromFile(ACCOUNT_WITH_TRANSACTIONS, CheckingAccount.class);
        System.out.println(account.getTransactions().size());
    }

    @Test
    void getMostRecentDeposits() {
    }

    @Test
    void getMostRecentWithdraws() {
    }

    @Test
    void getMostRecentTransfers() {
    }

    @Test
    void createDeposit() {
    }
}