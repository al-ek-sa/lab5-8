package edu.itmo.piikt.interfaces.confirmation;

/**
 * Enum containing messages for user action confirmation prompts.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public enum MessageConfirmation {
    EXIT("Are you sure you want to exit? (yes/no)", "Command cancelled"),

    CLEAR("Are you sure you want to clear the collection? (yes/no)", "Consent received, clearing collection"),

    ORGANIZATION("Would you like to add organization information? (yes/no)", "Organization details were not provided");

    private String question;
    private String refusal;

    MessageConfirmation(String question, String refusal) {
        this.question = question;
        this.refusal = refusal;
    }

    public String getQuestion() {
        return question;
    }

    public String getRefusal() {
        return refusal;
    }
}
