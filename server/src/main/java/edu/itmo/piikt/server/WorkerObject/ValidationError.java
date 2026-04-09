package edu.itmo.piikt.server.WorkerObject;

import edu.itmo.piikt.common.data.MessageExceptionValidation;

import java.util.List;

public record ValidationError(List<MessageExceptionValidation> errors, Object data) {
}
