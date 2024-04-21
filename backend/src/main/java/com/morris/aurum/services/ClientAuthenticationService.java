package com.morris.aurum.services;

import com.morris.aurum.models.Client;

public interface ClientAuthenticationService {

    Client getClientInfo(String userName, String password);
}
