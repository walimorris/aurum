package com.morris.aurum.repositories;

import com.morris.aurum.models.accounts.Account;
import com.morris.aurum.models.transactions.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

    @Query("{accountNumber:  '?0'}")
    Account findByAccountNumber(String accountNumber);

    @Query("{accountNumber:  '?0'}")
    Account deleteAccountByAccountNumber(String accountNumber);

    @Query(value = "{accountNumber:  '?0'}", fields = "{transactions:  1, _id:  0}")
    List<Transaction> findAllTransactionsByAccountNumber(String accountNumber);
}
