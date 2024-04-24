package com.morris.aurum.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {
    String primaryNumber;
    String streetName;
    String streetSuffix;
    String city;
    String state;
    String zipcode;
}
