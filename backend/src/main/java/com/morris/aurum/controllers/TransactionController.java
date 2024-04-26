package com.morris.aurum.controllers;

import com.morris.aurum.models.accounts.Account;
import com.morris.aurum.models.clients.Client;
import com.morris.aurum.models.transactions.Transaction;
import com.morris.aurum.services.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/aurum/api/accounts")
public class TransactionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/allTransactionsForAccount")
    public ResponseEntity<List<Transaction>> getAllAccountTransactions(@RequestParam Account accountId) {
        return null;
    }

    @GetMapping("/recentDepositsForAccount")
    public ResponseEntity<List<Transaction>> getRecentDepositTransactions(@RequestParam Account accountId) {
        return null;
    }

    @GetMapping("/recentWithdrawsForAccount")
    public ResponseEntity<List<Transaction>> getRecentWithdrawTransactions(@RequestParam Account accountId) {
        return null;
    }

    @GetMapping("/recentTransfersForAccount")
    public ResponseEntity<List<Transaction>> getRecentTransferTransactions(@RequestParam Account accountId) {
        return null;
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
    public ResponseEntity<Account> deposit(@RequestParam Account toAccount, @RequestParam BigDecimal amount) {
        return null;
    }

    @PostMapping("/transfer")
    public ResponseEntity<List<Account>> transfer(@RequestParam Account fromAccount, @RequestParam Account toAccount, @RequestParam BigDecimal amount) {
        return null;
    }
}
