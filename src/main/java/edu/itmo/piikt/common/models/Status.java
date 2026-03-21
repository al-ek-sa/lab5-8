package edu.itmo.piikt.common.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * The Enum class contains instances of possible statuses.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@AllArgsConstructor
@Getter
public enum Status implements Serializable {
    FIRED(1), HIRED(2), RECOMMENDED_FOR_PROMOTION(3), PROBATION(4);
    private final int id;
}
