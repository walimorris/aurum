package com.morris.aurum.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.morris.aurum.models.accounts.Account;
import com.morris.aurum.models.accounts.CheckingAccount;
import com.morris.aurum.models.accounts.SavingAccount;
import com.morris.aurum.models.clients.CorporateClient;
import com.morris.aurum.models.clients.IndividualClient;
import com.morris.aurum.services.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aurum/api/accounts")
public class AccountController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/individual/createCheckingAccount")
    public ResponseEntity<CheckingAccount> createIndividualCheckingAccount(@RequestBody IndividualClient client) {
        CheckingAccount createdAccountResult = accountService.createCheckingAccount(client);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdAccountResult);
    }

    @PostMapping("/individual/createSavingAccount")
    public ResponseEntity<SavingAccount> createIndividualSavingAccount(@RequestBody IndividualClient client) {
        SavingAccount createdAccountResult = accountService.createSavingAccount(client);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdAccountResult);
    }

    @PostMapping("/corporation/createCheckingAccount")
    public ResponseEntity<Account> createCorporateCheckingAccount(@RequestBody CorporateClient client) {
        Account createdAccountResult = accountService.createCheckingAccount(client);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdAccountResult);
    }

    @PostMapping("/corporation/createSavingAccount")
    public ResponseEntity<Account> createCorporateSavingAccount(@RequestBody CorporateClient client) {
        Account createdAccountResult = accountService.createSavingAccount(client);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdAccountResult);
    }

    @PostMapping("/deleteAccount")
    public ResponseEntity<Boolean> deleteAccount(@RequestParam String clientId, @RequestParam String accountNumber) {
        boolean deletedAccountResult = accountService.deleteAccount(clientId, accountNumber);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(deletedAccountResult);
    }

    @GetMapping("/getCheckingAccount")
    public ResponseEntity<Account> getAccount(@RequestParam String accountNumber) {
        Account accountResult = accountService.getAccount(accountNumber);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(accountResult);
    }

    @GetMapping("/getAllAccounts")
    public ResponseEntity<List<Account>> getAllAccounts(@RequestParam String clientId) throws JsonProcessingException {
        List<Account> accounts = accountService.getAllAccountsForClient(clientId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(accounts);
    }
}
