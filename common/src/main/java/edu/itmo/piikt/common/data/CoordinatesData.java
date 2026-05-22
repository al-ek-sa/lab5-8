package edu.itmo.piikt.common.data;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data transfer object for coordinates
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Data
@AllArgsConstructor
public class CoordinatesData {
	private String x;
	private String y;
}
