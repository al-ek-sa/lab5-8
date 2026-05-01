package edu.itmo.piikt.common.data.organization.type;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Wrapper for organization type identifier
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TypeOrganizationDate implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	private String id;
}
