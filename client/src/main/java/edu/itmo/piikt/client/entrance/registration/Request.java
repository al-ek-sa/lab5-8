package edu.itmo.piikt.client.entrance.registration;

import edu.itmo.piikt.common.sc.ClientCommand;

public interface Request {
	ClientCommand execute();
	void getDescription();
	String user();
}
