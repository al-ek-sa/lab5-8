package edu.itmo.piikt.server.validation.modelValidation;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.server.validation.builder.Builder;
import edu.itmo.piikt.server.validation.builder.RulesValidation;
import java.util.Optional;
import java.util.function.Function;

/**
 * The class generates Coordinates with the specified fields:
 *
 * <ul>
 * <li>private long x; //Maximum field value: 10
 * <li>private float y; //The field value must be greater than -644
 * </ul>
 *
 * <p>
 * The class provides methods that validate the field values.
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 * @see Function
 * @see Builder
 */
public class ValidationCoordinates {
	private static final AppLogger logger = new AppLogger(ValidationCoordinates.class);
	/** Validation function for X coordinate */
	private final Function<String, Optional<MessageExceptionValidation>> xValidation;
	/** Validation function for Y coordinate */
	private final Function<String, Optional<MessageExceptionValidation>> yValidation;

	public ValidationCoordinates() {
		this.xValidation = new Builder<String>("x").add(RulesValidation.validationX2()).build();
		this.yValidation = new Builder<String>("y").add(RulesValidation.validationY2()).build();
		logger.debug("ValidationCoordinates initialized");
	}

	/**
	 * Validates the X coordinate
	 *
	 * @param x
	 *            X coordinate value
	 * @return mpty Optional if valid, Optional with error message if invalid
	 */
	public Optional<MessageExceptionValidation> validationX(String x) {
		try (Context ignored = Context.newId()) {
			logger.debug("Validating x: {}", x);
			return xValidation.apply(x);
		} catch (Exception e) {
			logger.error("Error validating x: {}", e.getMessage());
			return Optional.of(new MessageExceptionValidation("x", "Validation error: " + e.getMessage()));
		}
	}

	/**
	 * Validates the Y coordinate
	 *
	 * @param y
	 *            Y coordinate value
	 * @return empty Optional if valid, Optional with error message if invalid
	 */
	public Optional<MessageExceptionValidation> validationY(String y) {
		try (Context ignored = Context.newId()) {
			logger.debug("Validating y: {}", y);
			return yValidation.apply(y);
		} catch (Exception e) {
			logger.error("Error validating y: {}", e.getMessage());
			return Optional.of(new MessageExceptionValidation("y", "Validation error: " + e.getMessage()));
		}
	}
}
