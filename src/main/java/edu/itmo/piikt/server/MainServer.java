package edu.itmo.piikt.server;

import edu.itmo.piikt.server.dispatcher.Dispetcher;
import edu.itmo.piikt.server.saveManager.CSVParser;

public class MainServer {
    public static void main(String[] args) {
        CSVParser csvParser = new CSVParser();
        Dispetcher dispetcher = new Dispetcher();
    }
}
