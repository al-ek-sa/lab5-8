package edu.itmo.piikt.reader;

import com.opencsv.bean.*;
import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.Worker;
import java.io.*;
import java.util.List;

public class CSVParser {
    private IOProvider io;
    private String fileName;
    public CSVParser(IOProvider io) {
        this.io = io;
        this.fileName = System.getenv("WORKER_FILE");
        if (this.fileName == null || this.fileName.isEmpty()) {
            this.fileName = "workers.csv";
        }
    }

    public void saveCollection() {
        List<Worker> workers = HistoryWorker.getInstance(io).getListWorker();

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {

            ColumnPositionMappingStrategy<Worker> mappingStrategy = new ColumnPositionMappingStrategy<>();
            mappingStrategy.setType(Worker.class);

            String[] columns = new String[] {
                    "id",
                    "name",
                    "coordinates.x",
                    "coordinates.y",
                    "salary",
                    "startDate",
                    "endDate",
                    "status",
                    "organization.annualTurnover",
                    "organization.type",
                    "organization.officialAddress.street"
            };
            mappingStrategy.setColumnMapping(columns);

            StatefulBeanToCsv<Worker> beanWriter = new StatefulBeanToCsvBuilder<Worker>(writer)
                    .withMappingStrategy(mappingStrategy)
                    .withSeparator(';')
                    .build();

            beanWriter.write(workers);

        } catch (Exception e) {
            io.printError("Error saving CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
}