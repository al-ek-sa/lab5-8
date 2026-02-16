package edu.itmo.piikt.models;

public enum Status {
    FIRED(1),
    HIRED(2),
    RECOMMENDED_FOR_PROMOTION(3),
    PROBATION(4);
    private final int id;
    Status(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
