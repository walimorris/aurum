package com.morris.aurum.services;

import com.morris.aurum.models.clients.Client;

public interface ClientAuthenticationService {

    /**
     * Retrieve ClientInfo from username/password.
     *
     * @param userName client username
     * @param password client password
     *
     * @return {@link Client}
     */
    Client getClientInfo(String userName, String password);
}
