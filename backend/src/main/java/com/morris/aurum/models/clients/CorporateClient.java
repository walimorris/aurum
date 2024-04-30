package com.morris.aurum.models.clients;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.morris.aurum.models.types.CorporateEntityType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CorporateClient extends Client {
    private String businessName;
    private CorporateEntityType corporateEntityType;
    private String ein;

    @Override
    @JsonSerialize(using = ToStringSerializer.class)
    public ObjectId getId() {
        return super.getId();
    }
}
