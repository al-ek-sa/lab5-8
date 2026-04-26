package edu.itmo.piikt.client.mode;

import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.client.io.provider.IOProvider;

/**
 * Strategy interface for different client execution modes
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public interface ClientMode {
	void execute(Network network, IOProvider io);
}
