package edu.itmo.piikt.common.massage;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum containing messages for user action confirmation prompts.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@AllArgsConstructor
@Getter
public enum MessageConfirmation {
    EXIT("Are you sure you want to exit? (yes/no)", "Command cancelled"),

    CLEAR("Are you sure you want to clear the collection? (yes/no)", "Consent received, clearing collection"),

    ORGANIZATION("Would you like to add organization information? (yes/no)", "Organization details were not provided");

    private String question;
    private String refusal;
}
