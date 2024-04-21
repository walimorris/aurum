package com.morris.aurum.repositories;

import com.morris.aurum.models.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends MongoRepository<Client, String> {

    @Query("{userName:  '?0'}")
    Client findByUserName(String userName);

    @Query("{clientId:  '?0'}")
    Client findByClientId(String clientId);

    @Query("{emailAddress:  '?0'}")
    Client findByEmailAddress(String emailAddress);
}
