package edu.itmo.piikt.validationModels;

import java.util.UUID;

/**
 * The class generates unique IDs for employees. The generator is a singleton.
 * The generation is not thread-safe.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public class GeneratorId {
    private static GeneratorId instance;

    private GeneratorId() {
    }

    public static GeneratorId getInstance() {
        if (instance == null) {
            instance = new GeneratorId();
        }
        return instance;
    }

    /**
     * The getter returns the ID and then increments it by 1.
     *
     * @return id
     */
    public String getId() {
        return UUID.randomUUID().toString();
    }
}
