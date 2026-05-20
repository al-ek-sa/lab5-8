package edu.itmo.piikt.common.data;

import edu.itmo.piikt.common.data.status.DataStatus;
import java.io.Serial;
import java.io.Serializable;
import lombok.*;

/**
 * Data transfer object for Worker information
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class WorkerData implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	private String name;
	private CoordinatesData coordinates;
	private String salary;
	private String startDate;
	private String endDate;
	private DataStatus status;
	private OrganizationData organization;
}
