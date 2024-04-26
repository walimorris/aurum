package com.morris.aurum.services;

import com.morris.aurum.models.transactions.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    /**
     * Get most recent transactions. limit: 10.
     *
     * @param accountNumber {@link String} account number for transactions
     * @return {@link List<Transaction>}
     */
    List<Transaction> getMostRecentTransactions(String accountNumber);

    /**
     * Get most recent deposits on an account.
     *
     * @param accountNumber {@link String} account number for deposits
     * @return {@link List<Transaction>}
     */
    List<Transaction> getMostRecentDeposits(String accountNumber);

    /**
     * Get most recent withdraws on an account.
     *
     * @param accountNumber {@link String} account number for withdraws
     * @return {@link List<Transaction>}
     */
    List<Transaction> getMostRecentWithdraws(String accountNumber);

    /**
     * Get most recent transfers on an account.
     *
     * @param accountNumber {@link String} account number for transfers
     * @return {@link List<Transaction>}
     */
    List<Transaction> getMostRecentTransfers(String accountNumber);

    /**
     * Create a deposit transaction.
     *
     * @param toAccount {@link String} account deposited to
     * @param depositAmount {@link BigDecimal} amount of deposit
     *
     * @return {@link Transaction} the completed transaction
     */
    Transaction createDeposit(String toAccount, BigDecimal depositAmount);
}
