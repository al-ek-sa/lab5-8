package edu.itmo.piikt.client.registration;

public class LoginRequest implements Request {
	private String login;
	private String password;
	public LoginRequest(String login, String password) {
		this.login = login;
		this.password = password;
	}
	@Override
	public String execute() {
		return "login";
	}
}
