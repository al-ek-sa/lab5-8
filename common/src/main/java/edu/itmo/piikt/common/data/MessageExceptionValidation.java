package edu.itmo.piikt.common.data;

import java.io.Serial;
import java.io.Serializable;

/**
 *	Record for validation error messages
 *
 * @param name name of the field that failed validation
 * @param message error description
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public record MessageExceptionValidation(String name, String message) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}
