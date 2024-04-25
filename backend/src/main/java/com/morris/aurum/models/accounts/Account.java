package com.morris.aurum.models.accounts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.morris.aurum.models.types.AccountType;
import com.morris.aurum.models.types.ActiveType;
import com.morris.aurum.models.types.CurrencyType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.BsonDateTime;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Document("accounts")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

    @Id
    @BsonId
    @JsonIgnore
    private ObjectId id;
    private CurrencyType currencyType;
    private BigDecimal balance;
    private AccountType accountType;
    private String accountNumber;
    private String routingNumber;
    private ActiveType activeType;
    private List<String> transactions;

    @JsonIgnore
    private BsonDateTime creationDate;
}
