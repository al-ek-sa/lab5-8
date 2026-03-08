package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.exception.*;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.Status;
import java.util.function.Function;

/**
 * The class returns the selected instance of the enum Status.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public class ValidationStatus implements TypeIOProvider {
    private final Function<IOProvider, Status> statusValidation;
    public ValidationStatus(IOProvider io) {
        Validation validation = type(io);

        this.statusValidation = new Builder<Integer>().add(RulesValidation.enumRuler(Status.values().length))
                .validation(validation).build(reader -> {
                    ConsoleMessage.ENUM.printMessage(reader);
                    for (Status status : Status.values()) {
                        reader.println("(" + status.getId() + ") " + status.name());
                    }

                    return Integer.parseInt(reader.readLine());
                }).andThen(id -> Status.values()[id - 1]);
    }

    public Status status(IOProvider io) {
        return statusValidation.apply(io);
    }
}
