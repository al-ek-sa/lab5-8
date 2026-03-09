package edu.itmo.piikt.util;

import java.util.UUID;

/**
 * Utility class for generating unique identifiers.
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 */
public class GeneratorId {
    private GeneratorId() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * The getter returns the ID.
     *
     * @return id
     */
    public static String getId() {
        return UUID.randomUUID().toString();
    }
}
