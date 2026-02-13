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
        long xConsole = scanner.nextLong();
        if (xConsole <= 10) {
            return xConsole;
        } return validatorX();
    }

    public long validatorY(){
        long yConsole = scanner.nextLong();
        if (yConsole > -644) {
            return yConsole;
        } return validatorY();
    }

    public Coordinates validationCoordinates() {
        return new Coordinates(validatorX(),validatorY());
    }
}