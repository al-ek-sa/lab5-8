package edu.itmo.piikt.common.data;

import lombok.*;

import java.io.Serializable;

/**
 * The Enum class contains instances of possible organization types.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Getter
@ToString(includeFieldNames = true)
@AllArgsConstructor
public enum OrganizationTypeData implements Serializable {
    COMMERCIAL("1"), PUBLIC("2"), GOVERNMENT("3"), TRUST("4"), OPEN_JOINT_STOCK_COMPANY("5");
    private static final long serialVersionUID = 1L;
    //todo документация! обязательно проверить на null, перепроверить анатоции и зависимости подключить
    private final String id;
}
