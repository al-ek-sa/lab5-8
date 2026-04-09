package edu.itmo.piikt.client.commands;

import edu.itmo.piikt.client.command.AddCommand;
import edu.itmo.piikt.client.command.ExecuteScriptCommand;
import edu.itmo.piikt.client.command.UpdateCommand;
import edu.itmo.piikt.client.command.history.HistoryCommand;
import edu.itmo.piikt.client.data.Worker;
import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.command.data.Commands;

import java.util.HashMap;
import java.util.Map;

public class CommandMap {
	private final Map<String, Command> map = new HashMap<>();
	public CommandMap(Network network, Worker worker) {
		map.put(Commands.ADD.getName(), new AddCommand(network));
		map.put(Commands.UPDATE.getName(), new UpdateCommand(network, null));
		map.put(Commands.HISTORY.getName(), new HistoryCommand());
		map.put(Commands.EXECUTE_SCRIPT.getName(), new ExecuteScriptCommand());
	}
}
