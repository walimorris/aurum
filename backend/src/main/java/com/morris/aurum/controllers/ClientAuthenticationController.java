package com.morris.aurum.controllers;

import com.morris.aurum.models.Client;
import com.morris.aurum.services.ClientAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aurum/api/auth")
public class ClientAuthenticationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientAuthenticationController.class);
    private final ClientAuthenticationService clientAuthenticationService;

    @Autowired
    public ClientAuthenticationController(ClientAuthenticationService clientAuthenticationService) {
        this.clientAuthenticationService = clientAuthenticationService;
    }

    @PostMapping("/signin")
    public ResponseEntity<Client> authenticateUser(@RequestParam String userName, @RequestParam String password) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(clientAuthenticationService.getClientInfo(userName, password));
    }
}
