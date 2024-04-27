package com.morris.aurum.models;

import com.morris.aurum.models.types.CountryCode;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    String primaryNumber;
    String streetName;
    String streetSuffix;
    String country;
    CountryCode countryCode;
    String city;
    String state;
    String zipcode;
}
