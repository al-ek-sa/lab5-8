package edu.itmo.piikt.reader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * The class implements parsing a collection containing employee data into CSV format, saving to a file,
 * and reading data from a file.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */

public class InputReader {
    private BufferedReader reader;
    private static InputReader instance;
    private InputReader(){
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public static InputReader getInstance(){
        if (instance == null){
            InputReader inputReader = new InputReader();
            instance = inputReader;
        }
        return instance;
    }

    private BufferedReader newReader() {
        return new BufferedReader(new InputStreamReader(System.in));
    }


    /**
     *The method reads data from the console.
     *
     * @return data
     */
    
    public String nextLine() {
        String input = read();
        while (input == null) {
            this.reader = newReader();
            input = read();
        }
        return input;
    }

    private String read() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            return null;
        }
    }
}
