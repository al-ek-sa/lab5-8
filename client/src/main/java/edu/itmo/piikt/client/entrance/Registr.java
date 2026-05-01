package edu.itmo.piikt.client.entrance;

import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.algorithms.DamerauLevenshteinDistance;
import edu.itmo.piikt.common.server_client.ClientCommand;
import lombok.Data;

@Data
public class Registr {
	private int count = 3;
	private final IOProvider io;
	private final Command command;
	public Registr(IOProvider io) {
		this.io = io;
		this.command = new Command(io);
	}
	public ClientCommand registration() {
		String type = io.readLine();
		if (DamerauLevenshteinDistance.distance(type.toLowerCase(), "login") <= 1) {
			return command.executeCommand("login");
		} else if (DamerauLevenshteinDistance.distance(type.toLowerCase(), "register") <= 1) {
			return command.executeCommand("register");

		} else if (DamerauLevenshteinDistance.distance(type.toLowerCase(), "reset_password") <= 1) {
			return command.executeCommand("reset_password");
		}

		return null;
	}
}
