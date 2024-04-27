package com.morris.aurum.models.requests;

import com.morris.aurum.models.Address;
import com.morris.aurum.models.Contact;
import com.morris.aurum.models.types.AccountType;
import com.morris.aurum.models.types.ClientType;
import com.morris.aurum.models.types.CorporateEntityType;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateClientRequest {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String businessName;
    private CorporateEntityType corporateEntityType;
    private String ein;
    private String userName;
    private String password;
    private String emailAddress;
    private Address address;
    private Contact contact;
    private ClientType clientType;
    private AccountType accountType;
}
