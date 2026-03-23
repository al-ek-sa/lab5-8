package edu.itmo.piikt.common.util;

import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;

import java.io.*;
import java.nio.ByteBuffer;
//todo проверить какие исключения могут быть
@NoArgsConstructor
@UtilityClass
public class DS {
    public static Object deserialize(ByteBuffer byteBuffer) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteBuffer.array(), 0, byteBuffer.limit());
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            return objectInputStream.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static ByteBuffer serialize(Object object) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            return ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
