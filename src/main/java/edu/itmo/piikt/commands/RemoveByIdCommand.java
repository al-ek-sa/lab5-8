package edu.itmo.piikt.commands;

import edu.itmo.piikt.exception.ExceptionBigIntegerMAX_INTEGER;
import edu.itmo.piikt.exception.ExceptionId;
import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command remove_by_id id : remove an element from the
 * collection by its id.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class RemoveByIdCommand {
    Logger logger = Logger.getLogger(RemoveByIdCommand.class.getName());

    public RemoveByIdCommand() {
    }

    public void execute(IOProvider io, String argument) {
        try {

            BigInteger bigInteger = new BigInteger(argument);

            if (bigInteger.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
                throw new ExceptionBigIntegerMAX_INTEGER();
            }

            if (bigInteger.compareTo(BigInteger.valueOf(0)) <= 0) {
                throw new ExceptionId();
            }

            int idConsole = Integer.parseInt(argument);
            logger.log(Level.INFO, "Deletion of item by ID started");
            HistoryWorker.getInstance(io).idMatches(argument);
            HistoryWorker.getInstance(io).removeId(idConsole);
        } catch (ExceptionBigIntegerMAX_INTEGER e) {
            logger.log(Level.INFO, e.getMessage());
        } catch (ExceptionId e) {
            logger.log(Level.INFO, e.getMessage());
        } catch (RuntimeException e) {
            logger.log(Level.INFO,
                    "Extraneous characters entered in the argument, repeat the command (the argument can only contain integers greater than 0)");
        }
    }
}
