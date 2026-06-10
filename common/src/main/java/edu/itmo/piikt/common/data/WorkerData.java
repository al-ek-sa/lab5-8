package edu.itmo.piikt.common.data;

import edu.itmo.piikt.common.data.status.DataStatus;
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
public final class WorkerData {
	private String name;
	private CoordinatesData coordinates;
	private String salary;
	private String startDate;
	private String endDate;
	private DataStatus status;
	private OrganizationData organization;
}
