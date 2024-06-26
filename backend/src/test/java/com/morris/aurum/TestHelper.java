package com.morris.aurum;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.morris.aurum.models.accounts.Account;
import com.morris.aurum.models.serializers.BsonDateTimeDeserializer;
import com.morris.aurum.models.transactions.Transaction;
import org.bson.BsonDateTime;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * The TestHelper class exposes various methods used to help in Unit Tests. Some methods include: converting
 * objects, pulling objects from the file system, String methods, parsing JSON files, and reading files.
 */
public class TestHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestHelper.class);
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private TestHelper() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(ToStringSerializer.class, new ToStringSerializer());

        SimpleModule simpleModule2 = new SimpleModule();
        simpleModule2.addDeserializer(BsonDateTime.class, new BsonDateTimeDeserializer());

        objectMapper.registerModules(simpleModule, simpleModule2);
    }

    /**
     * More complicated objects such as objects that are based on a List or Map collection
     * require more class and type checks in order to convert them to its reference type for
     * the object mapper that will convert them. Note: such objects will need to be type cast
     * from the calling client.
     * <br><br>
     * <pre>{@code
     * List<Result> resultsList = (List<Result>) TestHelper.convertModelFromFile(BACK_LINKS_RESULT_JSON, List.class, Result.class);
     * }
     * </pre>
     *
     * @param fileName file path to object json
     * @param clazz the collection class
     * @param type the object type in the collection
     *
     * @return {@link Object}
     *
     * @throws IOException exception
     */
    public static Object convertModelFromFile(String fileName, Class<?> clazz, Class<?> type) throws IOException {
        if (clazz.isAssignableFrom(List.class)) {
            if (type == Account.class) {
                TypeReference<List<Account>> reference = new TypeReference<>() {};
                return objectMapper.readValue(new File(fileName), reference);
            }
            if (type == Transaction.class) {
                TypeReference<List<Transaction>> reference = new TypeReference<>() {};
                return objectMapper.readValue(new File(fileName), reference);
            }
        }
        return objectMapper.readValue(new File(fileName), clazz);
    }

    /**
     * Converts an object located in a file (in json format) to it's Java Object type.
     * Note: This requires a cast from the calling client.
     *
     * @param fileName filepath to object json
     * @param clazz class
     *
     * @return T
     *
     * @throws IOException exception
     */
    public static <T> T convertModelFromFile(String fileName, Class<T> clazz) throws IOException {
        return objectMapper.readValue(new File(fileName), clazz);
    }

    /**
     * Writes object value to json using {@link ObjectMapper}.
     *
     * @param object {@link Object}
     *
     * @return {@link JSONObject}
     */
    public static JSONObject getJSONFromObject(Object object) {
        return objectMapper.convertValue(object, JSONObject.class);
    }

    /**
     * Writes object value to string using {@link ObjectMapper}.
     *
     * @param object {@link Object}
     *
     * @return {@link String}
     * @throws JsonProcessingException processing error
     */
    public static String writeValueAsString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    /**
     * Get {@link BufferedReader} from file.
     *
     * @param filePath path to file
     *
     * @return {@link BufferedReader}
     */
    public static BufferedReader getBufferedReaderFromTxtFile(String filePath) {
        try {
            return new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    public static String asJSONString(Object object) throws JsonProcessingException {
        if (object != null) {
            return writeValueAsString(object);
        }
        return null;
    }
}
