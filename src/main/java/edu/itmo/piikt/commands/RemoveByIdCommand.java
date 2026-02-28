package edu.itmo.piikt.commands;

import edu.itmo.piikt.exception.ExceptionBigIntegerMAX_INTEGER;
import edu.itmo.piikt.exception.ExceptionBigIntegerMIN_INTEGER;
import edu.itmo.piikt.exception.ExceptionId;
import edu.itmo.piikt.exception.ExceptionNull;
import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.ArgumentCommand;

import java.math.BigInteger;
import java.util.logging.Logger;import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command remove_by_id id : remove an element from the collection by its id.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class RemoveByIdCommand extends ArgumentCommand {
    private IOProvider io;
    Logger logger = Logger.getLogger(RemoveByIdCommand.class.getName());
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

            int idConsole = Integer.parseInt(argument);
            io.printeDesign();
            //удаление элемента по id началось
            logger.log(Level.INFO,"Deletion of item by ID started");
            io.printeDesign();
            HistoryWorker.getInstance(io).idMatches(argument);
            HistoryWorker.getInstance(io).removeId(idConsole);
        } catch (ExceptionBigIntegerMAX_INTEGER e){
            io.printeDesign();
            logger.log(Level.INFO,e.getMessage());
            io.printeDesign();
        } catch (ExceptionId e) {
            io.printeDesign();
            logger.log(Level.INFO,e.getMessage());
            io.printeDesign();
        } catch (RuntimeException e) {
            io.printeDesign();
            //В аргументе введены посторонние символы, повторите команду (аргумент может содержать только целые числа больше 0)
            logger.log(Level.INFO,"Extraneous characters entered in the argument, repeat the command (the argument can only contain integers greater than 0)");
            io.printeDesign();
        }

    }
}
