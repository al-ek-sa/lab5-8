package edu.itmo.piikt.common.interfaces.confirmation;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum containing possible positive user responses for confirmation prompts.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum Agreement {
    PLUS("+"), YES("yes"), Y("y"), ДА("да"), Д("д");

    private final String name;
}
