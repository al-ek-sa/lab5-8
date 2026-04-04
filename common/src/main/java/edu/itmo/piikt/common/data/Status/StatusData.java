package edu.itmo.piikt.common.data.Status;

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
public enum StatusData {
    FIRED("1"), HIRED("2"), RECOMMENDED_FOR_PROMOTION("3"), PROBATION("4");
    private static final long serialVersionUID = 1L;
    // todo документация! обязательно проверить на null, перепроверить анатоции и
    // зависимости подключить
    private final String id;
}
