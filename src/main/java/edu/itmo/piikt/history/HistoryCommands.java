package edu.itmo.piikt.history;

import java.util.LinkedList;

/**
 * A class for storing all entered commands. The class is a singleton.
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 */
public enum HistoryCommands {
    INSTANCE;
    private LinkedList<String> listCommands = new LinkedList<>();
    // todo
    public void add(String command) {
        listCommands.addFirst(command);
    }

    public LinkedList<String> getLinkedList() {
        return listCommands;
    }

    public void printHistory() {
        listCommands.descendingIterator().forEachRemaining(System.out::println);
    }
}
