package edu.itmo.piikt.client;

import edu.itmo.piikt.client.entrance.registration.LoginRequest;
import edu.itmo.piikt.client.entrance.registration.RegisterRequest;
import edu.itmo.piikt.client.entrance.registration.Request;
import edu.itmo.piikt.client.entrance.registration.ResetPasswordRequest;
import edu.itmo.piikt.client.mode.InteractiveMode;
import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.client.io.provider.IOProvider;
import edu.itmo.piikt.client.io.providerType.IOConsole;
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
			io.println("Выберите способ входа и введите соответствующую команду: \n> регистрация (register)"
					+ " \n> вход в аккаунт (login) \n> восстановление пароля (reset_password)");
			String request = io.readLine();

			InteractiveMode interactiveMode = new InteractiveMode();
			interactiveMode.execute(client, io);
			client.close();
		} catch (IOException e) {
			logger.error("Client failed: {}", e.getMessage());
		} catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
