package edu.itmo.piikt.server.saveManager;

import com.opencsv.bean.*;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.common.models.Worker;
import java.io.*;
import java.util.List;

/**
 * The class implements parsing a collection containing employee data into CSV
 * format, saving to a file, and reading data from a file.
 *
 * @author Lishyk Aliaksandra
 * @version 1.2
 * @see CSVParser
 * @see HistoryWorker
 * @see ColumnPositionMappingStrategy
 * @see StatefulBeanToCsv
 */
public class CSVParser {
    private static final AppLogger logger = new AppLogger(CSVParser.class);
    private String fileName;

    public CSVParser() {
        this.fileName = System.getenv("WORKER_FILE");
        if (this.fileName == null || this.fileName.isEmpty()) {
            this.fileName = "workers.csv";
        }
        logger.info("CSVParser initialized, file: {}", fileName);
    }

    /**
     * The method implements parsing employee data with saving to a file.
     */
    public void saveCollection() {
        try (Context context = Context.newId()) {
            List<Worker> workers = HistoryWorker.INSTANCE.getListWorker();
            logger.info("Saving collection, size: {}", workers.size());
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
                logger.info("Data saved successfully to {}", fileName);
            } catch (FileNotFoundException e) {
                logger.error("No file access permissions: {}", e.getMessage());
            } catch (Exception e) {
                logger.error("Error saving CSV: {}", e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Error in saveCollection: {}", e.getMessage());
        }
    }

    /**
     * The method reads employee data from a file in CSV format.
     */
    public void readFile() {
        try (Context context = Context.newId()) {
            logger.info("Reading file: {}", fileName);
            try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(fileName));
                 InputStreamReader reader = new InputStreamReader(input)) {
                CsvToBean<Worker> csvReader = new CsvToBeanBuilder<Worker>(reader).withType(Worker.class)
                        .withSeparator(';').withIgnoreLeadingWhiteSpace(true).withIgnoreEmptyLine(false)
                        .withThrowExceptions(true).build();
                List<Worker> workers = csvReader.parse();
                HistoryWorker historyWorker = HistoryWorker.INSTANCE;
                workers.forEach(historyWorker::add);
                logger.info("Loaded {} workers from file", workers.size());
            } catch (FileNotFoundException e) {
                logger.warn("File not found: {}, will create new file on save", fileName);
            } catch (Exception e) {
                logger.error("Error reading CSV: {}", e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Error in readFile: {}", e.getMessage());
        }
    }
}