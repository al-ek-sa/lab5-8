package edu.itmo.piikt.managers;

import com.opencsv.*;
import edu.itmo.piikt.historyWorker.HistoryWorker;

import java.io.FileOutputStream;
import java.io.PrintWriter;

/**public class WorkerManager {
    private HistoryWorker historyWorker;
    private String fileName;

    public WorkerManager() {
        this.fileName = System.getenv("");
        if (this.fileName == null) {
            this.fileName = "worker.csv";
        }
    }
    public void saveToFile(LinkedList<Worker> workers) {
        PrintWriter writer = new PrintWriter(new FileOutputStream("worker.csv"));
}
*/