package com.morris.aurum.models.clients;

import com.morris.aurum.models.Address;
import com.morris.aurum.models.Contact;
import com.morris.aurum.models.types.ClientType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@Document("clients")
public class Client {

    @Id
    @BsonId
    private ObjectId id;
    private String userName;
    private String password;
    private String clientId;
    private String emailAddress;
    private Address address;
    private List<Contact> contacts;
    private List<String> accounts;
    private ClientType clientType;

    public Client() {}
}
