package edu.itmo.piikt.common.server_client;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Builder
public record ServerResponse(boolean execution, String message, Object dataString, List<String> data,
                             List<MessageExceptionValidation> errors) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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
            System.out.println("Ошибка: " + message);
            if (errors != null && !errors.isEmpty()) {
                for (MessageExceptionValidation error : errors) {
                    System.out.println("  " + error.name() + ": " + error.message());
                }
            }
        } else {
            if (message != null && !message.isEmpty()) {
                System.out.println(message);
            }
            if (data != null && !data.isEmpty()) {
                for (Object item : data) {
                    System.out.println(item);
                }
            }
            if (errors != null && !errors.isEmpty()) {
                for (MessageExceptionValidation error : errors) {
                    System.out.println("  " + error.name() + ": " + error.message());
                }
            }
        }
    }
}
