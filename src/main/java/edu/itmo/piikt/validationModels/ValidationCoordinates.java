package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.Coordinates;

public class ValidationCoordinates {
    private IOProvider io;
    public ValidationCoordinates(IOProvider io){
        this.io =io;
    }


    public long validatorX(){
        System.out.println("Enter the first coordinate X (value must not exceed 10)");
        while(true){
            try{
                io.print("Enter the first coordinate X ");
                io.println("(value must not exceed 10, required field)");
                String input = io.readLine().trim();
                long xConsole = Long.parseLong(input);
                if (xConsole <= 10) {
                    return xConsole;
                } else io.printException("Invalid coordinate. X must not exceed 10");
            } catch (RuntimeException e) {
                io.printException("ОБРАТИТЬ ВНИМАНИЕ МОЖЕТ БЫТЬ СТРОКОЙ А МОЖЕТ БЫТЬ NULL");
            }
        }
    }


    public long validatorY() {
        while (true) {
            try {
                io.print("Enter the second coordinate Y ");
                io.println("(value must be greater than -644, required field)");
                String input = io.readLine().trim();
                long yConsole = Long.parseLong(input);
                if (yConsole > -644) {
                    return yConsole;
                } else io.printException("Incorrect coordinate. The Y value must be greater than -644");
            }catch (NullPointerException e){
                io.printException("Строка не заполнена");
            } catch (RuntimeException e) {
                io.printException("Строка содержит буквы");
            }
        }
    }

    public Coordinates сoordinates() {
        io.println("All coordinate fields have been successfully filled");
        return new Coordinates(validatorX(),validatorY());
    }
}