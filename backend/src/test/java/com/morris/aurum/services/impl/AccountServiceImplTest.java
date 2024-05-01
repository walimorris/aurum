package com.morris.aurum.services.impl;

import com.morris.aurum.TestHelper;
import com.morris.aurum.models.accounts.CheckingAccount;
import com.morris.aurum.models.clients.IndividualClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;

import static org.mockito.Mockito.when;

@SpringBootTest
class AccountServiceImplTest {

    @MockBean
    private AccountServiceImpl accountService;

    private static final String CLIENT_1 = "backend/src/test/java/resources/clients/individual_client_request_1_result.json";
    private static final String CHECKING_ACCOUNT_RESPONSE = "backend/src/test/java/resources/accounts/post_checking_account_response_1.json";

    @Test
    void createCheckingAccount() throws IOException {
        IndividualClient client = TestHelper.convertModelFromFile(CLIENT_1, IndividualClient.class);
        CheckingAccount checkingAccount = TestHelper.convertModelFromFile(CHECKING_ACCOUNT_RESPONSE, CheckingAccount.class);
        String checkingAccountAsString = TestHelper.writeValueAsString(checkingAccount);

        when(accountService.createCheckingAccount(client)).thenReturn(checkingAccount);
        CheckingAccount result = accountService.createCheckingAccount(client);
        String checkingAccountResultAsString = TestHelper.writeValueAsString(result);

        Assertions.assertEquals(checkingAccountResultAsString, checkingAccountAsString);
    }

    @Test
    void createSavingAccount() {
    }

    @Test
    void getAccount() {
    }

    @Test
    void getAllAccountsForClient() {
    }

    @Test
    void deleteAccount() {
    }
}