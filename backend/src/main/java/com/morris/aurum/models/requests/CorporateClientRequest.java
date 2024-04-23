package com.morris.aurum.models.requests;

import com.morris.aurum.models.Address;
import com.morris.aurum.models.Contact;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CorporateClientRequest {
    private String businessName;
    private String entityType;
    private String ein;
    private String userName;
    private String password;
    private String emailAddress;
    private Address address;
    private Contact contact;
}
