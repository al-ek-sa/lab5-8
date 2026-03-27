package edu.itmo.piikt.common.logger;

import org.slf4j.MDC;

import java.util.UUID;

public class Context implements  AutoCloseable {
    private static final String  KEY = "id";

    public Context() {
        this(UUID.randomUUID().toString());
    }

    public Context(String id) {
        MDC.put(KEY, id);
    }

    @Override
    public void  close() {
        MDC.remove(KEY);
    }

    public static String getId() {
        return  MDC.get(KEY);
    }

    public static Context newId() {
        return new Context();
    }

    public static Context withId(String id) {
        return new Context(id);
    }
}
