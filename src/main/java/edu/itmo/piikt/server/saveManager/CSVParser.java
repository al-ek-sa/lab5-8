package edu.itmo.piikt.server.saveManager;

import com.opencsv.bean.*;
import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.client.io.provider.IOProvider;
import edu.itmo.piikt.common.models.Worker;
import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements parsing a collection containing employee data into CSV
 * format, saving to a file, and reading data from a file.
 *
 * @author Lishyk Aliaksandra
 * @version 1.1
 * @see Logger
 * @see CSVParser
 * @see HistoryWorker
 * @see ColumnPositionMappingStrategy
 * @see StatefulBeanToCsv
 * @see BufferedInputStream
 * @see BufferedReader
 * @see InputStreamReader
 */
public class CSVParser {
    private String fileName;
    Logger logger = Logger.getLogger(CSVParser.class.getName());

    public CSVParser() {
        this.fileName = System.getenv("WORKER_FILE");
        if (this.fileName == null || this.fileName.isEmpty()) {
            this.fileName = "workers.csv";
        }
    }

    /**
     * The method implements parsing employee data with saving to a file.
     *
     * @throws Exception
     *             If file system errors occurred.
     */
    public void saveCollection() {
        List<Worker> workers = HistoryWorker.INSTANCE.getListWorker();

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {

            ColumnPositionMappingStrategy<Worker> mappingStrategy = new ColumnPositionMappingStrategy<>();
            mappingStrategy.setType(Worker.class);

            String[] columns = new String[]{"id", "name", "coordinates.x", "coordinates.y", "creationDate", "salary",
                    "startDate", "endDate", "status", "organization.annualTurnover", "organization.type",
                    "organization.officialAddress.street"};
            mappingStrategy.setColumnMapping(columns);

            StatefulBeanToCsv<Worker> beanWriter = new StatefulBeanToCsvBuilder<Worker>(writer)
                    .withMappingStrategy(mappingStrategy).withSeparator(';').build();
            beanWriter.write(workers);
            logger.log(Level.INFO, "Data saved to file");

        } catch (FileNotFoundException e) {
            logger.log(Level.INFO, "No file access permissions");
        } catch (Exception e) {
            logger.log(Level.INFO, "Error saving CSV: " + e.getMessage());
        }
    }

    /**
     * The method reads employee data from a file in CSV format.
     *
     * @throws Exception
     *             If file system errors occurred.
     */
    public void readFile(IOProvider io) {
        try {
            try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(fileName));
                    InputStreamReader reader = new InputStreamReader(input)) {
                CsvToBean<Worker> csvReader = new CsvToBeanBuilder<Worker>(reader).withType(Worker.class)
                        .withSeparator(';').withIgnoreLeadingWhiteSpace(true).withIgnoreEmptyLine(false)
                        .withThrowExceptions(true).build();
                List<Worker> workers = csvReader.parse();
                HistoryWorker historyWorker = HistoryWorker.INSTANCE;

                workers.forEach(historyWorker::add);

            } catch (FileNotFoundException e) {
                io.printError("No file access permissions");
            } catch (Exception e) {
                io.printError("Error reading CSV" + e.getMessage());
            }
        } catch (Exception e) {
            io.printError("Error reading CSV" + e.getMessage());
        }
    }
}
