package edu.itmo.piikt.server.history;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.models.Coordinates;

import java.util.ArrayList;
import java.util.List;

public enum HistoryCoordinate {
    INSTANCE;
    private final List<Coordinates> list = new ArrayList<>();
    private static final AppLogger logger = new AppLogger(HistoryCoordinate.class);

    public void add(Coordinates coordinates){
        try (Context ignored = Context.newId()) {
            logger.debug("Adding Coordinates: x={}, y={}", coordinates.getX(), coordinates.getY());
            var list1 = list.stream().filter(coordinates1 -> coordinates1.equals(coordinates)).toList();
            if(list1.isEmpty()) list.add(coordinates);
            logger.debug("Collection size: {}", list.size());
        } catch (Exception e) {
            logger.error("Error adding coordinate: {}", e);
            throw new RuntimeException(e);
        }
    }
}
