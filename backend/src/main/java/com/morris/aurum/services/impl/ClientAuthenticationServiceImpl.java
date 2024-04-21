package com.morris.aurum.services.impl;

import com.morris.aurum.models.Client;
import com.morris.aurum.repositories.ClientRepository;
import com.morris.aurum.services.ClientAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientAuthenticationServiceImpl implements ClientAuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientAuthenticationServiceImpl.class);

    private final ClientRepository clientRepository;

    @Autowired
    public ClientAuthenticationServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client getClientInfo(String userName, String password) {
        Client client = clientRepository.findByUserName(userName);
        if (client != null && client.getPassword().equals(password)) {
            return client;
        }
        return null;
    }
}
