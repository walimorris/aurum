package com.morris.aurum.models.accounts;

import com.morris.aurum.models.types.AccountType;
import com.morris.aurum.models.types.ActiveType;
import com.morris.aurum.models.types.CurrencyType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.BsonDateTime;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@SuperBuilder
public class Account {

    @Id
    @BsonId
    private ObjectId id;
    private CurrencyType currencyType;
    private BigDecimal balance;
    private AccountType accountType;
    private String accountNumber;
    private String routingNumber;
    private ActiveType activeType;
    private List<String> transactions;
    private BsonDateTime creationDate;
}
