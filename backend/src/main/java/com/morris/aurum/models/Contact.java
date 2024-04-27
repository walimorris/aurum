package com.morris.aurum.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    String countryCode;
    String areaCode;
    String primaryNumber;
}
