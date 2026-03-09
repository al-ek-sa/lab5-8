package edu.itmo.piikt.managers;

public enum NameIOProvider {
    CONSOLE("Console"), FILE("File");
    private final String name;
    NameIOProvider(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
