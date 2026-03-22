package edu.itmo.piikt.server.WorkerObject;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.data.WorkerData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class ValidationError {
    private final List<MessageExceptionValidation> errors;
    private final Object data;


}
