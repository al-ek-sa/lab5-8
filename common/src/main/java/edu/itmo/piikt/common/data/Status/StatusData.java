package edu.itmo.piikt.common.data.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The Enum class contains instances of possible statuses.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@AllArgsConstructor
@Getter
public enum StatusData {
	FIRED("1"), HIRED("2"), RECOMMENDED_FOR_PROMOTION("3"), PROBATION("4");
	private static final long serialVersionUID = 1L;
	private final String id;
}
