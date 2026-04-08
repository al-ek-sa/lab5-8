package edu.itmo.piikt.common.data;

import java.io.Serial;
import java.io.Serializable;

public record MessageExceptionValidation(String name, String message) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}
