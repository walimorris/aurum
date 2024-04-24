package com.morris.aurum.models.clients;

import com.morris.aurum.models.clients.Client;
import com.morris.aurum.models.types.CorporateEntityType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class CorporateClient extends Client {
    private String businessName;
    private CorporateEntityType corporateEntityType;
    private String ein;
}
