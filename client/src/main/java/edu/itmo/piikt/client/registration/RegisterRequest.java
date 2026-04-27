package edu.itmo.piikt.client.registration;

public class RegisterRequest implements Request {
	private String login;
	private String password;
	private String email;
	public RegisterRequest(String login, String password, String email) {
		this.email = email;
		this.login = login;
		this.password = password;
	}

	@Override
	public String execute() {
		return "register";
	}
}
