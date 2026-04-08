package edu.itmo.piikt.common.data.OrganizationType;

import lombok.*;

/**
 * The Enum class contains instances of possible organization types.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum OrganizationTypeData {
    COMMERCIAL("1"), PUBLIC("2"), GOVERNMENT("3"), TRUST("4"), OPEN_JOINT_STOCK_COMPANY("5");
    private final String id;
}
