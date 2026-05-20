package edu.itmo.piikt.server.validation.object;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import java.util.List;

/**
 * Record representing validation errors for a data object
 *
 * @param errors
 *            list of validation error messages
 * @param data
 *            data the original data object that failed validation
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public record ValidationError(List<MessageExceptionValidation> errors, Object data) {
}
