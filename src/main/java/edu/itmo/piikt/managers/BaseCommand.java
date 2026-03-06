package edu.itmo.piikt.managers;

public interface BaseCommand {

    default void before() {
    }
    default void after() {
    }
    default void onError(RuntimeException e) {
    }
}
