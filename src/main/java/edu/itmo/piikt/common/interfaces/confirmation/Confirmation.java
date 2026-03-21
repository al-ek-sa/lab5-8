package edu.itmo.piikt.common.interfaces.confirmation;

import edu.itmo.piikt.client.io.data.NameIOProvider;
import edu.itmo.piikt.common.provider.IOProvider;

import java.util.Arrays;

/**
 * Interface implementing a default method for action confirmation.
 *
 * @author Lishyk Aliaksandra
 * @version 1.1
 */
public interface Confirmation {
    /**
     * Requests and validates user confirmation for an action.
     *
     * @param io
     *            the input/output provider
     * @return {@code true} if user confirmed, {@code false} otherwise
     */
    default Boolean confirmation(IOProvider io) {
        if (io.name().equals(NameIOProvider.CONSOLE.getName())) {
            io.printDesign();
            question(io);
            while (true) {
                String input = io.readLine();
                if (Arrays.stream(Agreement.values()).map(Agreement::getName)
                        .anyMatch(name -> name.equalsIgnoreCase(input))) {
                    return true;
                }
                if (Arrays.stream(Refusal.values()).map(Refusal::getName)
                        .anyMatch(name -> name.equalsIgnoreCase(input))) {
                    refusal(io);
                    return false;
                }
                io.println("please enter yes/no");
            }
        } else {
            return true;
        }
    }

    /**
     * Displays the confirmation question to the user.
     *
     * @param io
     *            the input/output provider
     */
    void question(IOProvider io);

    /**
     * Handles user's refusal to confirm the action.
     *
     * @param io
     *            the input/output provider
     */
    void refusal(IOProvider io);
}
