package edu.itmo.piikt.managers;

import com.opencsv.bean.CsvBindByName;

public record CommandRecord(@CsvBindByName(column = "command") String command) {
}
