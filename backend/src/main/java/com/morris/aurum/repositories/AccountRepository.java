package com.morris.aurum.repositories;

import com.mongodb.lang.NonNull;
import com.morris.aurum.models.accounts.Account;
import com.morris.aurum.models.accounts.CheckingAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

    @Query("{accountNumber:  '?0'}")
    Account findByAccountNumber(String accountNumber);

    @Query("{accountNumber:  '?0'}")
    CheckingAccount findByCheckingAccountNumber(String accountNumber);

    @Query("{accountNumber:  '?0'}")
    CheckingAccount findBySavingAccountNumber(String accountNumber);

    @Query("{accountNumber:  '?0'}")
    long deleteAccountByAccountNumber(String accountNumber);

    @Query(value = "{accountNumber:  '?0'}", fields = "{transactions:  1, _id:  0}")
    Account findAllRecentTransactionsByAccountNumber(String accountNumber);

    @NonNull
    @Override
    <S extends Account> S save(@NonNull S entity);
}
