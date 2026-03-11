package edu.itmo.piikt.interfaces.confirmation;

/**
 * Enum containing possible negative user responses for confirmation prompts.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public enum Refusal {
    MINES("-"), NO("no"), N("n"), НЕТ("нет"), Н("н");

    private final String name;

    Refusal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
