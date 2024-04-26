package com.morris.aurum.models.accounts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.morris.aurum.models.transactions.Transaction;
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

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Document("accounts")
@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * Accounts Schema uses the subset pattern - in the subset pattern we are storing the
 * 10 most recent transactions in the account. Older transactions will
 * be stored in the transactions' collection. In a real situation, a bank account may
 * need to store more than 10 transactions. In general, the subset pattern will improve
 * performance, given that recent transactions should be immediately available to clients
 * when they open their account information. With a subset of transactions embedded in
 * Accounts, we can gain a performance enhancement.
 */
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
    private List<Transaction> transactions;

    @JsonIgnore
    private BsonDateTime creationDate;
}
