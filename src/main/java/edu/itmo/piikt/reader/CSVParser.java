package edu.itmo.piikt.reader;

import com.opencsv.CSVReader;
import com.opencsv.bean.*;
import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.Worker;
import java.io.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * The class implements parsing a collection containing employee data into CSV format, saving to a file,
 * and reading data from a file.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

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

    /**
     *The method implements parsing employee data with saving to a file.
     *
     * @throws Exception If file system errors occurred.
     */

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
                    "creationDate",
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

            //данные сохраннены в файл
            io.printlnCommand("Data saved to file");
            io.printeDesign();


        } catch (FileNotFoundException e) {
            io.printError("нет прав доступа в файл");
        } catch (Exception e) {
            io.printError("Error saving CSV: " + e.getMessage());
        }
    }

    /**
     *The method reads employee data from a file in CSV format.
     *
     * @throws Exception If file system errors occurred.
     */
    public void readFile(){
        try{
            try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(fileName));
                 InputStreamReader reader = new InputStreamReader(input)) {
                 CsvToBean<Worker> csvReader = new CsvToBeanBuilder<Worker>(reader)
                         .withType(Worker.class)
                         .withSeparator(';')
                         .withIgnoreLeadingWhiteSpace(true)
                         .withIgnoreEmptyLine(true)
                         .withThrowExceptions(false)
                         .build();
                 List<Worker> workers = csvReader.parse();
                 HistoryWorker historyWorker = HistoryWorker.getInstance(io);
                 for(Worker worker : workers) {
                     historyWorker.add(worker);
                 }

            } catch (FileNotFoundException e) {
                io.printError("нет прав доступа в файл");
            } catch (Exception e) {
                io.printError("Error reading CSV" + e.getMessage());
            }
        }catch (Exception e) {
            io.printError("Error reading CSV" + e.getMessage());
        }
    }
}