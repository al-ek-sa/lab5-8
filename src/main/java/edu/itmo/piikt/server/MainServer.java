package edu.itmo.piikt.server;

import edu.itmo.piikt.server.dispatcher.Dispatcher;
import edu.itmo.piikt.server.saveManager.CSVParser;

public class MainServer {
    public static void main(String[] args) {
        CSVParser csvParser = new CSVParser();
        Dispatcher dispetcher = new Dispatcher();
    }
}
