package edu.itmo.piikt.models;

/**
 * The Enum class contains instances of possible statuses.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

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
