package edu.itmo.piikt.managers;

public enum Agreement {
    PLUS("+"),
    YES("yes"),
    Y("y"),
    ДА("да"),
    Д("д");

    private final String name;

    Agreement(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
