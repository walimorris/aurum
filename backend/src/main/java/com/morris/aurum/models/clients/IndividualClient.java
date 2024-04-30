package com.morris.aurum.models.clients;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class IndividualClient extends Client {
    private String firstName;
    private String lastName;
    private String dateOfBirth;

    @Override
    @JsonSerialize(using = ToStringSerializer.class)
    public ObjectId getId() {
        return super.getId();
    }
}
