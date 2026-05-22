package edu.itmo.piikt.common.util;

import edu.itmo.piikt.common.logger.AppLogger;
import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import lombok.experimental.UtilityClass;

/**
 * Serialization utility for converting objects to/from ByteBuffer
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
@UtilityClass
public class DS {
	private static final AppLogger log = new AppLogger(DS.class);
	private static final ObjectMapper MAPPER;
	static {
		MAPPER = new ObjectMapper();
		MAPPER.registerModule(new JavaTimeModule());
		MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	public static <T> T deserialize(ByteBuffer byteBuffer, Class<T> tClass) {
		log.debug("Deserializing object from ByteBuffer, size: {} bytes", byteBuffer.remaining());
		try {
			byte[] bytes = new byte[byteBuffer.remaining()];
			byteBuffer.get(bytes);
			String json = new String(bytes, StandardCharsets.UTF_8);
			log.debug("JSON: {}", json);
			T result = MAPPER.readValue(json, tClass);log.debug("Deserialization successful: {}", result.getClass().getSimpleName());
			return result;
		} catch (IOException e) {
			log.error("Deserialization failed: {}", e.getMessage());
			throw new RuntimeException("JSON deserialization failed", e);
		}
	}

	public static ByteBuffer serialize(Object object) {
		log.debug("Serializing object: {}", object.getClass().getSimpleName());
		try {
			String json = MAPPER.writeValueAsString(object);
			log.debug("JSON: {}", json);
			byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
			log.debug("Serialization successful, size: {} bytes", bytes.length);
			return ByteBuffer.wrap(bytes);
		} catch (IOException e) {
			log.error("Serialization failed: {}", e.getMessage());
			throw new RuntimeException("JSON serialization failed for: " + object.getClass().getName(), e);
		}
	}

	public static ObjectMapper getMapper() {
		return MAPPER;
	}
}
