package com.morris.aurum.controllers;

import com.morris.aurum.models.accounts.Account;
import com.morris.aurum.models.clients.Client;
import com.morris.aurum.models.transactions.Transaction;
import com.morris.aurum.services.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/aurum/api/transactions")
public class TransactionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/allTransactionsForAccount")
    public ResponseEntity<List<Transaction>> getAllAccountTransactions(@RequestParam String accountId) {
        return null;
    }

    @GetMapping("/recentTransactionsForAccount")
    public ResponseEntity<List<Transaction>> getRecentTransactions(@RequestParam String accountId) {
        List<Transaction> recentTransactions = transactionService.getMostRecentTransactions(accountId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(recentTransactions);
    }

    @GetMapping("/recentDepositsForAccount")
    public ResponseEntity<List<Transaction>> getRecentDepositTransactions(@RequestParam String accountId) {
        List<Transaction> recentDeposits = transactionService.getMostRecentDeposits(accountId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(recentDeposits);
    }

    @GetMapping("/recentWithdrawsForAccount")
    public ResponseEntity<List<Transaction>> getRecentWithdrawTransactions(@RequestParam String accountId) {
        List<Transaction> recentWithdraws = transactionService.getMostRecentWithdraws(accountId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(recentWithdraws);
    }

    @GetMapping("/recentTransfersForAccount")
    public ResponseEntity<List<Transaction>> getRecentTransferTransactions(@RequestParam String accountId) {
        List<Transaction> recentTransfers = transactionService.getMostRecentTransfers(accountId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(recentTransfers);
    }

    @GetMapping("/allTransactionsForClient")
    public ResponseEntity<List<Transaction>> getAllAccountTransactionsForClient(@RequestParam Client client) {
        return null;
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Account> withdraw(@RequestParam Account fromAccount, @RequestParam BigDecimal amount) {
        return null;
    }

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestParam String toAccountNumber, @RequestParam BigDecimal amount) {
        Transaction transactionResult = transactionService.createDeposit(toAccountNumber, amount);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(transactionResult);
    }

    @PostMapping("/transfer")
    public ResponseEntity<List<Account>> transfer(@RequestParam Account fromAccount, @RequestParam Account toAccount, @RequestParam BigDecimal amount) {
        return null;
    }
}
