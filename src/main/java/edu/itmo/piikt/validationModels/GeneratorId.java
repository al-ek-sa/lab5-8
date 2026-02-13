package edu.itmo.piikt.validationModels;

public class GeneratorId {
    private static GeneratorId instance;
    private int startId;

    private GeneratorId(){
        this.startId = 1; /**потом изменить на значение из файла, если файл пустой, то будет начинаться с 1*/
    }

    public static GeneratorId getInstance(){
        if (instance == null) {
            instance = new GeneratorId();
        } return  instance;
    }

    public int getId(){ /**многопотчность страдает*/
        return  startId++;
    }
}