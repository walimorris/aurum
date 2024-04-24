package com.morris.aurum.services;

import com.morris.aurum.models.clients.Client;
import com.morris.aurum.models.requests.CreateClientRequest;

public interface ClientService {
    Client insertClient(Client client);
    Client createNewCorporateClient(CreateClientRequest clientRequest);
    Client createNewIndividualClient(CreateClientRequest clientRequest);
}
