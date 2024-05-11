package com.morris.aurum.models.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.morris.aurum.models.serializers.BsonDateTimeDeserializer;
import com.morris.aurum.models.types.CurrencyType;
import com.morris.aurum.models.types.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.BsonDateTime;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Document("transactions")
public class Transaction {

    @Id
    @JsonProperty("_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String transactionId;
    private CurrencyType currencyType;

    @JsonDeserialize(using = BsonDateTimeDeserializer.class)
    private BsonDateTime creationDate;
    private TransactionType transactionType;
}
