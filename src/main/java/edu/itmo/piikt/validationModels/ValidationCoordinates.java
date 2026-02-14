package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.models.Address;
import edu.itmo.piikt.models.Coordinates;
import edu.itmo.piikt.reader.InputReader;

public class ValidationCoordinates {
    private InputReader scanner;
    public ValidationCoordinates(){
        this.scanner = InputReader.getInstance();
    }

    public long validatorX(){
        System.out.println("Первая координата Х");
        long xConsole = scanner.nextLong();
        if (xConsole <= 10) {
            return xConsole;
        }
        System.out.println("Неверное значение, х не должно превышать 10");
        return validatorX();
    }

    public long validatorY(){
        System.out.println("Введите вторую координату Y");
        long yConsole = scanner.nextLong();
        if (yConsole > -644) {
            return yConsole;
        }
        System.out.println("Координата введена неверно, Y должно быть больше -644");
        return validatorY();
    }

    public Coordinates validationCoordinates() {
        return new Coordinates(validatorX(),validatorY());
    }
}