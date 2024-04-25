package com.morris.aurum.models.clients;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.morris.aurum.models.Address;
import com.morris.aurum.models.Contact;
import com.morris.aurum.models.types.ActiveType;
import com.morris.aurum.models.types.ClientType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Document("clients")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Client {

    @Id
    @BsonId
    @JsonIgnore
    private ObjectId id;
    private String userName;
    private String password;
    private String clientId;
    private String emailAddress;
    private Address address;
    private List<Contact> contacts;
    private List<String> accounts;
    private ClientType clientType;
    private ActiveType activeType;
}
