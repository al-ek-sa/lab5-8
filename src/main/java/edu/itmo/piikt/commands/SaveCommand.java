package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOProvider;

public class SaveCommand extends Commands {
    private IOProvider io;
    public SaveCommand(IOProvider io){
        super("save");
        this.io = io;
    }
    @Override
    public void execute() {

    }
}
