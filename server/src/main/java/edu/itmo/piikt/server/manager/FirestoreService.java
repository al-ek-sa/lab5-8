package edu.itmo.piikt.server.manager;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.models.Worker;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FirestoreService {
	private static final AppLogger logger = new AppLogger(FirestoreService.class);
	private final Firestore firestore;

	public FirestoreService() throws IOException {
		try (Context ignored = Context.newId()) {
			String keyPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
			String projectId = System.getenv("GOOGLE_CLOUD_PROJECT");

			if (keyPath == null || keyPath.isBlank()) {
				logger.error("GOOGLE_APPLICATION_CREDENTIALS environment variable not set");
				throw new IOException("GOOGLE_APPLICATION_CREDENTIALS not set");
			}
			if (projectId == null || projectId.isBlank()) {
				logger.error("GOOGLE_CLOUD_PROJECT environment variable not set");
				throw new IOException("GOOGLE_CLOUD_PROJECT not set");
			}

			GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(keyPath));
			this.firestore = FirestoreOptions.newBuilder()
					.setProjectId(projectId)
					.setCredentials(credentials)
					.build()
					.getService();
			logger.info("Firestore connected successfully! Project: {}", projectId);
		}
	}

	public boolean saveWorker(Worker worker) {
		try (Context ignored = Context.newId()) {
			logger.debug("Saving worker to Firestore: id={}, name={}", worker.getUuid(), worker.getName());
			firestore.collection("workers")
					.document(worker.getUuid())
					.set(worker)
					.get();
			logger.info("Worker saved to Firestore: id={}", worker.getUuid());
			return true;
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Failed to save worker: {}", e.getMessage(), e);
			return false;
		} catch (Exception e) {
			logger.error("Unexpected error saving worker: {}", e.getMessage(), e);
			return false;
		}
	}

	public List<Worker> getAllWorkers() {
		try (Context ignored = Context.newId()) {
			logger.debug("Fetching all workers from Firestore");
			List<Worker> workers = new ArrayList<>();
			for (DocumentSnapshot doc : firestore.collection("workers").get().get().getDocuments()) {
				Worker worker = doc.toObject(Worker.class);
				if (worker != null) {
					workers.add(worker);
				}
			}
			logger.info("Fetched {} workers from Firestore", workers.size());
			return workers;
		} catch (Exception e) {
			logger.error("Failed to fetch workers from Firestore: {}", e.getMessage(), e);
			return new ArrayList<>();
		}
	}

	public boolean deleteWorker(String workerId) {
		try (Context ignored = Context.newId()) {
			logger.debug("Deleting worker from Firestore: id={}", workerId);
			firestore.collection("workers")
					.document(workerId)
					.delete()
					.get();
			logger.info("Worker deleted from Firestore: id={}", workerId);
			return true;
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Failed to delete worker: {}", e.getMessage(), e);
			return false;
		} catch (Exception e) {
			logger.error("Unexpected error deleting worker: {}", e.getMessage(), e);
			return false;
		}
	}
}