package edu.itmo.piikt.managers;

public abstract class Commands {
    private String name;
    public Commands (String name) {
        this.name = name;
    }

    public String getName(){
        return  name;
    }

    public abstract void execute();
}
