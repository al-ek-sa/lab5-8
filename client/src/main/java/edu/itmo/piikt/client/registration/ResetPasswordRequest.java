package edu.itmo.piikt.client.registration;

public class ResetPasswordRequest implements Request {
	private String login;
	private String email;
	public ResetPasswordRequest(String login, String email) {
		this.login = login;
		this.email = email;
	}

	@Override
	public String execute() {
		return "reset_password";
	}
}
