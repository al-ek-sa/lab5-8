package edu.itmo.piikt.common.interfaceCommon;

import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;

/**
 * Client interface for network communication with the server
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public interface Client extends AutoCloseable {
	/**
	 * Closes the client connection
	 * @throws Exception if an error occurs during closing
	 */
	@Override
	void close() throws Exception;

	/**
	 * Establishes a connection to the server
	 * @throws Exception if connection fails
	 */
	void connect() throws Exception;

	/**
	 * Sends a command to the server
	 * @param clientResponse command to send
	 * @return server response
	 * @throws Exception if sending or receiving fails
	 */
	ServerResponse send(ClientCommand clientResponse) throws Exception;
}
