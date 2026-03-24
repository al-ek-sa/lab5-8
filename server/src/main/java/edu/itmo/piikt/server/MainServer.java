package edu.itmo.piikt.server;

import edu.itmo.piikt.server.dispatcher.Dispatcher;
import edu.itmo.piikt.server.netWork.NetWork;
import edu.itmo.piikt.server.saveManager.CSVParser;

import java.io.IOException;

public class MainServer {
    public static void main(String[] args) {
        CSVParser csvParser = new CSVParser();
        csvParser.readFile();
        Dispatcher dispatcher = new Dispatcher();
        NetWork netWork = new NetWork(dispatcher);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            netWork.stop();
            csvParser.saveCollection();
        }));
        try {
            netWork.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
