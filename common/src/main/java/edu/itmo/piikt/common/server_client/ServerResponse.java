package edu.itmo.piikt.common.server_client;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Builder
@Value
@AllArgsConstructor
public class ServerResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    boolean execution;
    String message;
    Object dataString;
    List<String> data;
    List<MessageExceptionValidation> errors;
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

    public static ServerResponse error(String message, List<MessageExceptionValidation> exeption, Object object) {
        return ServerResponse.builder().execution(false).message(message).errors(exeption).dataString(object).build();
    }

    public boolean exception() {
        return Optional.ofNullable(errors).map(list -> !list.isEmpty()).orElse(false);
    }

    public void printToConsole() {
        if (!isExecution()) {
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
