package edu.itmo.piikt.common.util;

import edu.itmo.piikt.common.logger.AppLogger;
import java.io.*;
import java.nio.ByteBuffer;
import lombok.experimental.UtilityClass;

/**
 * Serialization utility for converting objects to/from ByteBuffer
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@UtilityClass
public class DS {
	private static final AppLogger log = new AppLogger(DS.class);

	/**
	 * Deserializes an object from a ByteBuffer
	 * @param byteBuffer buffer containing serialized data
	 * @return deserialized object
	 * @throws RuntimeException if deserialization fails
	 */
	public static Object deserialize(ByteBuffer byteBuffer) {
		log.debug("Deserializing object from ByteBuffer, size: {} bytes", byteBuffer.remaining());
		try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteBuffer.array(), 0,
				byteBuffer.limit());
				ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
			var result = objectInputStream.readObject();
			log.debug("Deserialization successful: {}", result.getClass().getSimpleName());
			return result;
		} catch (ClassNotFoundException ex) {
			log.error("Deserialization failed: class not found - {}", ex.getMessage());
			throw new RuntimeException("Class not found during deserialization", ex);
		} catch (IOException e) {
			log.error("Deserialization failed: IO error - {}", e.getMessage());
			throw new RuntimeException("IO error during deserialization", e);
		} catch (Exception e) {
			log.error("Deserialization failed: {}", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/**
	 * Serializes an object to a ByteBuffer
	 * @param object object to serialize
	 * @return ByteBuffer containing serialized data
	 * @throws RuntimeException if serialization fails
	 */
	public static ByteBuffer serialize(Object object) {
		log.debug("Serializing object: {}", object.getClass().getSimpleName());
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
			objectOutputStream.writeObject(object);
			objectOutputStream.flush();
			byte[] bytes = byteArrayOutputStream.toByteArray();
			log.debug("Serialization successful, size: {} bytes", bytes.length);
			return ByteBuffer.wrap(bytes);
		} catch (NotSerializableException e) {
			log.error("Serialization failed: object not serializable - {}", e.getMessage());
			throw new RuntimeException("Object not serializable: " + object.getClass().getName(), e);
		} catch (IOException e) {
			log.error("Serialization failed: IO error - {}", e.getMessage());
			throw new RuntimeException("IO error during serialization", e);
		}
	}
}
