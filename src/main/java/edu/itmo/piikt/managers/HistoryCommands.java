package edu.itmo.piikt.managers;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * A class for storing all entered commands. The class is a singleton.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public enum HistoryCommands {
    INSTANCE;
    private LinkedList<String> listCommands = new LinkedList<>();

    public void add(String command) {
        listCommands.addFirst(command);
    }

    public Iterator<String> getIterator() {
        return listCommands.iterator();
    }

    public LinkedList<String> getLinkedList() {
        return listCommands;
    }

    public void printHistory() {
        Iterator<String> iterator = listCommands.descendingIterator();
        while (iterator.hasNext()) {
            String command = iterator.next();
            System.out.println(command);
        }
    }
}
