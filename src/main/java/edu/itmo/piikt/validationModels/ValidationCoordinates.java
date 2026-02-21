package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.exception.*;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.Coordinates;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ValidationCoordinates {
    private IOProvider io;
    public ValidationCoordinates(IOProvider io){
        this.io =io;
    }


    public Long validatorX() {

        if (io.name().equals("File")) {
            try {
                String input = io.readLine().trim();
                long xConsole = Long.parseLong(input);
                if (xConsole <= 10) {
                    return xConsole;
                } else {
                    throw new ExceptionCoordinataX();
                }
            } catch (ExceptionCoordinataX e){
                e.getMessage();
            } catch (RuntimeException e) {
                throw new RuntimeException("The string contains symbols, please try again");
            }
        }


        if (io.name().equals("Console")){
            while (true) {
                try {
                    //Введите первую координату(значение не должно превышать 10, поле обязательно к заполнению)
                    io.printField("Enter the first coordinate X", "(value must not exceed 10, required field)");
                    String input = io.readLine().trim();

                    if (input.equals("null") || input.trim().isEmpty()) {
                        throw new ExceptionNull();
                    }

                    BigInteger bigInteger = new BigInteger(input);

                    if (bigInteger.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) == 1) {
                        throw new ExceptionCoordinataX();
                    }

                    if (bigInteger.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) == -1) {
                        throw new ExceptionBigIntegerMIN_LONG();
                    }

                    long xConsole = Long.parseLong(input);


                    if (xConsole <= 10) {
                        return xConsole;
                        //Введенное значение некорректно. Х не должно превышать 10
                    } else {
                        throw new ExceptionCoordinataX();
                    }
                } catch (ExceptionNull e) {
                    io.printException(e.getMessage());
                } catch (ExceptionBigIntegerMAX_LONG e) {
                    io.printException(e.getMessage());
                }catch (ExceptionBigIntegerMIN_LONG e){
                    io.printException(e.getMessage());
                }catch (ExceptionCoordinataX e) {
                    io.printException(e.getMessage());
                } catch (RuntimeException e){
                    io.printException("The string contains symbols, please try again");
                }
            }
        } else {
            //неизвестный тип чтения
            throw new RuntimeException("Unknown reading type");
        }
    }


    public Float validatorY() {

        if (io.name().equals("File")) {
            try {
                String input = io.readLine().trim();
                String inputFloat = input.replace(',', '.');
                float yConsole = Float.parseFloat(inputFloat);
                if (yConsole > -644) {
                    return yConsole;
                } //Значение введено некорректно, значение должно быть больше -644
                else {
                    throw new RuntimeException("Incorrect coordinate. The Y value must be greater than -644");
                }
            } catch (RuntimeException e) {
                throw new RuntimeException("The string contains symbols, please try again");
            }}

        if (io.name().equals("Console")){
            while (true) {
                try {
                    //Введите вторую координату У (значение должно быть больше -644, поле обязательно к заполнению)
                    io.printField("Enter the second coordinate Y", "(value must be greater than -644, required field)");
                    String input = io.readLine().trim();

                    if (input.equals("null") || input.trim().isEmpty()){
                        throw new ExceptionNull();
                    }

                    String inputFloat = input.replace(',', '.');

                    BigDecimal bigDecimal = new BigDecimal(inputFloat);

                    if (bigDecimal.compareTo(BigDecimal.valueOf(Float.MAX_VALUE)) == 1) {
                        throw new ExceptionBigDecimalMAX_FLOAT();
                    }


                    if (bigDecimal.compareTo(BigDecimal.valueOf(Float.MIN_VALUE)) == -1){
                        throw new ExceptionCoordinateY();
                    }

                    float yConsole = Float.parseFloat(inputFloat);
                    if (yConsole > -644) {
                        return yConsole;
                    } else {
                        throw new ExceptionCoordinateY();
                    }
                }catch (ExceptionNull e) {
                    io.printException(e.getMessage());
                } catch (ExceptionBigDecimalMAX_FLOAT e) {
                    io.printException(e.getMessage());
                } catch (ExceptionCoordinateY e) {
                    io.printException(e.getMessage());
                } catch (RuntimeException e) {
                    io.printException("The string contains symbols, please try again");
                }
            }
        } else {
            throw new RuntimeException("Unknown reading type");
        }
    }

    public Coordinates сoordinates() {
        return new Coordinates(validatorX(),validatorY());
    }
}