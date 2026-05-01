package edu.itmo.piikt.client.entrance;

import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.algorithms.DamerauLevenshteinDistance;
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
	public void registration() {
		while (true) {
			String type = io.readLine();
			if (DamerauLevenshteinDistance.distance(type.toLowerCase(), "login") <= 1) {
				if (count > 0) {
					command.executeCommand("login");
					count = count - 1;
				} else {
					command.executeCommand("reset_password");
				}
			} else if (DamerauLevenshteinDistance.distance(type.toLowerCase(), "register") <= 1) {
				command.executeCommand("register");

			} else if (DamerauLevenshteinDistance.distance(type.toLowerCase(), "reset_password") <= 1) {
				command.executeCommand("reset_password");
			}
		}
	}
}
