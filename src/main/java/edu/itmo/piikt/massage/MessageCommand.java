package edu.itmo.piikt.massage;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum MessageCommand {
    ADD(Optional.of("Start adding an item"), Optional.of("Item successfully added"), Optional.empty()),

    CLEAR(Optional.of("Consent received, clearing collection"), Optional.of("Collection cleared successfully"),
            Optional.empty()),

    COUNT_BY_ORGANIZATION(Optional.of("Enter all values for Organization"),
            Optional.of("Number of elements displayed successfully"),
            Optional.of("Execution error, elements not displayed")),

    EXECUTE_SCRIPT(Optional.of("Start of script reading"), Optional.of("Script successfully read and executed"),
            Optional.empty()),

    EXIT(Optional.of("Exit application"), Optional.empty(), Optional.of("Exit application")),

    FILTER_CONTAINS_NAME(Optional.of("Search users by name"),
            Optional.of("All users with the entered name have been displayed"), Optional.of("Search failed")),

    HEAD(Optional.of("Displaying the last added element"), Optional.of("Element displayed on the screen"),
            Optional.empty()),

    HELP(Optional.empty(), Optional.empty(), Optional.empty()),

    HELP_ENTERING(Optional.empty(), Optional.empty(), Optional.empty()),

    HISTORY(Optional.of("Displaying the last 14 commands"), Optional.of("Commands displayed successfully"),
            Optional.of("Command not executed")),

    INFO(Optional.of("Displaying information about the collection"), Optional.of("Information successfully displayed"),
            Optional.of("Information not displayed")),

    PRINT_FIELD_DESCENDING_END_DATE(Optional.of("Sorting by date of dismissal started"), Optional.empty(),
            Optional.of("Failed to sort")),

    REMOVE_BY_ID(Optional.of("Deletion of item by ID started"), Optional.empty(), Optional.of(
            "Extraneous characters entered in the argument, repeat the command (the argument can only contain integers greater than 0)")),

    SAVE(Optional.of("Saving data to file started"), Optional.empty(), Optional.of("Data saved to file")),

    REMOVE_LOVER(Optional.of("Deletion of items started"), Optional.of("Items successfully deleted"),
            Optional.of("Invalid UUID format")),

    SHOW(Optional.of("Displaying collection"), Optional.of("Collection displayed"),
            Optional.of("Displaying collection interrupted")),

    UPDATE_ID(Optional.of("Start of data update"), Optional.of("Data successfully updated"),
            Optional.of("Invalid UUID format"));

    private final Optional<String> messageBefore;
    private final Optional<String> messageAfter;
    private final Optional<String> messageException;

    MessageCommand(Optional<String> messageBefore, Optional<String> messageAfter, Optional<String> messageException) {
        this.messageBefore = messageBefore;
        this.messageAfter = messageAfter;
        this.messageException = messageException;
    }

    public void loggerBefore(Logger logger) {
        messageBefore.ifPresent(message -> logger.log(Level.INFO, message));
    }

    public void loggerAfter(Logger logger) {
        messageAfter.ifPresent(message -> logger.log(Level.INFO, message));
    }

    public void loggerError(Logger logger, Exception e) {
        messageException.ifPresentOrElse(message -> logger.log(Level.SEVERE, message + ": " + e.getMessage()),
                () -> logger.log(Level.SEVERE, "Exception: " + e.getMessage()));
    }
}
