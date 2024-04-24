package com.morris.aurum.models.accounts;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
public class SavingAccount extends Account {
    BigDecimal interestRate;
}
