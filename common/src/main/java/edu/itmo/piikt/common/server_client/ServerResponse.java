package edu.itmo.piikt.common.server_client;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.logger.AppLogger;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import lombok.*;

@Builder
public record ServerResponse(boolean execution, String message, Object dataString, List<String> data,
		List<MessageExceptionValidation> errors) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	private static final AppLogger log = new AppLogger(ServerResponse.class);

	public static ServerResponse successfulCompletion(String message) {
		return ServerResponse.builder().execution(true).message(message).build();
	}

	public static ServerResponse successfulCompletion(String message, List<String> data) {
		return ServerResponse.builder().execution(true).message(message).data(data).build();
	}

	public static ServerResponse successfulCompletion(String message, Object dataString) {
		return ServerResponse.builder().execution(true).message(message).dataString(dataString).build();
	}

	public static ServerResponse error(String message, List<MessageExceptionValidation> exception) {
		return ServerResponse.builder().execution(false).message(message).errors(exception).build();
	}

	public static ServerResponse error(String message) {
		return ServerResponse.builder().execution(false).message(message).build();
	}

	public static ServerResponse error(String message, List<MessageExceptionValidation> exception, Object object) {
		return ServerResponse.builder().execution(false).message(message).errors(exception).dataString(object).build();
	}

	public boolean exception() {
		return Optional.ofNullable(errors).map(list -> !list.isEmpty()).orElse(false);
	}

	public void printToConsole() {
		if (!execution()) {
			System.out.println("Ошибка: " + Optional.ofNullable(message).orElse("неизвестная ошибка"));
		} else {
			Optional.ofNullable(message).filter(m -> !m.isBlank()).ifPresent(System.out::println);
			if (data != null && !data.isEmpty()) {
				data.forEach(System.out::println);
			}
			if (errors != null && !errors.isEmpty()) {
				errors.forEach(System.out::println);
			}
		}
		log.debug("Printed response: execution={}, message={}, dataSize={}, errorsSize={}", execution, message,
				data != null ? data.size() : 0, errors != null ? errors.size() : 0);
	}
}
