package edu.itmo.piikt.common.models;

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
public enum OrganizationType implements Serializable {
    COMMERCIAL(1), PUBLIC(2), GOVERNMENT(3), TRUST(4), OPEN_JOINT_STOCK_COMPANY(5);
    private final int id;
}
