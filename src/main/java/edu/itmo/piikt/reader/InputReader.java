package edu.itmo.piikt.reader;
import java.util.Scanner;

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
        return scanner.nextLine();
    }

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