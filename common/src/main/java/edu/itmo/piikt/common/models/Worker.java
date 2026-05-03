package edu.itmo.piikt.common.models;

import edu.itmo.piikt.common.util.GeneratorId;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.*;

/**
 * The class of the Worker type object.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Worker implements Comparable<Worker>, Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private String uuid;

	private String name;

	private Coordinates coordinates;

	private Float salary;

	private java.time.LocalDate startDate;

	private java.time.LocalDate endDate;

	private Status status;

	private Organization organization;

	public Worker(String name, Coordinates coordinates, Float salary, LocalDate startDate, LocalDate endDate,
			Status status, Organization organization) {
		this.uuid = GeneratorId.getId();
		this.name = name;
		this.coordinates = coordinates;
		this.salary = salary;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.organization = organization;
	}

	/**
	 * Returns a brief description of this Worker. The exact details of the
	 * representation are unspecified and subject to change, but the following may
	 * be regarded as typical:
	 *
	 * <p>
	 * "id: id, name: name, coordinates: coordinates, creationDate: creationDate,
	 * salary: salary, startDate: startDate, endDate: endDate, status: status,
	 * organization: organization"
	 */
	@Override
	public String toString() {
		return "id: " + uuid + ", name: " + name + ", coordinates: "
				+ (coordinates == null ? "null" : coordinates.toString()) + ", salary: " + salary + ", \nstartDate: "
				+ startDate + ", endDate: " + endDate + ", status: " + (status == null ? "null" : status.toString())
				+ ", organization: " + (organization == null ? "null" : organization.toString()) + "\n";
	}

	@Override
	public int compareTo(Worker other) {
		return this.uuid.compareTo(other.uuid);
	}
}
