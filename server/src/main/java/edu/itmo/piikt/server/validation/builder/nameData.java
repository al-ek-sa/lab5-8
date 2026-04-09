package edu.itmo.piikt.server.validation.builder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum nameData {
    X("х"), Y("у"), NAME("name"), COORDINATES("coordinates"), SALARY("salary"), START_DATE("startDate"), STATUS(
            "status"), TYPE("type"), STREET("street"), ANNUAL_TURNOVER("annualTurnover");
    private final String name;
}
