package edu.itmo.piikt.io;

public interface IOProvider {
    void print(String message);
    void println(String message);
    void printException(String message);
    void printError(String message);
    String readLIne();
}
