package edu.itmo.piikt.common.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for coordinates
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoordinatesData {
	private String x;
	private String y;
}
