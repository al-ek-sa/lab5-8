package edu.itmo.piikt.common.server_client;

import lombok.*;

import java.io.Serializable;

@Value
@Builder
public class ClientCommand implements Serializable {
    private static final long serialVersionUID = 1L;
    String nameCommand;
    String argumentCommand;
    Object data;
}
