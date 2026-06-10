package edu.itmo.piikt.common.util;

import edu.itmo.piikt.common.logger.AppLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

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
		try {
			byte[] bytes = new byte[byteBuffer.remaining()];
			byteBuffer.get(bytes);
			return MAPPER.readValue(bytes, tClass);
		} catch (IOException e) {
			throw new RuntimeException("JSON deserialization failed", e);
		}
	}

	public static ByteBuffer serializeWithSize(Object object) {
		try {
			byte[] data = MAPPER.writeValueAsBytes(object);
			ByteBuffer buffer = ByteBuffer.allocate(8 + data.length);
			buffer.putLong(data.length);
			buffer.put(data);
			buffer.flip();
			return buffer;
		} catch (IOException e) {
			throw new RuntimeException("Serialization failed", e);
		}
	}

}
