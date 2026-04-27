package edu.itmo.piikt.client.registration;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FactoryRequest {
	public static Request createRegisterRequest(String login, String password, String email) {
		return new RegisterRequest(login, password, email);
	}

	public static Request createLoginRequest(String login, String password) {
		return new LoginRequest(login, password);
	}

	public static Request createResetPasswordRequest(String email, String newPassword) {
		return new ResetPasswordRequest(email, newPassword);
	}
}
