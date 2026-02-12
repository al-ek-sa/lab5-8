package edu.itmo.piikt.commands;

//public class AddCommand implements Command {
public class AddCommand extends Commands {
    public AddCommand(){
        super("add");
    }

    @Override
    public void execute() {
        System.out.println("работает");
    }

}
