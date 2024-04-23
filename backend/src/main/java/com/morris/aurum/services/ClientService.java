package com.morris.aurum.services;

import com.morris.aurum.models.clients.Client;

public interface ClientService {

    /**
     * Insert/add new client.
     *
     * @param client {@link Client}
     *
     * @return {@link Client}
     */
    Client insertClient(Client client);
}
