package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;

/**
 * The class implements the command help_entering_command : display help on entering available commands
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class HelpEnteringCommand extends Commands {
    private IOProvider io;
    public HelpEnteringCommand(IOProvider io){
        super("help_entering_command");
        this.io =io;
    }

    @Override
    public void execute() {
        io.printeDesign();
        io.println("   >>> help: Both in the console and in the script, the command is entered as a single word without arguments. (\"help\") \n" +
                "   >>> info: Both in the console and in the script, the command is entered as a single word without arguments. (\"info\")\n"+
                "   >>> show: Both in the console and in the script, the command is entered as a single word without arguments. (\"show\")\n" +
                "   >>> add: In the console, only the command is entered without arguments (\"add\"). \n    In script mode, the data can be entered either as a single CSV line \n    (add {\"name\";\"x\";\"y\";\"salary\";\"startDate\";\"endDate\";status\";\"annual turnover\";\"organization type\";\"street\"}), or line by line in the same order.\n" +
                "   >>> update: In the console, the command is entered together with the id on one line\n   (\"update _____\"). In the script, the command is entered together with the\n    id on one line (\"update _____\"), and then the data is entered line by line, in the same order as the add command.\n" +
                "   >>> remove_by_id: Both in the console and in the script, the command is entered and \n    then the id is specified on the same line. (\"remove_by_id _____\")\n" +
                "   >>> clear: Both in the console and in the script, the command is entered as a single word without arguments.(\"clear\")\n" +
                "   >>> save: Both in the console and in the script, the command is entered as a single word without arguments. (\"save\")\n" +
                "   >>> execute_script: Both in the console and in the script, the command is entered on one line (\"execute_script _____\").\n" +
                "   >>> exit: Both in the console and in the script, the command is entered as a single word without arguments.(\"exit\")\n" +
                "   >>> head: Both in the console and in the script, the command is entered as a single word without arguments. (\"head\")\n" +
                "   >>> remove_lower: Both in the console and in the script, the command is entered and then the id is specified on the same line. (\"remove_lower _____\")\n"+
                "   >>> history: Both in the console and in the script, the command is entered as a single word without arguments. (\"history\")\n" +
                "   >>> count_by_organization: In the console, only the command name is entered\n    (\"count_by_organization\"), while in the script, the command is entered, and\n    then the data is entered line by line in the following order (annual turnover; type; street).\n" +
                "   >>> filter_contains_name: Both in the console and in the script, the command is entered, and then the employee's name is entered on the same line. (\"filter_contains_name _____\"). \n" +
                "   >>> print_field_descending_end_date: Both in the console and in the script, the command is entered as a single \n    word without arguments.(\"print_field_descending_end_date\")\n" +
                "   >>> help_entering_command: Both in the console and in the script, the command is entered as a single word without arguments. (\"help_entering_command\")\n" +
                "   >>> ATTENTION! When writing a script, the last line in the file must remain empty.");
        io.printeDesign();
    }
}
