package edu.itmo.piikt.common.interfaces.confirmation;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum containing possible negative user responses for confirmation prompts.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum Refusal {
    MINES("-"), NO("no"), N("n"), НЕТ("нет"), Н("н");

    private final String name;
}
