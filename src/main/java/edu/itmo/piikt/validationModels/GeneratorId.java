package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;

public class GeneratorId {
    private IOProvider io;
    private static GeneratorId instance;
    private int startId;

    private GeneratorId(IOProvider io){
        this.startId = HistoryWorker.getInstance(io).tailWorked() + 1;
        this.io = io;
    }

    public static GeneratorId getInstance(IOProvider io){
        if (instance == null) {
            instance = new GeneratorId(io);
        } return  instance;
    }

    public int getId(){ /**многопотчность страдает*/
        return  startId++;
    }

    public void setStartId(int startId) {
        this.startId = startId;
    }
}