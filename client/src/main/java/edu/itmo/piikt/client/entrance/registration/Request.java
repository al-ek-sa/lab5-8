package edu.itmo.piikt.client.entrance.registration;

import edu.itmo.piikt.common.server_client.ClientCommand;

public interface Request {
	ClientCommand execute();
	void getDescription();
}
