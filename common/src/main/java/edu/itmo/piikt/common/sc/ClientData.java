package edu.itmo.piikt.common.sc;

import lombok.Data;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Data
public class ClientData {
	private ByteBuffer reader;
	private ByteBuffer writer;
	private Object command;
	private Object message;
	private String user;

	private boolean readingChunked = false;
	private long expectedSize = 0;
	private List<byte[]> chunks = new ArrayList<>();
	private long receivedBytes = 0;

	public ClientData(Integer number) {
		this.reader = ByteBuffer.allocate(number);
		this.writer = ByteBuffer.allocate(number);
	}

	public void clearReader() {
		reader.clear();
	}

	public void startChunkedReading(long expectedSize) {
		this.readingChunked = true;
		this.expectedSize = expectedSize;
		this.receivedBytes = 0;
		this.chunks.clear();
	}

	public void addChunk(byte[] data) {
		chunks.add(data);
		receivedBytes += data.length;
	}

	public boolean isChunkedComplete() {
		return receivedBytes >= expectedSize;
	}

	public byte[] getAssembledData() {
		byte[] result = new byte[(int) expectedSize];
		int offset = 0;
		for (byte[] chunk : chunks) {
			System.arraycopy(chunk, 0, result, offset, chunk.length);
			offset += chunk.length;
		}
		return result;
	}

	public void finishChunkedReading() {
		this.readingChunked = false;
		this.chunks.clear();
		this.expectedSize = 0;
		this.receivedBytes = 0;
	}
}
