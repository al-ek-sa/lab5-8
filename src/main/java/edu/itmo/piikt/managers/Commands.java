package edu.itmo.piikt.managers;

/**
 An abstract class that is the common parent for commands without arguments.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

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
