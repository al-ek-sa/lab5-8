package edu.itmo.piikt.server.validation.modelValidation;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.models.Status;
import edu.itmo.piikt.server.validation.builder.Builder;
import edu.itmo.piikt.server.validation.builder.RulesValidation;
import java.util.Optional;
import java.util.function.Function;

/**
 * The class returns the selected instance of the enum Status.
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 * @see Function
 * @see Builder
 * @see Status
 */
public class ValidationStatus {
    private static final AppLogger logger = new AppLogger(ValidationStatus.class);
    private final Function<String, Optional<MessageExceptionValidation>> statusValidation;

    public ValidationStatus() {
        this.statusValidation = new Builder<String>("status").add(RulesValidation.validationStatus()).build();
        logger.debug("ValidationStatus initialized");
    }

    public Optional<MessageExceptionValidation> validationStatus(String status) {
        try (Context ignored = Context.newId()) {
            logger.debug("Validating status: {}", status);
            return statusValidation.apply(status);
        } catch (Exception e) {
            logger.error("Error validating status: {}", e.getMessage());
            return Optional.of(new MessageExceptionValidation("status", "Validation error: " + e.getMessage()));
        }
    }

    // todo nullpointer оч аккуратно
    public Status status(int status) {
        try (Context context = Context.newId()) {
            Status result = Status.values()[status - 1];
            logger.debug("Status resolved: {}", result);
            return result;
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.error("Invalid status index: {}", status);
            return Status.FIRED;
        }
    }
}