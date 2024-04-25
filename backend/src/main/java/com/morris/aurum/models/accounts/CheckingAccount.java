package com.morris.aurum.models.accounts;

import com.morris.aurum.models.cards.DebitCard;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CheckingAccount extends Account {
    List<DebitCard> debitCards;
}
