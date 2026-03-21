package edu.itmo.piikt.common.data;

import java.io.Serializable;

public record MessageExceptionValidation(String name, String message) implements Serializable {
    private static final long serialVersionUID = 1L;
}
