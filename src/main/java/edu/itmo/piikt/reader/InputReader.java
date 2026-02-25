package edu.itmo.piikt.reader;
import java.util.Scanner;

/**
 * The class implements parsing a collection containing employee data into CSV format, saving to a file,
 * and reading data from a file.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class InputReader {
    private Scanner scanner;
    private static InputReader instance;
    private InputReader(){
        this.scanner = new Scanner(System.in);
    }

    public static InputReader getInstance(){
        if (instance == null){
            InputReader inputReader = new InputReader();
            instance = inputReader;
        }
        return instance;
    }

    public String nextLine() {
            String input;
            if (scanner.hasNext()) {
                input = scanner.nextLine();
            } else {
                instance = null;
                input = null;
            }
            if (input == null){
                getInstance();
            }
            return input;
    }

   /** public String nextLine() {
        String input;
        if (scanner.hasNext()) {
            input = scanner.nextLine();
        } else {
            instance = null;
            input = null;
        }
        if (input == null){
            getInstance();
        }
        return input;
    }*/

    /**
     *The method reads data from the console.
     *
     * @return data
     */



    public int nextInt() {
        return scanner.nextInt();
    }

    public long nextLong(){
        return  scanner.nextLong();
    }

    public  float nextFloat(){
        return scanner.nextFloat();
    }
}


/**public class InputReader {
    private Scanner scanner;
    /**private static InputReader instance;*/
    /**private InputReader(){
        this.scanner = new Scanner(System.in);
    }*/

    /**public static InputReader getInstance(){
        if (instance == null){
            InputReader inputReader = new InputReader();
            instance = inputReader;
        }
        return instance;
    }

    public String nextLine() {
        if (scanner.hasNext()) {
            return scanner.nextLine();
        } else {
            scanner.close();
            Scanner scanner1 = new Scanner(System.in);
            return "";
        }
    }*/

    /**
     *The method reads data from the console.
     *
     * @return data
     */



    /**public int nextInt() {
        return scanner.nextInt();
    }

    public long nextLong(){
        return  scanner.nextLong();
    }

    public  float nextFloat(){
        return scanner.nextFloat();
    }
}*/