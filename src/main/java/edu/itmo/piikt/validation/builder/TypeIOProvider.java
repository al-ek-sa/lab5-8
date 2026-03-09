package edu.itmo.piikt.validation.builder;

import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.io.data.NameIOProvider;

public interface TypeIOProvider {

    default Validation type(IOProvider io) {
        return io.name().equals(NameIOProvider.CONSOLE.getName()) ? Validation.CONSOLE : Validation.FILE;
    }
}
