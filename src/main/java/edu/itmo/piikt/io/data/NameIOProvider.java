package edu.itmo.piikt.io.data;

/**
 *  Enum containing the names of I/O providers.
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public enum NameIOProvider {
    CONSOLE("Console"), FILE("File");
    private final String name;
    NameIOProvider(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
