package edu.itmo.piikt.commands;

import java.util.LinkedList;

//public class HelpCommand implements Command {
public class HelpCommand extends Commands{
    public HelpCommand(){
        super("help");
    }

    @Override
    public void execute() {
        System.out.println("    help : вывести справку по доступным командам\n" +
                "    info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                "    show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "    add {element} : добавить новый элемент в коллекцию\n" +
                "    update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                "    remove_by_id id : удалить элемент из коллекции по его id\n" +
                "    clear : очистить коллекцию\n" +
                "    save : сохранить коллекцию в файл\n" +
                "    execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                "    exit : завершить программу (без сохранения в файл)\n" +
                "    head : вывести первый элемент коллекции\n" +
                "    remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный\n" +
                "    history : вывести последние 14 команд (без их аргументов)\n" +
                "    count_by_organization organization : вывести количество элементов, значение поля organization которых равно заданному\n" +
                "    filter_contains_name name : вывести элементы, значение поля name которых содержит заданную подстроку\n" +
                "    print_field_descending_end_date : вывести значения поля endDate всех элементов в порядке убывания");
    }
}
