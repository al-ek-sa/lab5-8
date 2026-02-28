package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;

/**
 *The class generates unique IDs for employees.
 *The generator is a singleton. The generation is not thread-safe.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class GeneratorId {
    private IOProvider io;
    private static GeneratorId instance;
    private int startId;

    private GeneratorId(IOProvider io){

        /**
         * The initial ID is set when the program starts.
         * If the employee data file is empty, it is set to 1 by default;
         * otherwise, the ID of the last employee in the file is taken and incremented by 1.
         */

        this.startId = HistoryWorker.getInstance(io).tailWorked() + 1;
        this.io = io;
    }

    public static GeneratorId getInstance(IOProvider io){
        if (instance == null) {
            instance = new GeneratorId(io);
        } return  instance;
    }

    /**
     *The getter returns the ID and then increments it by 1.
     * @return id
     */
    public int getId(){
        return  startId++;
    }

    public void setStartId(int startId) {
        this.startId = startId;
    }
}