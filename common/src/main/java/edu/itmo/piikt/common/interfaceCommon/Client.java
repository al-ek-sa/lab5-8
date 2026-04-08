package edu.itmo.piikt.common.interfaceCommon;

import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;

public interface Client extends AutoCloseable {
    @Override
    void close() throws Exception;
    void connect() throws Exception;
    ServerResponse send(ClientCommand clientResponse) throws Exception;
}
