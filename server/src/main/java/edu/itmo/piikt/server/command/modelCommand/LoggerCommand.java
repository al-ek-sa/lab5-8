package edu.itmo.piikt.server.command.modelCommand;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoggerCommand {
    ADD("успешно выполнена команда add"),
    HELP("уcпешно выполнена команда help"),
    CLEAR("успешно выполнена команда clear"),
    COUNT_BY_ORGANIZATION("успешно выполнена команда count_by_organization"),
    EXIT("успешно выполнена команда exit на стороне клиента"),
    FILTER_CONTAINS_NAME("успешно выполнена команда filter contains name"),
    HEAD("успешно выполнена команда head"),
    HELP_ENTERING("успешно выполнена команда help entering"),
    INFO("успешно выполнена команда info"),
    REMOVE_BY_ID("успешно выполнена команда remove by id"),
    REMOVE_LOWER("успешно выполнена команда remove lower"),
    PRINT_DATE("успешно выполнена команда print field descending end date"),
    SHOW("успешно выполнена команда show"),
    UPDATE("успешно выполнена команда update");
    private final  String logMessage;
}
