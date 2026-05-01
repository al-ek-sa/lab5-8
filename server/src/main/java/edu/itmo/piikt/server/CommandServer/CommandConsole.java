package edu.itmo.piikt.server.CommandServer;

import edu.itmo.piikt.common.algorithms.DamerauLevenshteinDistance;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.logger.AppLogger;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public enum CommandConsole {
	INSTANCE;

	private static final AppLogger logger = new AppLogger(CommandConsole.class);
	private CommandFactory commandFactory = new CommandFactory();

	public void execute(IOProvider io) {
		while (true) {
			String command = io.readLine().toLowerCase();
			commandFactory.execute(command, io);
		}
	}
}
