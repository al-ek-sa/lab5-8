package edu.itmo.piikt.managers;

/**
 * An abstract class that is the common parent for commands with arguments.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public abstract class ArgumentCommand {
    private String name;
    public ArgumentCommand (String name) {
        this.name = name;
    }

    public String getName(){
        return  name;
    }

    public abstract void execute(String argument);
}
