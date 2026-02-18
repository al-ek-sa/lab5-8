package edu.itmo.piikt.commands;

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
