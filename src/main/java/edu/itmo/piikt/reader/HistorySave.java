package edu.itmo.piikt.reader;

import edu.itmo.piikt.managers.HistoryCommands;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class HistorySave {
        private String fileName;
        private static HistorySave instance;
        private HistorySave() {
            this.fileName = System.getenv("HISTORY_FILE");
            if (this.fileName == null || this.fileName.isEmpty()) {
                this.fileName = "history.txt";
            }
        }

        public static HistorySave getInstance(){
            if (instance == null){
                instance = new HistorySave();
            }
            return instance;}

        public void saveCollection() {
            List<String> commands = HistoryCommands.getInstance().getLinkedList();

            try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
                for(String command : commands) {
                    writer.println(command);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void readFile(){
            try (Scanner scanner = new Scanner(new File(fileName))){
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    HistoryCommands.getInstance().add(line);}

            } catch (IOException e) {
            }
    }
}
