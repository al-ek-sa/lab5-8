package edu.itmo.piikt.common.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The Enum class contains instances of possible statuses.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@AllArgsConstructor
@Getter
public enum Status {
    FIRED(1), HIRED(2), RECOMMENDED_FOR_PROMOTION(3), PROBATION(4);
    private final int id;
}
