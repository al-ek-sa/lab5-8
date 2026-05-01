package edu.itmo.piikt.common.sc;

import java.io.Serial;
import java.io.Serializable;
import lombok.*;

/**
 * Command sent from client to server for execution
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Value
@Builder
public class ClientCommand implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	String nameCommand;
	String argumentCommand;
	Object data;
	String login;
	String email;
	String password;
	String user;
}
