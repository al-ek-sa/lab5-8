package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.Coordinates;

public class ValidationCoordinates {
    private IOProvider io;
    public ValidationCoordinates(IOProvider io){
        this.io =io;
    }


    public long validatorX() {

        if (io.name().equals("File")) {
            try {
                String input = io.readLine().trim();
                long xConsole = Long.parseLong(input);
                if (xConsole <= 10) {
                    return xConsole;
                    //Введенное значение некорректно. Х не должно превышать 10
                } else {throw  new RuntimeException("Invalid coordinate. X must not exceed 10");}
            } catch (RuntimeException e) {
                throw new RuntimeException("ОБРАТИТЬ ВНИМАНИЕ МОЖЕТ БЫТЬ СТРОКОЙ А МОЖЕТ БЫТЬ NULL");
            }
        }


        if (io.name().equals("Console")){
            while (true) {
                try {
                    //Введите первую координату(значение не должно превышать 10, поле обязательно к заполнению)
                    io.printField("Enter the first coordinate X", "(value must not exceed 10, required field)");
                    String input = io.readLine().trim();
                    long xConsole = Long.parseLong(input);
                    if (xConsole <= 10) {
                        return xConsole;
                        //Введенное значение некорректно. Х не должно превышать 10
                    } else io.printException("Invalid coordinate. X must not exceed 10");
                } catch (RuntimeException e) {
                    io.printException("ОБРАТИТЬ ВНИМАНИЕ МОЖЕТ БЫТЬ СТРОКОЙ А МОЖЕТ БЫТЬ NULL");
                }
            }
        } else {throw new RuntimeException("поле не сохранилось");}
    }


    public long validatorY() {

        if (io.name().equals("File")) {
            try {
                String input = io.readLine().trim();
                long yConsole = Long.parseLong(input);
                if (yConsole > -644) {
                    return yConsole;
                } //Значение введено некорректно, значение должно быть больше -644
                else {
                    throw new RuntimeException("Incorrect coordinate. The Y value must be greater than -644");
                }
            } catch (RuntimeException e) {
                throw new RuntimeException("Строка содержит буквы");
            }}

        if (io.name().equals("Console")){
            while (true) {
                try {
                    //Введите вторую координату У (значение должно быть больше -644, поле обязательно к заполнению)
                    io.printField("Enter the second coordinate Y", "(value must be greater than -644, required field)");
                    String input = io.readLine().trim();
                    long yConsole = Long.parseLong(input);
                    if (yConsole > -644) {
                        return yConsole;
                    } //Значение введено некорректно, значение должно быть больше -644
                    else io.printException("Incorrect coordinate. The Y value must be greater than -644");
                } catch (RuntimeException e) {
                    io.printException("Строка содержит буквы");
                }
            }
        } else {
            throw new RuntimeException("поле не сохранилось");
        }
    }

    public Coordinates сoordinates() {
        return new Coordinates(validatorX(),validatorY());
    }
}