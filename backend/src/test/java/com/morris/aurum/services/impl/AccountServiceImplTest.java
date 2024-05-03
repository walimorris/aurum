package com.morris.aurum.services.impl;

import com.morris.aurum.TestHelper;
import com.morris.aurum.models.accounts.CheckingAccount;
import com.morris.aurum.models.accounts.SavingAccount;
import com.morris.aurum.models.clients.IndividualClient;
import com.morris.aurum.models.properties.CountryCodeCurrencyProperties;
import com.morris.aurum.repositories.AccountRepository;
import com.morris.aurum.repositories.ClientRepository;
import com.morris.aurum.utils.BankingUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CountryCodeCurrencyProperties countryCodeCurrencyProperties;

    @Mock
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private static final String CLIENT_1 = "backend/src/test/java/resources/clients/individual_client_request_1_result.json";
    private static final String INDIVIDUAL_CLIENT_WITH_ACCOUNTS = "backend/src/test/java/resources/clients/individual_client_request_1_result.json";
    private static final String CHECKING_ACCOUNT_RESPONSE = "backend/src/test/java/resources/accounts/post_checking_account_response_1.json";
    private static final String SAVING_ACCOUNT_RESPONSE = "backend/src/test/java/resources/accounts/post_saving_account_response_1.json";

    @Test
    void createCheckingAccount() throws IOException {
        // The given account number from the test account json resource should be computed in this test
        // and the result should be compared to the result from the out of test. This ensures methods
        // are computing proper inputs to correct outputs.
        String clientId = "31796311";
        int accountSize = 2;
        String computedHash = BankingUtil.generateHashId(clientId + accountSize);

        IndividualClient client = TestHelper.convertModelFromFile(CLIENT_1, IndividualClient.class);
        CheckingAccount checkingAccount = TestHelper.convertModelFromFile(CHECKING_ACCOUNT_RESPONSE, CheckingAccount.class);
        String checkingAccountAsString = TestHelper.writeValueAsString(checkingAccount);

        when(clientRepository.findByClientId(client.getClientId())).thenReturn(client);
        when(clientRepository.save(any())).thenReturn(client);
        when(accountRepository.save(any(CheckingAccount.class))).thenReturn(checkingAccount);

        when(accountService.createCheckingAccount(client)).thenReturn(checkingAccount);
        CheckingAccount result = accountService.createCheckingAccount(client);
        String checkingAccountResultAsString = TestHelper.writeValueAsString(result);

        Assertions.assertEquals(checkingAccountResultAsString, checkingAccountAsString);
    }

    @Test
    void createSavingAccount() throws IOException {
        IndividualClient client = TestHelper.convertModelFromFile(CLIENT_1, IndividualClient.class);
        SavingAccount savingAccount = TestHelper.convertModelFromFile(SAVING_ACCOUNT_RESPONSE, SavingAccount.class);
        String savingAccountAsString = TestHelper.writeValueAsString(savingAccount);

        when(accountService.createSavingAccount(client)).thenReturn(savingAccount);
        SavingAccount result = accountService.createSavingAccount(client);
        String savingAccountResultAsString = TestHelper.writeValueAsString(result);

        Assertions.assertEquals(savingAccountResultAsString, savingAccountAsString);
    }

    @Test
    void deleteAccount() throws IOException {
        // If these account numbers below are updated in the individual client resource test file, they must also be updated here.
        String accountNumber_1 = "111111111";
        String accountNumber_2 = "222222222";
        IndividualClient individualClient = TestHelper.convertModelFromFile(INDIVIDUAL_CLIENT_WITH_ACCOUNTS, IndividualClient.class);
        individualClient.getAccounts().remove(accountNumber_1);

        // Ensure the account_1 was removed and account_2 still exists
        assertAll(
                () -> assertEquals(1, individualClient.getAccounts().size()),
                () -> assertTrue(individualClient.getAccounts().contains(accountNumber_2))
        );

        when(clientRepository.updateClientByClientId(individualClient.getClientId())).thenReturn(individualClient);
        when(accountRepository.deleteAccountByAccountNumber(accountNumber_1)).thenReturn(1L);

        boolean result = accountService.deleteAccount(individualClient, accountNumber_1);
        assertTrue(result);
    }

    @Test
    void getAccount() {
    }

    @Test
    void getAllAccountsForClient() {
    }
}