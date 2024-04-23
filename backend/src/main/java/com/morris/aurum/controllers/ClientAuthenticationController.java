package com.morris.aurum.controllers;

import com.morris.aurum.models.clients.Client;
import com.morris.aurum.models.Contact;
import com.morris.aurum.models.clients.CorporateClient;
import com.morris.aurum.models.requests.CorporateClientRequest;
import com.morris.aurum.services.ClientAuthenticationService;
import com.morris.aurum.services.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/aurum/api/auth")
public class ClientAuthenticationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientAuthenticationController.class);
    private final ClientAuthenticationService clientAuthenticationService;
    private final ClientService clientService;

    @Autowired
    public ClientAuthenticationController(ClientAuthenticationService clientAuthenticationService,
                                          ClientService clientService) {

        this.clientAuthenticationService = clientAuthenticationService;
        this.clientService = clientService;
    }

    @PostMapping("/signin")
    public ResponseEntity<Client> authenticateUser(@RequestParam String userName, @RequestParam String password) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(clientAuthenticationService.getClientInfo(userName, password));
    }

    @PostMapping("/addBusinessClient")
    public ResponseEntity<Client> addClient(CorporateClientRequest corporateClientRequest) {
        String clientHashId = generateHashId(corporateClientRequest);
        List<Contact> contacts = Collections.singletonList(corporateClientRequest.getContact());

        // build the CorporateClient from requests
        CorporateClient corporateClient = CorporateClient.builder()
                .userName(corporateClientRequest.getUserName())
                .password(corporateClientRequest.getPassword()) // TODO: field level encryption
                .clientId(clientHashId)
                .emailAddress(corporateClientRequest.getEmailAddress())
                .address(corporateClientRequest.getAddress())
                .contacts(contacts)
                .clientType("Corporation")
                .businessName(corporateClientRequest.getBusinessName())
                .entityType(corporateClientRequest.getEntityType())
                .ein(corporateClientRequest.getEin())
                .build();

        Client clientResult = clientService.insertClient(corporateClient);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(clientResult);
    }

    private String generateHashId(CorporateClientRequest clientRequest) {
        return String.valueOf(clientRequest.getEin().hashCode());
    }
}
