package edu.itmo.piikt.common.models;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvRecurse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The class of the Organization type object.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public final class Organization {
    @CsvBindByPosition(position = 9)
    private int annualTurnover;

    @CsvBindByPosition(position = 10)
    private OrganizationType type;

    @CsvRecurse
    private Address officialAddress;

    /**
     * Returns a brief description of this Organization. The exact details of the
     * representation are unspecified and subject to change, but the following may
     * be regarded as typical:
     *
     * <p>
     * "annualTurnover: annualTurnover, type: type, officialAddress:
     * officialAddress"
     */
    @Override
    public String toString() {
        return " annualTurnover: " + annualTurnover + ", type: " + (type == null ? "null" : type.toString())
                + ", officialAddress: " + (officialAddress == null ? "null" : officialAddress.toString());
    }
}
