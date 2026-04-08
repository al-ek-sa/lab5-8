package edu.itmo.piikt.common.data.OrganizationType;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TypeOrganizationDate implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	private String id;
}
