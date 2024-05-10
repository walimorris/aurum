package com.morris.aurum.services.impl;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import com.morris.aurum.TestHelper;
import com.morris.aurum.models.accounts.Account;
import com.morris.aurum.models.accounts.CheckingAccount;
import com.morris.aurum.models.accounts.SavingAccount;
import com.morris.aurum.models.clients.Client;
import com.morris.aurum.models.clients.IndividualClient;
import com.morris.aurum.models.types.AccountType;
import com.morris.aurum.repositories.AccountRepository;
import com.morris.aurum.repositories.ClientRepository;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = {"coverage"})
class AccountServiceImplTest {
    private final AccountRepository accountRepositoryMock = Mockito.mock(AccountRepository.class);
    private final ClientRepository clientRepositoryMock = Mockito.mock(ClientRepository.class);
    private final MongoTemplate mongoTemplateMock = Mockito.mock(MongoTemplate.class);
    private final CodecRegistry codecRegistryMock = Mockito.mock(CodecRegistry.class);

    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        // This is constructor injection and manually providing mock objects. This method has shown to be more
        // reliable than other ways of dependency injection. All tests should be made this way.
        accountService = new AccountServiceImpl(accountRepositoryMock, clientRepositoryMock, mongoTemplateMock, codecRegistryMock);
    }

    private static final String CLIENT_1 = "backend/src/test/java/resources/clients/individual_client_request_1_result.json";
    private static final String CLIENT_1_CLIENT_ID = "31796311";
    private static final String INDIVIDUAL_CLIENT_WITH_ACCOUNTS = "backend/src/test/java/resources/clients/individual_client_request_1_result.json";
    private static final String INDIVIDUAL_CLIENT_ACCOUNT_NUMBER_1 = "111111111";
    private static final String INDIVIDUAL_CLIENT_ACCOUNT_NUMBER_2 = "222222222";
    private static final String INDIVIDUAL_CLIENT_WITH_ACCOUNTS_2 = "backend/src/test/java/resources/clients/individual_client_request_2_result.json";
    private static final String INDIVIDUAL_CLIENT_2_ACCOUNT_NUMBER_1 = "226987653";
    private static final String INDIVIDUAL_CLIENT_2_ACCOUNT_NUMBER_2 = "226987651";
    private static final String CHECKING_ACCOUNT_RESPONSE = "backend/src/test/java/resources/accounts/checking_account_response_1.json";
    private static final String CHECKING_ACCOUNT_RESPONSE_ACCOUNT_NUMBER = "226987653";
    private static final String SAVING_ACCOUNT_RESPONSE = "backend/src/test/java/resources/accounts/saving_account_response_1.json";

    @Test
    void createCheckingAccount_NullClient_ReturnsNull() {
        Client nullClient = null;
        CheckingAccount result = accountService.createCheckingAccount(nullClient);
        assertNull(result);
        verifyNoInteractions(clientRepositoryMock, accountRepositoryMock);
    }

    @Test
    void createCheckingAccount_SuccessfulCreation_ReturnsCheckingAccount() throws IOException {
        IndividualClient client = TestHelper.convertModelFromFile(CLIENT_1, IndividualClient.class);
        Account checkingAccount = TestHelper.convertModelFromFile(CHECKING_ACCOUNT_RESPONSE, CheckingAccount.class);

        when(accountRepositoryMock.save(any(Account.class))).thenReturn(checkingAccount);
        CheckingAccount result = accountService.createCheckingAccount(client);

        assertNotNull(result);
        assertEquals(CLIENT_1_CLIENT_ID, client.getClientId());
        assertEquals(AccountType.CHECKING, result.getAccountType());
    }

    @Test
    void createCheckingAccount_ErrorDuringCreation_ReturnsNull() throws IOException {
        IndividualClient client = TestHelper.convertModelFromFile(CLIENT_1, IndividualClient.class);

        when(clientRepositoryMock.findByClientId(CLIENT_1_CLIENT_ID)).thenReturn(client);
        when(accountRepositoryMock.save(any(Account.class))).thenThrow(new RuntimeException("Failed to save account"));
        CheckingAccount result = accountService.createCheckingAccount(client);

        assertNull(result);
        verify(accountRepositoryMock).save(any(Account.class));
        verifyNoMoreInteractions(clientRepositoryMock, accountRepositoryMock);
    }

    @Test
    void createSavingAccount_NullClient_ReturnsNull() {
        Client nullClient = null;
        SavingAccount result = accountService.createSavingAccount(nullClient);
        assertNull(result);
        verifyNoInteractions(clientRepositoryMock, accountRepositoryMock);
    }

    @Test
    void createSavingAccount_SuccessfulCreation_ReturnsCheckingAccount() throws IOException {
        IndividualClient client = TestHelper.convertModelFromFile(CLIENT_1, IndividualClient.class);
        Account savingAccount = TestHelper.convertModelFromFile(SAVING_ACCOUNT_RESPONSE, SavingAccount.class);

        when(accountRepositoryMock.save(any(Account.class))).thenReturn(savingAccount);
        SavingAccount result = accountService.createSavingAccount(client);

        assertNotNull(result);
        assertEquals(CLIENT_1_CLIENT_ID, client.getClientId());
        assertEquals(AccountType.SAVING, result.getAccountType());
    }

    @Test
    void createSavingAccount_ErrorDuringCreation_ReturnsNull() throws IOException {
        IndividualClient client = TestHelper.convertModelFromFile(CLIENT_1, IndividualClient.class);

        when(clientRepositoryMock.findByClientId(CLIENT_1_CLIENT_ID)).thenReturn(client);
        when(accountRepositoryMock.save(any(Account.class))).thenThrow(new RuntimeException("Failed to save account"));
        SavingAccount result = accountService.createSavingAccount(client);

        assertNull(result);
        verify(accountRepositoryMock).save(any(Account.class));
        verifyNoMoreInteractions(clientRepositoryMock, accountRepositoryMock);
    }

    @Test
    void deleteAccount_Successful_Delete_ReturnsTrue() throws IOException {
        // If these account numbers below are updated in the individual client resource test file, they must also be updated here.
        IndividualClient individualClient = TestHelper.convertModelFromFile(INDIVIDUAL_CLIENT_WITH_ACCOUNTS, IndividualClient.class);
        individualClient.getAccounts().remove(INDIVIDUAL_CLIENT_ACCOUNT_NUMBER_1);

        // Ensure the account_1 was removed and account_2 still exists
        assertAll(
                () -> assertEquals(1, individualClient.getAccounts().size()),
                () -> assertTrue(individualClient.getAccounts().contains(INDIVIDUAL_CLIENT_ACCOUNT_NUMBER_2))
        );

        // we need to mock mongoTemplate processes for account array updates on client
        MongoDatabase mongoDatabaseMock = Mockito.mock(MongoDatabase.class);
        when(mongoTemplateMock.getDb()).thenReturn(mongoDatabaseMock);
        when(mongoTemplateMock.getDb().withCodecRegistry(any(CodecRegistry.class))).thenReturn(mongoDatabaseMock);
        MongoCollection<Document> mongoCollectionMock = Mockito.mock(MongoCollection.class);
        when(mongoDatabaseMock.getCollection(any())).thenReturn(mongoCollectionMock);

        // let's mock the update and results
        UpdateResult updateResultMock = Mockito.mock(UpdateResult.class);
        when(mongoCollectionMock.updateOne(any(Bson.class), any(Bson.class))).thenReturn(updateResultMock);
        when(updateResultMock.wasAcknowledged()).thenReturn(true);

        when(accountRepositoryMock.deleteAccountByAccountNumber(INDIVIDUAL_CLIENT_ACCOUNT_NUMBER_1)).thenReturn(1L);

        boolean result = accountService.deleteAccount(individualClient.getClientId(), INDIVIDUAL_CLIENT_ACCOUNT_NUMBER_1);
        assertTrue(result);
    }

    @Test
    void deleteAccount_NullClient_EmptyClient_ReturnsFalse() {
        boolean result1 = accountService.deleteAccount(null, INDIVIDUAL_CLIENT_ACCOUNT_NUMBER_1);
        boolean result2 = accountService.deleteAccount(CLIENT_1_CLIENT_ID, "");
        boolean result3 = accountService.deleteAccount("", INDIVIDUAL_CLIENT_ACCOUNT_NUMBER_1);
        boolean result4 = accountService.deleteAccount(CLIENT_1_CLIENT_ID, null);
        assertAll(
                () -> assertFalse(result1),
                () -> assertFalse(result2),
                () -> assertFalse(result3),
                () -> assertFalse(result4),
                () -> verifyNoInteractions(clientRepositoryMock, accountRepositoryMock)
        );
    }

    @Test
    void deleteAccount_ErrorDuringDeletion_ReturnsFalse() throws IOException {
        // If these account numbers below are updated in the individual client resource test file, they must also be updated here.
        IndividualClient individualClient = TestHelper.convertModelFromFile(INDIVIDUAL_CLIENT_WITH_ACCOUNTS, IndividualClient.class);
        individualClient.getAccounts().remove(INDIVIDUAL_CLIENT_ACCOUNT_NUMBER_1);

        // Ensure the account_1 was removed and account_2 still exists
        assertAll(
                () -> assertEquals(1, individualClient.getAccounts().size()),
                () -> assertTrue(individualClient.getAccounts().contains(INDIVIDUAL_CLIENT_ACCOUNT_NUMBER_2))
        );

        // we need to mock mongoTemplate processes for account array updates on client
        MongoDatabase mongoDatabaseMock = Mockito.mock(MongoDatabase.class);
        when(mongoTemplateMock.getDb()).thenReturn(mongoDatabaseMock);
        when(mongoTemplateMock.getDb().withCodecRegistry(any(CodecRegistry.class))).thenReturn(mongoDatabaseMock);
        MongoCollection<Document> mongoCollectionMock = Mockito.mock(MongoCollection.class);
        when(mongoDatabaseMock.getCollection(any())).thenReturn(mongoCollectionMock);

        UpdateResult updateResultMock = Mockito.mock(UpdateResult.class);
        when(mongoCollectionMock.updateOne(any(Bson.class), any(Bson.class))).thenReturn(updateResultMock);
        when(updateResultMock.wasAcknowledged()).thenReturn(true);

        when(accountRepositoryMock.deleteAccountByAccountNumber(INDIVIDUAL_CLIENT_ACCOUNT_NUMBER_1))
                .thenThrow(new RuntimeException("Failed to delete account"));

        boolean result = accountService.deleteAccount(individualClient.getClientId(), INDIVIDUAL_CLIENT_ACCOUNT_NUMBER_1);
        assertFalse(result);
        assertThrows(RuntimeException.class, () -> accountRepositoryMock.deleteAccountByAccountNumber(INDIVIDUAL_CLIENT_ACCOUNT_NUMBER_1));
    }

    @Test
    void getAccount_SUCCESSFUL_ReturnsAccount() throws IOException {
        Account account = TestHelper.convertModelFromFile(CHECKING_ACCOUNT_RESPONSE, CheckingAccount.class);

        when(accountRepositoryMock.findByAccountNumber(CHECKING_ACCOUNT_RESPONSE_ACCOUNT_NUMBER)).thenReturn(account);
        Account accountResult = accountService.getAccount(CHECKING_ACCOUNT_RESPONSE_ACCOUNT_NUMBER);

        assertEquals(accountResult.getAccountNumber(), CHECKING_ACCOUNT_RESPONSE_ACCOUNT_NUMBER);
        assertNotNull(accountResult);
    }

    @Test
    void getAccount_EmptyAccountNumber_ReturnsNull() {
        Account accountResult = accountService.getAccount("");
        assertNull(accountResult);
    }

    @Test
    void getAccount_ErrorDuringFetch_ReturnsNull() throws IOException {
        when(accountRepositoryMock.findByAccountNumber(CHECKING_ACCOUNT_RESPONSE_ACCOUNT_NUMBER))
                .thenThrow(new RuntimeException("Failed to fetch Account"));

        Account accountResult = accountService.getAccount(CHECKING_ACCOUNT_RESPONSE_ACCOUNT_NUMBER);
        assertNull(accountResult);
    }

    @Test
    void getAllAccountsForClient() throws IOException {
        List<Document> clientResultsWithAccountsList = new ArrayList<>();

        // get the accounts
        CheckingAccount account1 = TestHelper.convertModelFromFile(CHECKING_ACCOUNT_RESPONSE, CheckingAccount.class);
        SavingAccount account2 = TestHelper.convertModelFromFile(SAVING_ACCOUNT_RESPONSE, SavingAccount.class);

        // get account json representation
        String account1AsJsonString = TestHelper.asJSONString(account1);
        String account2AsJsonString = TestHelper.asJSONString(account2);

        // we will wrap a document manually - these should be account
        Document accountDocument1 = Document.parse(account1AsJsonString);
        Document accountDocument2 = Document.parse(account2AsJsonString);

        clientResultsWithAccountsList.add(accountDocument1);
        clientResultsWithAccountsList.add(accountDocument2);

        MongoDatabase mongoDatabaseMock = Mockito.mock(MongoDatabase.class);
        when(mongoTemplateMock.getDb()).thenReturn(mongoDatabaseMock);
        when(mongoTemplateMock.getDb().withCodecRegistry(any(CodecRegistry.class))).thenReturn(mongoDatabaseMock);
        MongoCollection<Document> mongoCollectionMock = Mockito.mock(MongoCollection.class);
        when(mongoDatabaseMock.getCollection(any())).thenReturn(mongoCollectionMock);
        AggregateIterable<Document> aggregateIterableMock = Mockito.mock(AggregateIterable.class);
        when(mongoCollectionMock.aggregate(any())).thenReturn(aggregateIterableMock);
        when(aggregateIterableMock.into(any())).thenReturn(new ArrayList<>(clientResultsWithAccountsList));

        List<Account> result = accountService.getAllAccountsForClient(CLIENT_1_CLIENT_ID);

        assertNotNull(result);
        assertEquals(2, result.size()); // we passed two accounts we should get two back
    }
}