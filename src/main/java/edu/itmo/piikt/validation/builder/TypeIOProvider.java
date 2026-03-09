package edu.itmo.piikt.validation.builder;

import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.io.data.NameIOProvider;

/**
 * Interface for determining the validation mode based on io provider type.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public interface TypeIOProvider {

    default Validation type(IOProvider io) {
        return io.name().equals(NameIOProvider.CONSOLE.getName()) ? Validation.CONSOLE : Validation.FILE;
    }
}
