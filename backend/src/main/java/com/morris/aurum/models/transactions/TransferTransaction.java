package com.morris.aurum.models.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.morris.aurum.models.serializers.BsonDateTimeDeserializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.BsonDateTime;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class TransferTransaction extends Transaction {
    private BigDecimal transferAmount;
    private String toAccount;
    private String fromAccount;

    /**
     * To ensure that id field in the CheckingAccount class is also serialized using the
     * ObjectIdSerializer, we've applied the @JsonSerialize annotation to the id field
     * in the CheckingAccount class as well.
     *
     * @return {@link ObjectId}
     */
    @Override
    @JsonSerialize(using = ToStringSerializer.class)
    public ObjectId getId() {
        return super.getId();
    }

    /**
     * To ensure that the creationDate field in the CheckingAccount class is also serialized using the
     * BsonDateTimeSerializer, we've applied the @JsonSerialize annotation to the creationDate
     * field in the CheckingAccount class as well.
     *
     * @return {@link BsonDateTime}
     */
    @Override
    @JsonDeserialize(using = BsonDateTimeDeserializer.class)
    public BsonDateTime getCreationDate() {
        return super.getCreationDate();
    }
}
