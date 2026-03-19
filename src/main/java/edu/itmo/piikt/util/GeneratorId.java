package edu.itmo.piikt.util;

import lombok.experimental.UtilityClass;

import java.util.UUID;

/**
 * Utility class for generating unique identifiers.
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 * @see UUID
 */
@UtilityClass
public class GeneratorId {
    /**
     * The getter returns the ID.
     *
     * @return id
     */
    public static String getId() {
        return UUID.randomUUID().toString();
    }
}
