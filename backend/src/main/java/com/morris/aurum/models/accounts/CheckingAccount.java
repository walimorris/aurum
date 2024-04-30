package com.morris.aurum.models.accounts;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.morris.aurum.models.cards.DebitCard;
import com.morris.aurum.models.serializers.BsonDateTimeDeserializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.BsonDateTime;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CheckingAccount extends Account {
    List<DebitCard> debitCards;

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
