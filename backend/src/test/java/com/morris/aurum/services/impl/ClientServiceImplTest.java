package com.morris.aurum.services.impl;

import com.morris.aurum.TestHelper;
import com.morris.aurum.models.Address;
import com.morris.aurum.models.Contact;
import com.morris.aurum.models.clients.Client;
import com.morris.aurum.models.clients.IndividualClient;
import com.morris.aurum.models.requests.CreateClientRequest;
import com.morris.aurum.models.types.AccountType;
import com.morris.aurum.models.types.ActiveType;
import com.morris.aurum.models.types.ClientType;
import com.morris.aurum.models.types.CountryCode;
import com.morris.aurum.utils.BankingUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = {"coverage"})
class ClientServiceImplTest {
    private static final String CLIENT_REQUEST_1_JSON = "backend/src/test/java/resources/clients/client_request_1.json";
    private static final String CLIENT_REQUEST_1_RESULT_JSON = "backend/src/test/java/resources/clients/client_request_1_result.json";

    @Test
    void createNewCorporateClient() throws IOException, JSONException {

    }

    @Test
    void createNewIndividualClient() throws IOException {
        Contact contact = Contact.builder()
                .countryCode("+1")
                .areaCode("206")
                .primaryNumber("5151")
                .build();

        Address address = Address.builder()
                .country("United States of America")
                .primaryNumber("651")
                .countryCode(CountryCode.USA)
                .state("California")
                .city("Los Angeles")
                .streetSuffix("BLVD")
                .streetName("Roosevelt")
                .zipcode("66543")
                .build();

        CreateClientRequest clientRequest = CreateClientRequest.builder()
                .clientType(ClientType.INDIVIDUAL)
                .contact(contact)
                .accountType(AccountType.CHECKING)
                .address(address)
                .emailAddress("rsavage@gmail.com")
                .firstName("Randy")
                .lastName("Savage")
                .dateOfBirth("03/25/1965")
                .userName("rsavage1")
                .password("**********")
                .build();

        BankingUtil mockBankingUtil = Mockito.mock(BankingUtil.class);
        when(mockBankingUtil.generateHashId(clientRequest.getUserName())).thenReturn("31796311");
        String clientHashId = mockBankingUtil.generateHashId(clientRequest.getUserName());
        List<Contact> contacts = Collections.singletonList(clientRequest.getContact());

        IndividualClient individualClient = IndividualClient.builder()
                .userName(clientRequest.getUserName())
                .password(clientRequest.getPassword())
                .clientId(clientHashId)
                .emailAddress(clientRequest.getEmailAddress())
                .address(clientRequest.getAddress())
                .contacts(contacts)
                .clientType(clientRequest.getClientType())
                .activeType(ActiveType.ACTIVE)
                .firstName(clientRequest.getFirstName())
                .lastName(clientRequest.getLastName())
                .dateOfBirth(clientRequest.getDateOfBirth())
                .build();

        IndividualClient clientResultFromRequestJson = (IndividualClient) TestHelper.convertModelFromFile(CLIENT_REQUEST_1_RESULT_JSON,
                IndividualClient.class, null);
        String individualClientResultFromRequestTestString = TestHelper.writeValueAsString(individualClient);
        String clientResultFromRequestModelString = TestHelper.writeValueAsString(clientResultFromRequestJson);
        Assertions.assertEquals(clientResultFromRequestModelString, individualClientResultFromRequestTestString);
    }
}