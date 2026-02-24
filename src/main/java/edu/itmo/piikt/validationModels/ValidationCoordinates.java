package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.exception.*;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.Coordinates;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * The class generates Coordinates with the specified fields:
 *
 * <ul>
 * <li>private long x; //Maximum field value: 10</li>
 * <li>private float y; //The field value must be greater than -644</li>
 * </ul>
 *
 * <p>The class provides methods that validate the field values.</p>
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class ValidationCoordinates {
    private IOProvider io;
    public ValidationCoordinates(IOProvider io){
        this.io =io;
    }

    /**
     *The method validates the X field value.
     *
     * @throws RuntimeException The method may throw an exception if the reading type is unknown.
     * @throws ExceptionCoordinataX If the X value entered in the file is greater than 10,
     * or if the value entered in the console exceeds the long range or is greater than 10.
     * @throws ExceptionNull When no value is entered into the console.
     * @throws ExceptionBigIntegerMIN_LONG If the entered value is less than the minimum value of the Float type.
     * @throws RuntimeException If the entered value could not be parsed into the Long type.
     * @return x
     */
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

                    if (bigInteger.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
                        throw new ExceptionCoordinataX();
                    }

                    if (bigInteger.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0) {
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

    /**
     *The method validates the Y field value.
     *
     * @throws RuntimeException The method may throw an exception if the reading type is unknown.
     * @throws RuntimeException If the value entered in the file does not match the expected value or nothing is entered.
     * @throws ExceptionNull When no value is entered into the console.
     * @throws ExceptionBigDecimalMAX_FLOAT If the value entered in the console exceeds the maximum value of the Float type.
     * @throws ExceptionCoordinateY If the value entered in the console either exceeds the minimum value of the Float type
     * or the value is less than or equal to -644.
     * @throws RuntimeException Error parsing the value entered in the console into the Float type.
     * @return x
     */

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

    /**
     *The method returns a Coordinates object with validated fields.
     *
     * @return Coordinates
     */
    public Coordinates сoordinates() {
        return new Coordinates(validatorX(),validatorY());
    }
}