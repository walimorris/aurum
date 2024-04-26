package com.morris.aurum.models.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.morris.aurum.models.types.CurrencyType;
import com.morris.aurum.models.types.TransactionType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.BsonDateTime;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@SuperBuilder
public class Transaction {

    @Id
    @BsonId
    @JsonIgnore
    private ObjectId id;
    private String transactionId;
    private CurrencyType currencyType;
    private BsonDateTime creationDate;
    private TransactionType transactionType;
}
