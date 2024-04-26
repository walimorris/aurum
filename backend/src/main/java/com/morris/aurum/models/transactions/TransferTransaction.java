package com.morris.aurum.models.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
public class TransferTransaction extends Transaction {

    @Id
    @BsonId
    @JsonIgnore
    private ObjectId id;
    private BigDecimal transferAmount;
    private String toAccount;
    private String fromAccount;
}
