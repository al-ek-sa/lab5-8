package edu.itmo.piikt.managers;


import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import java.io.FileWriter;
import java.io.Writer;
import java.util.LinkedList;

import edu.itmo.piikt.commands.HistoryCommands;


public class HistoryManager {/**
    private static final String HISTORY_FILE = "history.csv";
    private HistoryCommands historyCommands;
    public HistoryManager(){
        this.historyCommands = HistoryCommands.getInstance();
    }
    public void WriteCommands(){
    try (Writer writer = new FileWriter(HISTORY_FILE)){
        LinkedList<String> commands = historyCommands.getLinkedList();
        StatefulBeanToCsv<CommandRecord> beanToCsv = new StatefulBeanToCsv<String>(writer).build();
        beanTiCsv.write(commands);}
    }*/
}