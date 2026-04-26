package edu.itmo.piikt.client.io.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum containing the names of I/O providers.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum NameIOProvider {
	CONSOLE("Console"), FILE("File");
	private final String name;
}
