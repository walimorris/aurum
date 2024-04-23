package com.morris.aurum.services.impl;

import com.morris.aurum.models.clients.Client;
import com.morris.aurum.repositories.ClientRepository;
import com.morris.aurum.services.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client insertClient(@NonNull Client client) {
        return clientRepository.insert(client);
    }
}
