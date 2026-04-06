package edu.itmo.piikt.common.protocol;

import lombok.experimental.UtilityClass;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
@UtilityClass
public class Frame {
    public static ByteBuffer encode(byte[] data) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 + data.length);
        byteBuffer.putInt(data.length);
        byteBuffer.put(data);
        byteBuffer.flip();
        return byteBuffer;
    }

    public static List<byte[]> decode(ByteBuffer byteBuffer) {
        List<byte[]> list = new ArrayList<>();
        byteBuffer.flip();
        while(byteBuffer.remaining() >= 4) {
            byteBuffer.mark();
            int number = byteBuffer.getInt();
            if (byteBuffer.remaining() >= number) {
                byte[] input = new byte[number];
                byteBuffer.get(input);
                list.add(input);
            } else {
                byteBuffer.reset();
                break;
            }
        }
        ByteBuffer buffer = ByteBuffer.allocate(byteBuffer.remaining());
        buffer.put(byteBuffer);
        buffer.flip();
        byteBuffer.clear();
        byteBuffer.put(buffer);
        return list;
    }
}


