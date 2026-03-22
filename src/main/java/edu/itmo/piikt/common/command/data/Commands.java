package edu.itmo.piikt.common.command.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum containing data for all available commands. This enumeration stores
 * comprehensive information about each command, including its name,
 * description, and syntax rules.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
@Getter
@AllArgsConstructor
public enum Commands {
    HELP("help", "display help on available commands",
            "Both in the console and in the script, the command is entered as a single word without arguments. (\"help\")"),

    INFO("info",
            "output information about the collection to stdout (type, initialization date, number of elements, etc.)",
            "Both in the console and in the script, the command is entered as a single word without arguments. (\"info\")"),

    SHOW("show", "output all collection elements in string representation to stdout",
            "Both in the console and in the script, the command is entered as a single word without arguments. (\"show\")"),

    ADD("add", "add a new element to the collection", "In the console, only the "
            + "command is entered without arguments (\"add\"). \nIn script mode, the data can be entered either as a single CSV line \n"
            + "(add {\"name\";\"x\";\"y\";\"salary\";\"startDate\";\"endDate\";status\";\"annual turnover\";"
            + "\"organization type\";\"street\"}), or line by line in the same order."),

    UPDATE("update", "update the value of the collection element whose id is equal to the specified one",
            "In the console, the command is entered together with the id on one line\n(\"update _____\"). In the script, the command is "
                    + "entered together with the\nid on one line (\"update _____\"), and then the data is entered line by line, in the same "
                    + "order as the add command."),

    REMOVE_BY_ID("remove_by_id", "remove an element from the collection by its id",
            "Both in the console and in the script, the command is entered and \nthen the id is specified on the same line. (\"remove_by_id _____\")"),

    CLEAR("clear", "clear the collection",
            "Both in the console and in the script, the command is entered as a single word without arguments.(\"clear\")"),

    SAVE("save", "save the collection to a file",
            "save: Both in the console and in the script, the command is entered as a single word without arguments. (\"save\")"),

    EXECUTE_SCRIPT("execute_script", "read and execute a script from the specified file",
            "Both in the console and in the script, the command is entered on one line (\"execute_script _____\")."),

    EXIT("exit", "terminate the program (without saving to file)",
            "Both in the console and in the script, the command is entered as a single word without arguments.(\"exit\")"),

    HEAD("head", "output the first element of the collection",
            "Both in the console and in the script, the command is entered as a single word without arguments. (\"head\")"),

    REMOVE_LOWER("remove_lower", "remove all elements from the collection that are less than the specified one",
            "Both in the console and in the script, the command is entered with a date (\"remove_lower yyyy-MM-dd\")"),

    HISTORY("history", "output the last 14 commands (without their arguments)",
            "Both in the console and in the script, the command is entered as a single word without arguments. (\"history\")"),

    COUNT_BY_ORGANIZATION("count_by_organization",
            "output the number of elements whose organization field value is equal to the specified one",
            "In the console, only the command name is entered\n(\"count_by_organization\"), while in the script, the command is entered, and\n"
                    + "then the data is entered line by line in the following order (annual turnover; type; street)."),

    FILTER_CONTAINS_NAME("filter_contains_name",
            "output elements whose name field value contains the specified substring",
            "Both in the console and in the script, the command is entered, and then the employee's name is entered on the same line. (\"filter_contains_name _____\")."),

    PRINT_FIELD_DESCENDING_END_DATE("print_field_descending_end_date",
            "output the values of the endDate field of all elements in descending order",
            "Both in the console and in the script, the command is entered as a single \nword without arguments.(\"print_field_descending_end_date\")"),

    HELP_ENTERING_COMMAND("help_entering_command", "display help on entering available commands",
            "Both in the console and in the script, the command is entered as a single word without arguments. (\"help_entering_command\")");

    private final String name;
    private final String description;
    private final String help;


    //todo возращает null очень аккуратно
    public static Commands nameCommands(String name) {
        for (Commands commands : values()) {
            if (commands.getName().equals(name)) {
                return commands;
            }
        }
        return  null;
    }
}
