package edu.itmo.piikt.models;

public record Coordinates (long x, float y){
    @Override
    public String toString() {
        return " координата х: " + x + ", координата у: " + y;
    }
}
/**
public class Coordinates {
    private long x; //Максимальное значение поля: 10
    private float y; //Значение поля должно быть больше -644
    public Coordinates(long x, float y){
        this.x = x;
        this.y = y;
    }
}
*/