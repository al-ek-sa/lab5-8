package edu.itmo.piikt.common.interfaceCommon;

import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;

import java.util.function.Function;

public interface Server {
    void stop();
    boolean connected();
    void start(Function<ClientCommand, ServerResponse> serverResponseFunction) throws Exception;
}
