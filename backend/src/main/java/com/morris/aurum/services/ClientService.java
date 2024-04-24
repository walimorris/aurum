package com.morris.aurum.services;

import com.morris.aurum.models.clients.Client;
import com.morris.aurum.models.requests.CreateClientRequest;

public interface ClientService {

    /**
     * Insert new client.
     *
     * @param client {@link Client}
     * @return {@link Client}
     */
    Client insertClient(Client client);

    /**
     * Create new Corporate Client {@link com.morris.aurum.models.clients.CorporateClient}
     * @param clientRequest {@link CreateClientRequest}
     *
     * @return {@link Client}
     */
    Client createNewCorporateClient(CreateClientRequest clientRequest);

    /**
     * Create new Individual Client {@link com.morris.aurum.models.clients.IndividualClient}
     * @param clientRequest {@link CreateClientRequest}
     *
     * @return {@link Client}
     */
    Client createNewIndividualClient(CreateClientRequest clientRequest);
}
