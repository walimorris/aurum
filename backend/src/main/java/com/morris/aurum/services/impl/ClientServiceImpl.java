package com.morris.aurum.services.impl;

import com.morris.aurum.models.Contact;
import com.morris.aurum.models.clients.Client;
import com.morris.aurum.models.clients.CorporateClient;
import com.morris.aurum.models.clients.IndividualClient;
import com.morris.aurum.models.requests.CreateClientRequest;
import com.morris.aurum.models.types.ActiveType;
import com.morris.aurum.repositories.ClientRepository;
import com.morris.aurum.services.ClientService;
import com.morris.aurum.utils.BankingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;
    private final BankingUtil bankingUtil;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, BankingUtil bankingUtil) {
        this.clientRepository = clientRepository;
        this.bankingUtil = bankingUtil;
    }

    @Override
    public Client createNewCorporateClient(CreateClientRequest clientRequest) {
        String clientHashId = bankingUtil.generateHashId(clientRequest.getEin());
        List<Contact> contacts = Collections.singletonList(clientRequest.getContact());

        return CorporateClient.builder()
                .userName(clientRequest.getUserName())
                .password(clientRequest.getPassword()) // TODO: field level encryption
                .clientId(clientHashId)
                .emailAddress(clientRequest.getEmailAddress())
                .address(clientRequest.getAddress())
                .contacts(contacts)
                .clientType(clientRequest.getClientType())
                .activeType(ActiveType.ACTIVE)
                .businessName(clientRequest.getBusinessName())
                .corporateEntityType(clientRequest.getCorporateEntityType())
                .ein(clientRequest.getEin())
                .build();
    }

    @Override
    public Client createNewIndividualClient(CreateClientRequest clientRequest) {
        String clientHashId = bankingUtil.generateHashId(clientRequest.getUserName());
        List<Contact> contacts = Collections.singletonList(clientRequest.getContact());

        return IndividualClient.builder()
                .userName(clientRequest.getUserName())
                .password(clientRequest.getPassword()) // TODO: field level encryption
                .clientId(clientHashId)
                .emailAddress(clientRequest.getEmailAddress())
                .address(clientRequest.getAddress())
                .contacts(contacts)
                .clientType(clientRequest.getClientType())
                .activeType(ActiveType.INACTIVE)
                .firstName(clientRequest.getFirstName())
                .lastName(clientRequest.getLastName())
                .dateOfBirth(clientRequest.getDateOfBirth())
                .build();
    }

    @Override
    public Client insertClient(Client client) {
        return clientRepository.insert(client);
    }
}
