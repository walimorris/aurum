package com.morris.aurum.repositories;

import com.morris.aurum.models.accounts.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

    @Query("{accountNumber:  '?0'}")
    Account findByAccountNumber(String accountNumber);

    @Query("{accountNumber:  '?0'}")
    Account deleteAccountByAccountNumber(String accountNumber);
}
