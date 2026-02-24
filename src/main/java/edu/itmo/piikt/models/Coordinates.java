package edu.itmo.piikt.models;

import com.opencsv.bean.CsvBindByPosition;

/**
 * The class of the Coordinates type object.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class Coordinates {
    @CsvBindByPosition(position = 2)
    private long x;
    @CsvBindByPosition(position = 3)
    private float y;
    public Coordinates(long x, float y){
        this.x = x;
        this.y = y;
    }

    public Coordinates() {}

    public void setY(float y) {
        this.y = y;
    }

    public void setX(long x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public long getX() {
        return x;
    }

    /**
     * Returns a brief description of this Coordinates. The exact details
     * of the representation are unspecified and subject to change,
     * but the following may be regarded as typical:
     *
     * "coordinate х: x, coordinate у: y"
     */

    @Override
    public String toString() {
        return " coordinate х: " + x + ", coordinate у: " + y;
    }
}
