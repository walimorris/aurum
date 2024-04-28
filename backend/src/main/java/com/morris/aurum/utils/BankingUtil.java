package com.morris.aurum.utils;

import org.bson.BsonDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component("bankingUtil")
public class BankingUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(BankingUtil.class);

    private static final int HASH = 17;
    private static final int BITS = 0xfffffff;

    /**
     * The key passed to Hash Function should be unique and immutable. If, for say, an object
     * changes it will also change the value of the hash (in this case used for clientIds).
     * EINs are unique identifiers and, as such, are safe to use. This function creates a Hash
     * calculation and passes in unique values to create Ids for Corporate and Individual client.
     *
     * @param v - unique value
     *
     * @return {@link String} Hash
     */
    public String generateHashId(String v) {
        // TODO: Implement org.apache.commons HashCodeBuilder
        return String.valueOf(HASH * 31 + v.hashCode() & BITS);
    }

    public BsonDateTime now() {
        return new BsonDateTime(new Date().getTime());
    }
}
