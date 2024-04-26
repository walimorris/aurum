package com.morris.aurum.models.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.codecs.pojo.annotations.BsonId;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
public class DepositTransaction extends Transaction {

    @Id
    @BsonId
    @JsonIgnore
    private BigDecimal depositAmount;
    private String toAccount;
    private BigDecimal endingBalance;
}
