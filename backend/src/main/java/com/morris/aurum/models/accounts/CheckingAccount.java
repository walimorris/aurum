package com.morris.aurum.models.accounts;

import com.morris.aurum.models.cards.DebitCard;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class CheckingAccount extends Account {
    List<DebitCard> debitCards;
}
