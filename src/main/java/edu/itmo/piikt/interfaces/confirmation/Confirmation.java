package edu.itmo.piikt.interfaces.confirmation;

import edu.itmo.piikt.io.data.NameIOProvider;
import edu.itmo.piikt.io.provider.IOProvider;

/**
 * Interface implementing a default method for action confirmation.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
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
                for (Agreement agree : Agreement.values()) {
                    if (input.equals(agree.getName())) {
                        return true;
                    }
                }
                for (Refusal fals : Refusal.values()) {
                    if (input.equals(fals.getName())) {
                        refusal(io);
                        return false;

                    }
                }
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
