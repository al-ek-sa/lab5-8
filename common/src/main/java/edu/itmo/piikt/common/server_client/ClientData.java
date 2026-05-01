package edu.itmo.piikt.common.server_client;

import java.nio.ByteBuffer;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Container for client-specific data buffers and state
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Data
@AllArgsConstructor
public class ClientData {
	private final ByteBuffer reader;
	private final ByteBuffer writer;
	private Object command;
	private Object message;
	private String user;

	public ClientData(Integer number) {
		this.reader = ByteBuffer.allocate(number);
		this.writer = ByteBuffer.allocate(number);
	}

	public void clearReader() {
		reader.clear();
	}
}
