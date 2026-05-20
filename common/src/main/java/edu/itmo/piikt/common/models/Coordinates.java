package edu.itmo.piikt.common.models;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The class of the Coordinates type object.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Coordinates implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private long x;

	private float y;

	/**
	 * Returns a brief description of these Coordinates. The exact details of the
	 * representation are unspecified and subject to change, but the following may
	 * be regarded as typical:
	 *
	 * <p>
	 * "coordinate х: x, coordinate у: y"
	 */
	@Override
	public String toString() {
		return " coordinate х: " + x + ", coordinate у: " + y;
	}
}
