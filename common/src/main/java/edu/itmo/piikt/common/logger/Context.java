package edu.itmo.piikt.common.logger;

import java.util.UUID;
import org.slf4j.MDC;

public class Context implements AutoCloseable {
	private static final String KEY = "id";
	private final String previousId;

	public Context() {
		this(UUID.randomUUID().toString());
	}

	public Context(String id) {
		this.previousId = MDC.get(KEY);
		MDC.put(KEY, id);
	}

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
