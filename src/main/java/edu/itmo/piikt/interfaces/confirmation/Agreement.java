package edu.itmo.piikt.interfaces.confirmation;

/**
 * Enum containing possible positive user responses for confirmation prompts.
 * @author  Lishyk Aliaksandra
 * @version  1.0
 */
public enum Agreement {
    PLUS("+"), YES("yes"), Y("y"), ДА("да"), Д("д");

    private final String name;

    Agreement(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
