package edu.itmo.piikt.commands;

import edu.itmo.piikt.exception.ExceptionBigIntegerMAX_INTEGER;
import edu.itmo.piikt.exception.ExceptionBigIntegerMIN_INTEGER;
import edu.itmo.piikt.exception.ExceptionId;
import edu.itmo.piikt.exception.ExceptionNull;
import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.ArgumentCommand;

import java.math.BigInteger;

public class RemoveByIdCommand extends ArgumentCommand {
    private IOProvider io;
    public RemoveByIdCommand(IOProvider io){
        super("remove_by_id");
        this.io = io;
    }
    @Override
    public void execute(String argument) {
        try{

            BigInteger bigInteger = new BigInteger(argument);

            if (bigInteger.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
                throw new ExceptionBigIntegerMAX_INTEGER();
            }

            if (bigInteger.compareTo(BigInteger.valueOf(0)) <= 0) {
                throw new ExceptionId();
            }

            io.printeDesign();
            //удаление элемента по id началось
            io.printlnCommand("Deletion of item by ID started");
            io.printeDesign();

            int idConsole = Integer.parseInt(argument);
            HistoryWorker.getInstance(io).idMatches(argument);
            HistoryWorker.getInstance(io).removeId(idConsole);
            io.printeDesign();
            //элемент успешно удален
            io.printlnCommand("Item successfully deleted");
            io.printeDesign();
        } catch (ExceptionBigIntegerMAX_INTEGER e){
            io.printeDesign();
            io.printException(e.getMessage());
            io.printeDesign();
        } catch (ExceptionId e) {
            io.printeDesign();
            io.printException(e.getMessage());
            io.printeDesign();
        } catch (RuntimeException e) {
            io.printeDesign();
            //В аргументе введены посторонние символы, повторите команду (аргумент может содержать только целые числа больше 0)
            io.printException("Extraneous characters entered in the argument, repeat the command (the argument can only contain integers greater than 0)");
            io.printeDesign();
        }

    }
}
