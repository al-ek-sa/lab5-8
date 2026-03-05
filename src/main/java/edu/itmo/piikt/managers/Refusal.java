package edu.itmo.piikt.managers;

public enum Refusal {
    MINES("-"), NO("no"), N("n"), НЕТ("нет"), Н("н");

    private final String name;

    Refusal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
