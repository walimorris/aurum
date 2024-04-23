package com.morris.aurum.controllers;

import com.morris.aurum.models.clients.Client;
import com.morris.aurum.models.Contact;
import com.morris.aurum.models.clients.CorporateClient;
import com.morris.aurum.models.clients.IndividualClient;
import com.morris.aurum.models.requests.ClientRequest;
import com.morris.aurum.models.types.ClientType;
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

    private static final int HASH = 17;

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
    public ResponseEntity<Client> addClient(ClientRequest clientRequest) {
        String clientHashId = generateHashId(clientRequest);
        List<Contact> contacts = Collections.singletonList(clientRequest.getContact());

        Client client;

        if (clientRequest.getClientType() == ClientType.CORPORATE) {
            // build the CorporateClient from requests
            client = CorporateClient.builder()
                    .userName(clientRequest.getUserName())
                    .password(clientRequest.getPassword()) // TODO: field level encryption
                    .clientId(clientHashId)
                    .emailAddress(clientRequest.getEmailAddress())
                    .address(clientRequest.getAddress())
                    .contacts(contacts)
                    .clientType(clientRequest.getClientType())
                    .businessName(clientRequest.getBusinessName())
                    .corporateEntityType(clientRequest.getCorporateEntityType())
                    .ein(clientRequest.getEin())
                    .build();
        } else {
            // build IndividualClient
            client = IndividualClient.builder()
                    .userName(clientRequest.getUserName())
                    .password(clientRequest.getPassword()) // TODO: field level encryption
                    .clientId(clientHashId)
                    .emailAddress(clientRequest.getEmailAddress())
                    .address(clientRequest.getAddress())
                    .contacts(contacts)
                    .clientType(clientRequest.getClientType())
                    .firstName(clientRequest.getFirstName())
                    .lastName(clientRequest.getLastName())
                    .dateOfBirth(clientRequest.getDateOfBirth())
                    .build();
        }
        Client clientResult = clientService.insertClient(client);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(clientResult);
    }

    /**
     * The key passed to Hash Function should be unique and immutable. If, for say, an object
     * changes it will also change the value of the hash (in this case used for clientIds). EINs
     * are unique identifiers and as such is safe to use. What about for individuals? What's a
     * unique key we can use here and how do we ALWAYS return positive Hash values?
     *
     * @param clientRequest {@link ClientRequest}
     *
     * @return {@link String} Hash
     */
    private String generateHashId(ClientRequest clientRequest) {
        // TODO: what if ClientRequest.getEin() or ClientRequest.getUserName() is Null?
        if (clientRequest.getClientType() == ClientType.CORPORATE) {
            return String.valueOf(HASH * 31 + clientRequest.getEin().hashCode());
        }
        return String.valueOf(HASH * 31 + clientRequest.getUserName().hashCode());
    }
}
