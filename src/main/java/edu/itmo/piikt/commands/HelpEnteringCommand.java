package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;

public class HelpEnteringCommand extends Commands {
    private IOProvider io;
    public HelpEnteringCommand(IOProvider io){
        super("help_entering_command");
        this.io =io;
    }

    @Override
    public void execute() {
        io.printeDesign();
        io.println("   >>> help: In both console and script modes, the command is entered without arguments (\"help\") \n" +
                "   >>> info: In both console and script modes, the command is entered without arguments (\"info\")\n"+
                "   >>> show: In both console and script modes, the command is entered without arguments (\"show\")\n" +
                "   >>> add: In the console, only the command is entered without arguments (\"add\"). \nIn script mode, the data can be entered either as a single CSV line \n(add {\"name\";\"x\";\"y\";\"salary\";\"startDate\";\"endDate\";status\";\"annual turnover\";\"organization type\";\"street\"}), or line by line in the same order.\n" +
                "   >>> update: In both console and script modes, the command is entered on a single line with one argument - the employee's personal ID (\"update id _______\")\n" +
                "   >>> remove_by_id: In both console and script modes, the command is entered on a single line with one argument - the employee's personal ID (\"remove_by_id _______\")\n" +
                "   >>> clear: \n" +
                "   >>> save: In both console and script modes, the command is entered without arguments (\"save\")\n" +
                "   >>> execute_script: \n" +
                "   >>> exit: \n" +
                "   >>> head: In both console and script modes, the command is entered without arguments (\"head\")\n" +
                "   >>> remove_lower: \n"+
                "   >>> history: \n" +
                "   >>> count_by_organization: \n" +
                "   >>> filter_contains_name \n" +
                "   >>> print_field_descending_end_date: In both console and script modes, the command is entered without \narguments (\"print_field_descending_end_date\")\n" +
                "   >>> help_entering_command: ");
        io.printeDesign();
    }
}

/**
 *help и info и show: и в консоли, и в скрипте вводиться просто команда без аргументов (help)
add: В консоли вводится только команда без аргументов ("add").
 В скрипте вводится либо в одну строку в формате csv (add {"name";"x";"y";"salary";"startDate";"endDate";status";"annual turnover";"organization type";"street"),
 либо вводятся с новой строки в том же порядке.
 update id: и в консоли, и в скрипте команда вводиться в одну строку с одним аргументом. В аргумент передаеться личный id работника("update id _______").
 remove_by_id:и в консоли, и в скрипте команда вводиться в одну строку с одним аргументом. В аргумент передаеться личный id работника("remove_by_id _______").

 */