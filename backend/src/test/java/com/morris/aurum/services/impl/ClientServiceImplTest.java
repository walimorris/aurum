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
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = {"coverage"})
class ClientServiceImplTest {

    private final ClientRepository mockClientRepository = Mockito.mock(ClientRepository.class);

    private ClientServiceImpl clientService;

    private static Contact contact;
    private static Address address;
    private static final String INDIVIDUAL_CLIENT_REQUEST_1_JSON = "backend/src/test/java/resources/clients/client_request_1.json";
    private static final String INDIVIDUAL_CLIENT_REQUEST_1_RESULT_JSON = "backend/src/test/java/resources/clients/individual_client_request_1_result.json";
    private static final String CORPORATE_CLIENT_REQUEST_1_RESULT_JSON = "backend/src/test/java/resources/clients/corporate_client_request_1_result.json";

    @BeforeEach
    void setUp() {

        // This is constructor injection and manually providing mock objects. This method has shown to be more
        // reliable than other ways of dependency injection. All tests should be made this way.
        clientService = new ClientServiceImpl(mockClientRepository);

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
    }

    @Test
    void createNewCorporateClient() {
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

        String computedClientHash = "152976708";
        Client corporateClientResult = clientService.createNewCorporateClient(clientRequest);
        assertEquals(computedClientHash, corporateClientResult.getClientId());
        assertEquals("edenworks@gmail.com", corporateClientResult.getEmailAddress());
    }

    @Test
    void createNewIndividualClient() {
        CreateClientRequest clientRequest  = CreateClientRequest.builder()
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

        String computedClientHash = "31796311";
        Client individualClient = clientService.createNewIndividualClient(clientRequest);
        assertEquals(computedClientHash, individualClient.getClientId());
        assertEquals("rsavage@gmail.com", individualClient.getEmailAddress());
    }

    @Test
    void insertClient() throws IOException {
        IndividualClient individualClient = TestHelper.convertModelFromFile(INDIVIDUAL_CLIENT_REQUEST_1_RESULT_JSON, IndividualClient.class);
        when(mockClientRepository.insert(individualClient)).thenReturn(individualClient);
        Client resultClient = clientService.insertClient(individualClient);

        assertAll(
                () -> assertEquals(individualClient.getClientId(), resultClient.getClientId()),
                () -> assertEquals(individualClient.getClientType(), resultClient.getClientType()),
                () -> assertEquals(individualClient.getPassword(), resultClient.getPassword()),
                () -> assertEquals(individualClient.getAddress(), resultClient.getAddress()),
                () -> assertEquals(2, resultClient.getAccounts().size()),
                () -> assertEquals(individualClient.getActiveType(), resultClient.getActiveType()),
                () -> assertEquals(1, resultClient.getContacts().size()),
                () -> assertEquals(individualClient.getUserName(), resultClient.getUserName())
        );
    }
}