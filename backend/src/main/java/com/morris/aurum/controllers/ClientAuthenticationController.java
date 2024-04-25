package com.morris.aurum.controllers;

import com.morris.aurum.models.accounts.Account;
import com.morris.aurum.models.clients.Client;
import com.morris.aurum.models.requests.CreateClientRequest;
import com.morris.aurum.models.types.AccountType;
import com.morris.aurum.models.types.ClientType;
import com.morris.aurum.services.AccountService;
import com.morris.aurum.services.ClientAuthenticationService;
import com.morris.aurum.services.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/aurum/api/auth")
public class ClientAuthenticationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientAuthenticationController.class);
    private final ClientAuthenticationService clientAuthenticationService;
    private final ClientService clientService;
    private final AccountService accountService;


    @Autowired
    public ClientAuthenticationController(ClientAuthenticationService clientAuthenticationService,
                                          ClientService clientService, AccountService accountService) {

        this.clientAuthenticationService = clientAuthenticationService;
        this.clientService = clientService;
        this.accountService = accountService;
    }

    @PostMapping("/signin")
    public ResponseEntity<Client> authenticateUser(@RequestParam String userName, @RequestParam String password) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(clientAuthenticationService.getClientInfo(userName, password));
    }

    @PostMapping("/addClient")
    public ResponseEntity<Client> addClient(@RequestBody CreateClientRequest clientRequest) {
        Client client;
        if (clientRequest.getClientType() == ClientType.CORPORATE) {
            client = clientService.createNewCorporateClient(clientRequest);
        } else {
            client = clientService.createNewIndividualClient(clientRequest);
        }
        // Create initial account and set the account number
        Account initialAccount = clientRequest.getAccountType() == AccountType.CHECKING ?
                accountService.createCheckingAccount(client) : accountService.createSavingAccount(client);
        client.setAccounts(Collections.singletonList(initialAccount.getAccountNumber()));
        Client clientResult = clientService.insertClient(client);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(clientResult);
    }
}
