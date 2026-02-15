package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.models.Coordinates;
import edu.itmo.piikt.reader.InputReader;

public class ValidationCoordinates {
    private InputReader scanner;
    public ValidationCoordinates(){
        this.scanner = InputReader.getInstance();
    }


    public long validatorX(){
        System.out.println("Enter the first coordinate X (value must not exceed 10)");
        while(true){
            try{
                String input = scanner.nextLine().trim();
                long xConsole = Long.parseLong(input);
                if (xConsole <= 10) {
                    return xConsole;
                } else System.out.println("Invalid coordinate. X must not exceed 10");
                System.out.println("Enter the first coordinate X (value must not exceed 10)");
            } catch (RuntimeException e) {
                System.out.println("Invalid coordinate. X must not exceed 10");
                System.out.println("Enter the first coordinate X (value must not exceed 10)");
            }
        }
    }


    public long validatorY() {
        System.out.println("Enter the second coordinate Y (value must be greater than -644)");
        while (true) {
            try{
                String input = scanner.nextLine().trim();
                long yConsole = Long.parseLong(input);
                if (yConsole > -644) {
                    return yConsole;
                } else System.out.println("Incorrect coordinate. The Y value must be greater than -644");
                System.out.println("Enter the second coordinate Y (value must be greater than -644)");
            } catch (RuntimeException e) {
                System.out.println("Incorrect coordinate. The Y value must be greater than -644");
                System.out.println("Enter the second coordinate Y (value must be greater than -644)");
            }
        }
    }

    public Coordinates сoordinates() {
        return new Coordinates(validatorX(),validatorY());
    }
}