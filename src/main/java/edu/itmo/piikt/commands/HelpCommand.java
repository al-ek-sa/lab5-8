package edu.itmo.piikt.commands;

import java.util.LinkedList;

//public class HelpCommand implements Command {
public class HelpCommand extends Commands{
    public HelpCommand(){
        super("help");
    }

    @Override
    public void execute() {
        System.out.println("    help : display help on available commands\n" +
                "    info : output information about the collection to stdout (type, initialization date, number of elements, etc.)\n" +
                "    show : output all collection elements in string representation to stdout\n" +
                "    add {element} : add a new element to the collection\n" +
                "    update id {element} : update the value of the collection element whose id is equal to the specified one\n" +
                "    remove_by_id id : remove an element from the collection by its id\n" +
                "    clear : clear the collection\n" +
                "    save : save the collection to a file\n" +
                "    execute_script file_name : read and execute a script from the specified file. The script contains commands in the same form as the user enters them in interactive mode\n" +
                "    exit : terminate the program (without saving to file)\n" +
                "    head : output the first element of the collection\n" +
                "    remove_lower {element} : remove all elements from the collection that are less than the specified one\n" +
                "    history : output the last 14 commands (without their arguments)\n" +
                "    count_by_organization organization : output the number of elements whose organization field value is equal to the specified one\n" +
                "    filter_contains_name name : output elements whose name field value contains the specified substring\n" +
                "    print_field_descending_end_date : output the values of the endDate field of all elements in descending order");
    }
}
