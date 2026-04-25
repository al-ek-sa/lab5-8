package edu.itmo.piikt.common.data;

import java.io.Serial;
import java.io.Serializable;
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
public final class AddressData implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	private String street;
}
