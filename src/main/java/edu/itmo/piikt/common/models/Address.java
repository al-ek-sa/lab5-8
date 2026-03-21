package edu.itmo.piikt.common.models;

import com.opencsv.bean.CsvBindByPosition;
import lombok.*;

import java.io.Serializable;

/**
 * The class of the Address type object.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Address implements Serializable {
    @CsvBindByPosition(position = 11)
    private String street;

    /**
     * Returns a brief description of this Address. The exact details of the
     * representation are unspecified and subject to change, but the following may
     * be regarded as typical:
     *
     * <p>
     * "street: street"
     */
    @Override
    public String toString() {
        return " street: " + street;
    }
}
