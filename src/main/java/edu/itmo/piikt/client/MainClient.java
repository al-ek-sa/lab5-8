package edu.itmo.piikt.client;

import edu.itmo.piikt.client.io.provider.IOProvider;
import edu.itmo.piikt.client.io.providerType.IOConsole;

public class MainClient {
    public static  void main(String[] args) {
        IOProvider io = new IOConsole();

    }
}
