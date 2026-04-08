package edu.itmo.piikt.common.server_client;

import java.io.Serial;
import java.io.Serializable;
import lombok.*;

@Value
@Builder
public class ClientCommand implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	String nameCommand;
	String argumentCommand;
	Object data;
}
