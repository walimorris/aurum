package com.morris.aurum.controllers;

import com.morris.aurum.models.accounts.Account;
import com.morris.aurum.models.clients.Client;
import com.morris.aurum.services.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/aurum/api/accounts")
public class AccountController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/createCheckingAccount")
    public ResponseEntity<Account> createCheckingAccount(Client client) {
        Account createdAccountResult = accountService.createCheckingAccount(client);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdAccountResult);
    }

    @PostMapping("/createSavingAccount")
    public ResponseEntity<Account> createSavingAccount(Client client) {
        Account createdAccountResult = accountService.createSavingAccount(client);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdAccountResult);
    }

    @PostMapping("/deleteAccount")
    public ResponseEntity<Map<String, Object>> deleteAccount(Client client, String accountNumber) {
        Map<String, Object> deletedAccountResult = accountService.deleteAccount(client, accountNumber);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(deletedAccountResult);
    }
}
