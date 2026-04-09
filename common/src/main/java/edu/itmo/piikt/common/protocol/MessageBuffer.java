package edu.itmo.piikt.common.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.ByteBuffer;
import java.util.List;

@Data
public class MessageBuffer {
    private final int size;
    private ByteBuffer buffer;

    public MessageBuffer(int size){
        this.size = size;
        this.buffer = ByteBuffer.allocate(size);
    }

    public void expandBuffer(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(buffer.capacity() + size);
        buffer.flip();
        byteBuffer.put(buffer);
        buffer = byteBuffer;
    }

    public void append(ByteBuffer bufferByte) {
        while(bufferByte.hasRemaining()){
            if(!buffer.hasRemaining()){
                expandBuffer();
            }
            buffer.put(bufferByte.get());
        }
    }

    public void clear(){
        buffer.clear();
    }

    public List<byte[]> extract(){
        return Frame.decode(buffer);
    }
}
