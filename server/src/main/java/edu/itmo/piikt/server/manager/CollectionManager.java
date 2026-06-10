package edu.itmo.piikt.server.manager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.server.history.HistoryWorker;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CollectionManager {
	private final ObjectMapper objectMapper;
	private final ReentrantReadWriteLock lock;
	@Getter
	private long currentVersion;
	private final List<VersionedUpdate> updateHistory;
	private static final int MAX_HISTORY_SIZE = 1000;

	public CollectionManager() {
		this.objectMapper = new ObjectMapper();
		this.lock = new ReentrantReadWriteLock();
		this.updateHistory = new ArrayList<>();
		this.currentVersion = HistoryWorker.INSTANCE.getListWorker().size();
		objectMapper.findAndRegisterModules();
	}

	public List<Worker> getAllWorkers() {
		lock.readLock().lock();
		try {
			return new ArrayList<>(HistoryWorker.INSTANCE.getListWorker());
		} finally {
			lock.readLock().unlock();
		}
	}

	public void addWorker(Worker worker) {
		lock.writeLock().lock();
		try {
			HistoryWorker.INSTANCE.add(worker);
			currentVersion++;
			JsonNode data = objectMapper.valueToTree(worker);
			saveUpdateToHistory(currentVersion, "ADD", data);
		} finally {
			lock.writeLock().unlock();
		}
	}

	public boolean updateWorker(Worker updated) {
		lock.writeLock().lock();
		try {
			Worker existing = findWorkerByUuid(updated.getUuid());
			if (existing == null) {
				return false;
			}

			HistoryWorker.INSTANCE.getListWorker().remove(existing);
			HistoryWorker.INSTANCE.add(updated);
			currentVersion++;
			JsonNode data = objectMapper.valueToTree(updated);
			saveUpdateToHistory(currentVersion, "UPDATE", data);
			return true;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public boolean removeWorker(String uuid) {
		lock.writeLock().lock();
		try {
			Worker toRemove = findWorkerByUuid(uuid);
			if (toRemove == null) {
				return false;
			}

			HistoryWorker.INSTANCE.getListWorker().remove(toRemove);
			currentVersion++;
			JsonNode data = objectMapper.createObjectNode().put("uuid", uuid);
			saveUpdateToHistory(currentVersion, "REMOVE", data);
			return true;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void clearAllWorkers() {
		lock.writeLock().lock();
		try {
			int size = HistoryWorker.INSTANCE.getListWorker().size();
			HistoryWorker.INSTANCE.getListWorker().clear();

			if (size > 0) {
				currentVersion++;
				JsonNode data = objectMapper.createObjectNode();
				saveUpdateToHistory(currentVersion, "CLEAR", data);
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	private void saveUpdateToHistory(long version, String operation, JsonNode data) {
		updateHistory.add(new VersionedUpdate(version, operation, data));
		while (updateHistory.size() > MAX_HISTORY_SIZE) {
			updateHistory.removeFirst();
		}
	}

	public List<String> getUpdatesSince(long fromVersion) {
		lock.readLock().lock();
		try {
			List<String> updates = new ArrayList<>();
			for (VersionedUpdate update : updateHistory) {
				if (update.version() > fromVersion) {
					try {
						ObjectNode message = objectMapper.createObjectNode();
						message.put("type", "UPDATE");
						message.put("version", update.version());
						message.put("operation", update.operation());
						message.set("data", update.data());
						updates.add(objectMapper.writeValueAsString(message));
					} catch (Exception e) {
						// todo
					}
				}
			}
			return updates;
		} finally {
			lock.readLock().unlock();
		}
	}

	public JsonNode getAllWorkersJson() {
		lock.readLock().lock();
		try {
			ArrayNode workers = objectMapper.createArrayNode();
			for (Worker worker : HistoryWorker.INSTANCE.getListWorker()) {
				workers.add(objectMapper.valueToTree(worker));
			}
			return workers;
		} finally {
			lock.readLock().unlock();
		}
	}

	public Worker findWorkerByUuid(String uuid) {
		for (Worker w : HistoryWorker.INSTANCE.getListWorker()) {
			if (w.getUuid().equals(uuid)) {
				return w;
			}
		}
		return null;
	}

	public record VersionedUpdate(long version, String operation, JsonNode data) {
	}
}
