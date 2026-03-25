package edu.itmo.piikt.common.server_client;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.ByteBuffer;
@Data
@AllArgsConstructor
public class ClientData {
    private final ByteBuffer reader;
    private final ByteBuffer writer;
    private Object command;
    private  Object message;
    public ClientData(Integer number) {
        this.reader = ByteBuffer.allocate(number);
        this.writer = ByteBuffer.allocate(number);
    }


    public void clearReader() {
        reader.clear();
    }

    public void flipReader() {
        reader.flip();
    }

    public void clearWriter() {
        writer.clear();
    }

    public void flipWriter() {
        writer.flip();
    }
}
