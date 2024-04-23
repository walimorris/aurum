package com.morris.aurum.models.clients;

import com.morris.aurum.models.clients.Client;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class CorporateClient extends Client {
    private String businessName;
    private String entityType;
    private String ein;

    public CorporateClient() {}
}
