package edu.itmo.piikt.client.mode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public enum NameMode {
	INTERACTIVE("interactive"), CRON("cron");
	private final String name;
	NameMode(String name) {
		this.name = name;
	}
}
