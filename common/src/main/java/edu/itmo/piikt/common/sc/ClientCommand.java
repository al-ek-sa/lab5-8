package edu.itmo.piikt.common.sc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

/**
 * Command sent from client to server for execution
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record ClientCommand(String nameCommand, String argumentCommand, Object data, String login, String email,
		String password, String user, String language) {
}
