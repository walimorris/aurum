package com.morris.aurum.controllers;

import com.morris.aurum.TestHelper;
import com.morris.aurum.models.accounts.Account;
import com.morris.aurum.models.accounts.CheckingAccount;
import com.morris.aurum.models.accounts.SavingAccount;
import com.morris.aurum.models.clients.IndividualClient;
import com.morris.aurum.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {

    @Autowired
    MockMvc mockModelViewController;

    @MockBean
    private AccountService accountService;

    private static final String UTF_8 = "UTF-8";

    private static final String CLIENT_1 = "backend/src/test/java/resources/clients/individual_client_request_1_result.json";
    private static final String CLIENT_1_ID = "31796311";
    private static final String CHECKING_ACCOUNT_RESPONSE = "backend/src/test/java/resources/accounts/checking_account_response_1.json";
    private static final String CHECKING_ACCOUNT_NUMBER_1 = "226987653";
    private static final String SAVING_ACCOUNT_RESPONSE = "backend/src/test/java/resources/accounts/saving_account_response_1.json";
    private static final String POST_NEW_INDIVIDUAL_CHECKING_ACCOUNT_REQUEST = "/aurum/api/accounts/individual/createCheckingAccount";
    private static final String POST_NEW_INDIVIDUAL_SAVING_ACCOUNT_REQUEST = "/aurum/api/accounts/individual/createSavingAccount";
    private static final String POST_DELETE_ACCOUNT = "/arum/api/accounts/deleteAccount";
    private static final String GET_ACCOUNT = "/aurum/api/accounts/getCheckingAccount";
    private static final String GET_ACCOUNT_PARAM = "accountNumber";
    private static final String GET_ALL_ACCOUNTS = "/aurum/api/accounts/getAllAccounts";
    private static final String GET_ALL_ACCOUNTS_PARAM = "clientId";


    @Test
    void createIndividualCheckingAccount() throws Exception {
        IndividualClient client = TestHelper.convertModelFromFile(CLIENT_1, IndividualClient.class);
        CheckingAccount checkingAccount = TestHelper.convertModelFromFile(CHECKING_ACCOUNT_RESPONSE, CheckingAccount.class);
        String clientAsString = TestHelper.writeValueAsString(client);
        String checkingAccountAsString = TestHelper.writeValueAsString(checkingAccount);

        when(accountService.createCheckingAccount(any())).thenReturn(checkingAccount);
        this.mockModelViewController.perform(post(POST_NEW_INDIVIDUAL_CHECKING_ACCOUNT_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientAsString)
                .characterEncoding(UTF_8))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(checkingAccountAsString));
    }

    @Test
    void createIndividualSavingAccount() throws Exception {
        IndividualClient client = TestHelper.convertModelFromFile(CLIENT_1, IndividualClient.class);
        SavingAccount savingAccount = TestHelper.convertModelFromFile(SAVING_ACCOUNT_RESPONSE, SavingAccount.class);
        String clientAsString = TestHelper.writeValueAsString(client);
        String savingAccountAsString = TestHelper.writeValueAsString(savingAccount);

        when(accountService.createSavingAccount(any())).thenReturn(savingAccount);
        this.mockModelViewController.perform(post(POST_NEW_INDIVIDUAL_SAVING_ACCOUNT_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientAsString)
                        .characterEncoding(UTF_8))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(savingAccountAsString));
    }

    @Test
    void deleteAccount() throws IOException {

    }

    @Test
    void getAccount() throws Exception {
        CheckingAccount checkingAccount = TestHelper.convertModelFromFile(CHECKING_ACCOUNT_RESPONSE, CheckingAccount.class);
        String checkingAccountAsString = TestHelper.writeValueAsString(checkingAccount);

        when(accountService.getAccount(CHECKING_ACCOUNT_NUMBER_1)).thenReturn(checkingAccount);

        this.mockModelViewController.perform(get(GET_ACCOUNT).param(GET_ACCOUNT_PARAM, CHECKING_ACCOUNT_NUMBER_1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(checkingAccountAsString));
    }

    @Test
    void getAllAccounts() throws Exception {
        CheckingAccount checkingAccount = TestHelper.convertModelFromFile(CHECKING_ACCOUNT_RESPONSE, CheckingAccount.class);
        SavingAccount savingAccount = TestHelper.convertModelFromFile(SAVING_ACCOUNT_RESPONSE, SavingAccount.class);
        List<Account> accounts = List.of(checkingAccount, savingAccount);
        String accountsAsString = TestHelper.writeValueAsString(accounts);

        when(accountService.getAllAccountsForClient(CLIENT_1_ID)).thenReturn(accounts);

        this.mockModelViewController.perform(get(GET_ALL_ACCOUNTS).param(GET_ALL_ACCOUNTS_PARAM, CLIENT_1_ID))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(accountsAsString));
    }
}