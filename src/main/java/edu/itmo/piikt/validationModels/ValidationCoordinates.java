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
 * @version 2.0
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
        while (true) {
            try {
                io.printField("Enter the first coordinate X", "(value must not exceed 10, required field)");
                String input = io.readLine().trim();
                if (input.equals("null") || input.trim().isEmpty()) {
                    if (io.name().equals("File")) {
                        throw new RuntimeException("_____________________");
                    } else {
                        throw new ExceptionNull();
                    }
                }
                BigInteger bigInteger = new BigInteger(input);
                if (bigInteger.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
                    if (io.name().equals("File")) {
                        throw new RuntimeException("_____________________");
                    } else {
                        throw new ExceptionCoordinataX();
                    }
                }
                if (bigInteger.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0) {
                    if (io.name().equals("File")) {
                        throw new RuntimeException("_____________________");
                    } else {
                        throw new ExceptionBigIntegerMIN_LONG();
                    }
                }
                long xConsole = Long.parseLong(input);
                if (xConsole <= 10) {
                    return xConsole;
                } else {
                    if (io.name().equals("File")) {
                        throw new RuntimeException("_____________________");
                    } else {
                        throw new ExceptionCoordinataX();
                    }
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
                if (io.name().equals("File")) {
                    throw new RuntimeException();
                } else {
                    io.printException("The string contains symbols, please try again");
                }
            }
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
        while (true) {
            try {
                io.printField("Enter the second coordinate Y", "(value must be greater than -644, required field)");
                String input = io.readLine().trim();
                if (input.equals("null") || input.trim().isEmpty()) {
                    if (io.name().equals("File")) {
                        throw new RuntimeException("_____________________");
                    } else {
                        throw new ExceptionNull();
                    }
                }
                String inputFloat = input.replace(',', '.');
                BigDecimal bigDecimal = new BigDecimal(inputFloat);
                if (bigDecimal.compareTo(BigDecimal.valueOf(Float.MAX_VALUE)) > 0) {
                    if (io.name().equals("File")) {
                        throw new RuntimeException("_____________________");
                    } else {
                        throw new ExceptionBigDecimalMAX_FLOAT();
                    }
                }
                float yConsole = Float.parseFloat(inputFloat);
                if (yConsole > -644) {
                    return yConsole;
                } else {
                    if (io.name().equals("File")) {
                        throw new RuntimeException("_____________________");
                    } else {
                        throw new ExceptionCoordinateY();
                    }
                }
            } catch (ExceptionNull e) {
                io.printException(e.getMessage());
            } catch (ExceptionBigDecimalMAX_FLOAT e) {
                io.printException(e.getMessage());
            } catch (ExceptionCoordinateY e) {
                io.printException(e.getMessage());
            } catch (RuntimeException e) {
                if (io.name().equals("File")) {
                    throw new RuntimeException();
                } else {
                    io.printException("The string contains symbols, please try again");
                }
            }
        }
    }

    /**
     *The method returns a Coordinates object with validated fields.
     *
     * @return Coordinates
     */
    public Coordinates coordinates() {
        return new Coordinates(validatorX(),validatorY());
    }
}