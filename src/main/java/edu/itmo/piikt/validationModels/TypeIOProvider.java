package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.io.IOProvider;

public interface TypeIOProvider {

    default Validation type(IOProvider io){
        return io.name().equals("true") ? Validation.CONSOLE : Validation.FILE;
    }
}
