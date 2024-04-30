package com.morris.aurum;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.morris.aurum.models.clients.Client;
import com.morris.aurum.models.clients.IndividualClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class SerializationDeserializationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSerializationDeserialization() throws Exception {
        // Create an instance of IndividualClient
        IndividualClient originalClient = new IndividualClient();
        originalClient.setFirstName("John");
        originalClient.setLastName("Doe");
        originalClient.setDateOfBirth("1990-01-01"); // Assuming date format is "YYYY-MM-DD"

        // Serialize IndividualClient to JSON
        String json = objectMapper.writeValueAsString(originalClient);

        // Deserialize JSON back to IndividualClient
        IndividualClient deserializedClient = objectMapper.readValue(json, IndividualClient.class);

        // Verify that deserializedClient matches originalClient
        assertEquals(originalClient.getFirstName(), deserializedClient.getFirstName());
        assertEquals(originalClient.getLastName(), deserializedClient.getLastName());
        assertEquals(originalClient.getDateOfBirth(), deserializedClient.getDateOfBirth());
    }

    @Test
    public void testSerializationToClient() throws Exception {
        // Create an instance of IndividualClient
        IndividualClient originalClient = new IndividualClient();
        originalClient.setFirstName("John");
        originalClient.setLastName("Doe");
        originalClient.setDateOfBirth("1990-01-01"); // Assuming date format is "YYYY-MM-DD"

        // Serialize IndividualClient to JSON
        String json = objectMapper.writeValueAsString(originalClient);

        // Deserialize JSON to Client (a concrete subclass of Client)
        Client deserializedClient = objectMapper.readValue(json, IndividualClient.class);

        // Verify that deserializedClient is an instance of IndividualClient
        assertEquals(IndividualClient.class, deserializedClient.getClass());

        // Verify that deserializedClient matches originalClient
        IndividualClient individualClient = (IndividualClient) deserializedClient;
        assertEquals(originalClient.getFirstName(), individualClient.getFirstName());
        assertEquals(originalClient.getLastName(), individualClient.getLastName());
        assertEquals(originalClient.getDateOfBirth(), individualClient.getDateOfBirth());
    }
}
