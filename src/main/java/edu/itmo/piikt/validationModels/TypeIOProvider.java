package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.NameIOProvider;

public interface TypeIOProvider {

    default Validation type(IOProvider io) {
        return io.name().equals(NameIOProvider.CONSOLE.getName()) ? Validation.CONSOLE : Validation.FILE;
    }
}
