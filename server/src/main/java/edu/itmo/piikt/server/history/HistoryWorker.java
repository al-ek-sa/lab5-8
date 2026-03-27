package edu.itmo.piikt.server.history;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.models.Worker;
import lombok.Getter;

import java.util.*;

/**
 * A class for storing a collection with registered employees. The class is a
 * singleton.
 *
 * @author Lishyk Aliaksandra
 * @version 2.2
 */
@Getter
public enum HistoryWorker {
    INSTANCE;

    private static final AppLogger logger = new AppLogger(HistoryWorker.class);
    private Date data;
    private LinkedList<Worker> listWorker = new LinkedList<>();

    HistoryWorker() {
        this.data = new Date();
    }

    public void add(Worker worker) {
        try (Context context = Context.newId()) {
            logger.debug("Adding worker: id={}, name={}", worker.getUuid(), worker.getName());
            listWorker.add(worker);
            logger.debug("Collection size: {}", listWorker.size());
        } catch (Exception e) {
            logger.error("Error adding worker: {}", e);
            throw new RuntimeException(e);
        }
    }

    public void clear() {
        try (Context context = Context.newId()) {
            logger.info("Clearing collection, size before: {}", listWorker.size());
            listWorker.clear();
            logger.info("Collection cleared");
        } catch (Exception e) {
            logger.error("Error clearing collection: {}", e);
            throw new RuntimeException(e);
        }
    }
}