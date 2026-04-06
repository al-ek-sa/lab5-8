package edu.itmo.piikt.common.protocol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ProtocolMessage implements Serializable {
    private static final long serialVersion = 1L;
    private Object data;
    private Message type;
    private long time;
    private UUID id;

    public ProtocolMessage ask(UUID uuid){
        return ProtocolMessage.builder().id(uuid).
                type(Message.ASK).time(System.currentTimeMillis()).build();
    }

    public ProtocolMessage request(Object data){
        return ProtocolMessage.builder().id(UUID.randomUUID()).type(Message.CUSTOMER_REQUEST).data(data).time(System.currentTimeMillis()).build();
    }

    public ProtocolMessage response(Object data, UUID id) {
        return ProtocolMessage.builder().id(id).type(Message.SERVER_RESPONSE).data(data).time(System.currentTimeMillis()).build();
    }
}
