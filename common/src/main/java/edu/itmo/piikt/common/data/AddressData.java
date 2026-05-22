package edu.itmo.piikt.common.data;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data transfer object for address information
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Data
@AllArgsConstructor
public final class AddressData {
	private String street;
}
