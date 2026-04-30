package edu.itmo.piikt.client;

import edu.itmo.piikt.client.mode.InteractiveMode;
import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.client.io.provider.IOProvider;
import edu.itmo.piikt.client.io.providerType.IOConsole;
import edu.itmo.piikt.client.registration.FactoryRequest;
import edu.itmo.piikt.client.registration.Request;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Config;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;

import java.io.IOException;

/**
 * Initializes the client, establishes connection to the server, and starts the
 * command processing loop. Handles reconnection attempts and graceful shutdown
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public class MainClient {
	private static final AppLogger logger = new AppLogger(MainClient.class);
	public static void main(String[] args) {
		Config.configureFromArgs(args);

		try (Context ignored = Context.newId()) {
			logger.info("Starting client");
			Network client = new Network();
			client.connect();
			IOProvider io = new IOConsole();
			String request = io.readLine();
			Request request1 = null;
			while (request1 == null) {
				io.println("Выберите способ входа и введите соответствующую команду: \n> регистрация (register)"
						+ " \n> вход в аккаунт (login) \n> восстановление пароля (reset_password)");
				switch (request) {
					case "login" -> {
						String login = io.readLine();
						String password = io.readLine();
						request1 = FactoryRequest.createLoginRequest(login, password);
					}
					case "register" -> {
						String login = io.readLine();
						String password = io.readLine();
						String email = io.readLine();
						request1 = FactoryRequest.createRegisterRequest(login, password, email);
					}
					case "reset_password" -> {
						String email = io.readLine();
						String password = io.readLine();
						request1 = FactoryRequest.createResetPasswordRequest(email, password);
					}
				}
				//ClientCommand clientCommand = ClientCommand.builder().build();
			}
			InteractiveMode interactiveMode = new InteractiveMode();
			interactiveMode.execute(client, io);
			client.close();
		} catch (IOException e) {
			logger.error("Client failed: {}", e.getMessage());
		}
	}
}
