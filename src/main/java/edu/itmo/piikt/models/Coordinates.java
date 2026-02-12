package edu.itmo.piikt.models;

public class Coordinates {
    private long x; //Максимальное значение поля: 10
    private float y; //Значение поля должно быть больше -644
    public Coordinates(long x, float y){
        this.x = validationX();
        this.y = validationsY();
    }

    private long validationX(){
        if (x>10){
            System.out.println("Значение больше 10: х =" + x);
        }
        return x;
    }

    private float validationsY(){
        if (y<=-644) {
            System.out.println("Значение y меньше либо равно -644: y = " + y);
        } return y;
    }
}
