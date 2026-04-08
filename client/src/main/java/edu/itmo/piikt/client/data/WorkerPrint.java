package edu.itmo.piikt.client.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WorkerPrint {
	SALARY("Enter salary*", "Enter salary again*"), NAME("Enter name*", "Enter name again*"), X(
			"Enter coordinate X* (number must be less than 10)",
			"Enter X again* (value must not exceed 10)"), Y("Enter coordinate Y* (number must be greater than -644)",
					"Enter Y again* (value must not be less than -644)"), STREET("Enter company address",
							"Enter address again*"), START_DATE("Enter start date* (Example: 1111-11-11)",
									"Enter start date again* (Format: 1111-11-11)"), END_DATE(
											"Select end date (Example: 1111-11-11)",
											"Enter end date again* (Format: 1111-11-11)"), ANNUAL_TURNOVER(
													"Enter company annual turnover",
													"Enter company turnover again"), STATUS(
															"Select status and enter its number*",
															"Enter status number again*"), TYPE("Enter company type",
																	"Enter type number again");

	private final String message;
	private final String messageError;
}
