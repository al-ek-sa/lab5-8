package edu.itmo.piikt.common.io.provider;

/**
 * An interface that is required for outputting data to the console, as well as
 * implementing data reading. When outputting, the data is provided in color.
 *
 * @author Lishyk Aliaksandra
 * @version 1.1
 */
public interface IOProvider {
    void println(String message);

    String readLine();

    String name();
}
