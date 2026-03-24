package edu.itmo.piikt.client;

import edu.itmo.piikt.client.io.provider.IOProvider;
import edu.itmo.piikt.client.io.providerType.IOConsole;
import edu.itmo.piikt.client.manager.ValidationCommand;
import edu.itmo.piikt.client.network.Network;

public class MainClient {

    private static final String HOST = "localhost";
    private static final int MAX_ATTEMPTS = 7;

    public static void main(String[] args) {
        IOProvider io = new IOConsole();
        Network client = null;

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                Network network = new Network(HOST);
                network.connect();
                client = network;
                break;
            } catch (Exception e) {
                if (attempt == MAX_ATTEMPTS) {
                    return;
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    return;
                }
            }
        }

        try {
            ValidationCommand.INSTANCE.setNetwork(client);
            ValidationCommand.INSTANCE.validation(io);
        } catch (Exception e) {
            //прописать исключение
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                // прописать исключение
            }
        }
    }
}