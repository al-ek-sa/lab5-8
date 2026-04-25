package edu.itmo.piikt.common.util;

import java.util.UUID;
import lombok.experimental.UtilityClass;

/**
 * Utility class for generating unique identifiers.
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 * @see UUID
 */
@UtilityClass
public class GeneratorId {
	/**
	 * The getter returns the ID.
	 *
	 * @return id
	 */
	public static String getId() {
		return UUID.randomUUID().toString();
	}
}
