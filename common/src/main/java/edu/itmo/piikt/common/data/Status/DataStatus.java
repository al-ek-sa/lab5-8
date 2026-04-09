package edu.itmo.piikt.common.data.Status;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Wrapper for worker status identifier
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataStatus implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	private String id;
}
