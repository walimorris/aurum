package com.morris.aurum.models.clients;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class IndividualClient extends Client {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
}
