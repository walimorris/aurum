package com.morris.aurum.models.clients;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class IndividualClient extends Client {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
}
