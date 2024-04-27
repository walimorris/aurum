package com.morris.aurum.services.impl;

import com.morris.aurum.TestHelper;
import com.morris.aurum.models.Address;
import com.morris.aurum.models.Contact;
import com.morris.aurum.models.clients.Client;
import com.morris.aurum.models.clients.CorporateClient;
import com.morris.aurum.models.clients.IndividualClient;
import com.morris.aurum.models.requests.CreateClientRequest;
import com.morris.aurum.models.types.*;
import com.morris.aurum.repositories.ClientRepository;
import com.morris.aurum.utils.BankingUtil;
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
    @Mock
    private static BankingUtil mockBankingUtil;

    @Mock
    private static ClientRepository mockClientRepository;

    private static Contact contact;
    private static Address address;
    private static CreateClientRequest clientRequest;
    private static final String INDIVIDUAL_CLIENT_REQUEST_1_JSON = "backend/src/test/java/resources/clients/individual_client_request_1.json";
    private static final String INDIVIDUAL_CLIENT_REQUEST_1_RESULT_JSON = "backend/src/test/java/resources/clients/individual_client_request_1_result.json";
    private static final String CORPORATE_CLIENT_REQUEST_1_RESULT_JSON = "backend/src/test/java/resources/clients/corporate_client_request_1_result.json";

    @BeforeAll
    static void setUp() {
        mockBankingUtil = Mockito.mock(BankingUtil.class);
        mockClientRepository = Mockito.mock(ClientRepository.class);

        contact = Contact.builder()
                .countryCode("+1")
                .areaCode("206")
                .primaryNumber("5151")
                .build();

        address = Address.builder()
                .country("United States of America")
                .primaryNumber("651")
                .countryCode(CountryCode.USA)
                .state("California")
                .city("Los Angeles")
                .streetSuffix("BLVD")
                .streetName("Roosevelt")
                .zipcode("66543")
                .build();

        clientRequest = CreateClientRequest.builder()
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
    }

    @Test
    void createNewCorporateClient() throws IOException {
        CreateClientRequest clientRequest = CreateClientRequest.builder()
                .ein("987654321")
                .businessName("Eden Works")
                .corporateEntityType(CorporateEntityType.CORPORATION)
                .clientType(ClientType.CORPORATE)
                .contact(contact)
                .accountType(AccountType.CHECKING)
                .address(address)
                .emailAddress("edenworks@gmail.com")
                .userName("edenworks")
                .password("**********")
                .build();

        when(mockBankingUtil.generateHashId(clientRequest.getEin())).thenReturn("111111111");
        String clientHashId = mockBankingUtil.generateHashId(clientRequest.getEin());
        List<Contact> contacts = Collections.singletonList(clientRequest.getContact());

        CorporateClient corporateClient = CorporateClient.builder()
                .userName(clientRequest.getUserName())
                .password(clientRequest.getPassword())
                .clientId(clientHashId)
                .emailAddress(clientRequest.getEmailAddress())
                .address(clientRequest.getAddress())
                .contacts(contacts)
                .clientType(clientRequest.getClientType())
                .activeType(ActiveType.ACTIVE)
                .businessName(clientRequest.getBusinessName())
                .corporateEntityType(clientRequest.getCorporateEntityType())
                .ein(clientRequest.getEin())
                .build();

        CorporateClient clientResultFromRequestJson = (CorporateClient) TestHelper.convertModelFromFile(CORPORATE_CLIENT_REQUEST_1_RESULT_JSON,
                CorporateClient.class, null);
        String corporateClientResultFromRequestTestString = TestHelper.writeValueAsString(corporateClient);
        String clientResultFromRequestModelString = TestHelper.writeValueAsString(clientResultFromRequestJson);
        Assertions.assertEquals(clientResultFromRequestModelString, corporateClientResultFromRequestTestString);
    }

    @Test
    void createNewIndividualClient() throws IOException {
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

        IndividualClient clientResultFromRequestJson = (IndividualClient) TestHelper.convertModelFromFile(INDIVIDUAL_CLIENT_REQUEST_1_RESULT_JSON,
                IndividualClient.class, null);
        String individualClientResultFromRequestTestString = TestHelper.writeValueAsString(individualClient);
        String clientResultFromRequestModelString = TestHelper.writeValueAsString(clientResultFromRequestJson);
        Assertions.assertEquals(clientResultFromRequestModelString, individualClientResultFromRequestTestString);
    }

    @Test
    void insertClient() {
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

        when(mockClientRepository.insert(individualClient)).thenReturn(individualClient);
        Client resultClient = mockClientRepository.insert(individualClient);

        assertAll(
                () -> assertEquals(resultClient.getClientId(), individualClient.getClientId()),
                () -> assertEquals(resultClient.getClientType(), individualClient.getClientType()),
                () -> assertEquals(resultClient.getPassword(), individualClient.getPassword()),
                () -> assertEquals(resultClient.getAddress(), individualClient.getAddress()),
                () -> assertNull(individualClient.getAccounts()),
                () -> assertEquals(resultClient.getActiveType(), individualClient.getActiveType()),
                () -> assertEquals(resultClient.getContacts().size(), individualClient.getContacts().size()),
                () -> assertEquals(resultClient.getUserName(), individualClient.getUserName()),
                () -> assertEquals("Randy", individualClient.getFirstName()),
                () -> assertEquals("Savage", individualClient.getLastName()),
                () -> assertEquals("03/25/1965", individualClient.getDateOfBirth())
        );
    }
}