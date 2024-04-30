package com.morris.aurum.controllers;

import com.morris.aurum.TestHelper;
import com.morris.aurum.models.accounts.Account;
import com.morris.aurum.models.accounts.CheckingAccount;
import com.morris.aurum.models.clients.Client;
import com.morris.aurum.models.clients.IndividualClient;
import com.morris.aurum.services.AccountService;
import com.morris.aurum.services.impl.AccountServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {

    @Autowired
    MockMvc mockModelViewController;

    @MockBean
    AccountService accountService;

    private static final String UTF_8 = "UTF-8";

    private static final String CLIENT_1 = "backend/src/test/java/resources/clients/individual_client_request_1_result.json";
    private static final String CHECKING_ACCOUNT_RESPONSE = "backend/src/test/java/resources/accounts/post_checking_account_response_1.json";
    private static final String POST_NEW_INDIVIDUAL_CHECKING_ACCOUNT_REQUEST = "/aurum/api/accounts/individual/createCheckingAccount";


    @Test
    void createCheckingAccount() throws Exception {
        IndividualClient client = TestHelper.convertModelFromFile(CLIENT_1, IndividualClient.class);
        Account checkingAccount = TestHelper.convertModelFromFile(CHECKING_ACCOUNT_RESPONSE, CheckingAccount.class);
        String clientAsString = TestHelper.writeValueAsString(client);
        String checkingAccountAsString = TestHelper.writeValueAsString(checkingAccount);

        when(accountService.createCheckingAccount(client)).thenReturn(checkingAccount);
        this.mockModelViewController.perform(post(POST_NEW_INDIVIDUAL_CHECKING_ACCOUNT_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientAsString)
                .characterEncoding(UTF_8))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    void createSavingAccount() {
    }

    @Test
    void deleteAccount() {
    }

    @Test
    void getAccount() {
    }

    @Test
    void getAllAccounts() {
    }
}