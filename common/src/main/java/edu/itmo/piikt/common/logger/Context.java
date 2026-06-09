package edu.itmo.piikt.common.logger;

import java.util.UUID;
import org.slf4j.MDC;

/**
 * Context manager for Mapped Diagnostic Context (MDC) to track request IDs
 * across logs
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class Context implements AutoCloseable {
	private static final String KEY = "id";
	private final String previousId;

	/**
	 * Creates a new context with a randomly generated UUID
	 */
	public Context() {
		this(UUID.randomUUID().toString());
	}

	public Context(String id) {
		this.previousId = MDC.get(KEY);
		MDC.put(KEY, id);
	}

	/**
	 * Restores the previous MDC value when the context is closed
	 */
	@Override
	public void close() {
		if (previousId != null) {
			MDC.put(KEY, previousId);
		} else {
			MDC.remove(KEY);
		}
	}

	public static String getId() {
		return MDC.get(KEY);
	}

	public static Context newId() {
		return new Context();
	}
}
