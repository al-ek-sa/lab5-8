package edu.itmo.piikt.models;

import com.opencsv.bean.CsvBindByPosition;

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

    @Override
    public String toString() {
        return " coordinate х: " + x + ", coordinate у: " + y;
    }
}
