package com.morris.aurum.models.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.bson.BsonDateTime;

import java.io.IOException;

public class BsonDateTimeDeserializer extends StdDeserializer<BsonDateTime> {
    private static final String DATE = "$date";
    private static final String VALUE = "value";
    private static final String NUMBER_LONG = "$numberLong";
    private static final String INVALID_BSON_VALUE = "Invalid BsonDateTime value: ";

    public BsonDateTimeDeserializer() {
        this(null);
    }

    protected BsonDateTimeDeserializer(Class<?> vc) {
        super(vc);
    }

    /**
     * Check if the JSON node contains the nested $date field.
     * If the $date field exists, extract the timestamp from the nested $numberLong field.
     * Parse the timestamp as a long and create a BsonDateTime object with that value.
     *
     * @param p {@link JsonParser}
     * @param ctxt {@link DeserializationContext}
     *
     * @return {@link BsonDateTime}
     */
    @Override
    public BsonDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        if (node != null) {
            String timestampString = null;
            if (node.has(DATE)) {
                JsonNode dateNode = node.get(DATE);
                if (dateNode != null && dateNode.has(NUMBER_LONG)) {
                    timestampString = dateNode.get(NUMBER_LONG).asText();
                }
            } else {
                if (node.has(VALUE)) {
                    timestampString = node.get(VALUE).asText();
                }
            }
            if (timestampString != null) {
                try {
                    long timestamp = Long.parseLong(timestampString);
                    return new BsonDateTime(timestamp);
                } catch (NumberFormatException e) {
                    throw new IOException(INVALID_BSON_VALUE + timestampString, e);
                }
            }
        }
        throw new IOException(INVALID_BSON_VALUE + node);
    }
}
