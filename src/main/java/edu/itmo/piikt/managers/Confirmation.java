package edu.itmo.piikt.managers;

import edu.itmo.piikt.io.IOProvider;

/**
 * An interface that must be inherited if the command adds some conditions for
 * execution.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public interface Confirmation {
    default Boolean confirmation(IOProvider io) {
        if (io.name().equals(NameIOProvider.CONSOLE.getName())) {
            io.printeDesign();
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

    void question(IOProvider io);
    void refusal(IOProvider io);
}
