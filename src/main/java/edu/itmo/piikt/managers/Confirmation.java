package edu.itmo.piikt.managers;

import edu.itmo.piikt.io.IOProvider;

/**
 * An interface that must be inherited if the command adds some conditions for execution.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public interface Confirmation {
    String confirmation(IOProvider io);
}
