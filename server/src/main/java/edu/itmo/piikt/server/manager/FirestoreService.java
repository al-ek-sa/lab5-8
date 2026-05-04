package edu.itmo.piikt.server.manager;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.models.Address;
import edu.itmo.piikt.common.models.Coordinates;
import edu.itmo.piikt.common.models.Organization;
import edu.itmo.piikt.common.models.OrganizationType;
import edu.itmo.piikt.common.models.Status;
import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.common.sc.ServerResponse;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Service for interacting with Google Cloud Firestore.
 * Provides CRUD operations for Worker objects in the cloud database.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class FirestoreService {
	private static final AppLogger logger = new AppLogger(FirestoreService.class);
	private final Firestore firestore;

	/**
	 * Initializes Firestore connection using environment variables.
	 * Requires GOOGLE_APPLICATION_CREDENTIALS and GOOGLE_CLOUD_PROJECT to be set.
	 *
	 * @throws IOException if credentials file not found or environment variables are missing
	 */
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
			this.firestore = FirestoreOptions.newBuilder().setProjectId(projectId).setCredentials(credentials).build()
					.getService();
			logger.info("Firestore connected successfully! Project: {}", projectId);
		}
	}

	/**
	 * Saves a Worker to Firestore.
	 * Converts Worker object to Map before saving.
	 *
	 * @param worker Worker object to save
	 * @return ServerResponse indicating success or failure
	 */
	public ServerResponse saveWorker(Worker worker) {
		try (Context ignored = Context.newId()) {
			logger.debug("Saving worker to Firestore: id={}, name={}", worker.getUuid(), worker.getName());
			Map<String, Object> data = convertWorkerToMap(worker);
			firestore.collection("workers").document(worker.getUuid()).set(data).get();
			logger.info("Worker saved to Firestore: id={}", worker.getUuid());
			return ServerResponse.successfulCompletion("Работник успешно добавлен");
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Failed to save worker: {}", e.getMessage(), e);
			return ServerResponse.error("Failed to add worker");
		} catch (Exception e) {
			logger.error("Unexpected error saving worker: {}", e.getMessage(), e);
			return ServerResponse.error("Failed to add worker");
		}
	}

	/**
	 * Retrieves all workers from Firestore.
	 * Converts Firestore documents back to Worker objects.
	 *
	 * @return List of workers
	 */
	public List<Worker> getAllWorkers() {
		try (Context ignored = Context.newId()) {
			logger.debug("Fetching all workers from Firestore");
			List<Worker> workers = new ArrayList<>();
			for (DocumentSnapshot doc : firestore.collection("workers").get().get().getDocuments()) {
				Worker worker = convertDocumentToWorker(doc);
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

	/**
	 * Deletes a worker from Firestore by ID.
	 *
	 * @param workerId Unique identifier of the worker
	 * @return ServerResponse indicating success or failure
	 */
	public ServerResponse deleteWorker(String workerId) {
		try (Context ignored = Context.newId()) {
			logger.debug("Deleting worker from Firestore: id={}", workerId);
			firestore.collection("workers").document(workerId).delete().get();
			logger.info("Worker deleted from Firestore: id={}", workerId);
			return ServerResponse.successfulCompletion("Работник успешно удалился");
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Failed to delete worker: {}", e.getMessage(), e);
			return ServerResponse.error("Deletion failed");
		} catch (Exception e) {
			logger.error("Unexpected error deleting worker: {}", e.getMessage(), e);
			return ServerResponse.error("Deletion failed");
		}
	}

	/**
	 * Converts Worker object to Map for Firestore storage.
	 * Handles date conversion (LocalDate -> ISO string).
	 *
	 * @param worker Worker object to convert
	 * @return Map representation of Worker
	 */
	private Map<String, Object> convertWorkerToMap(Worker worker) {
		Map<String, Object> map = new HashMap<>();
		map.put("uuid", worker.getUuid());
		map.put("name", worker.getName());
		map.put("salary", worker.getSalary());
		map.put("status", worker.getStatus() != null ? worker.getStatus().name() : null);

		if (worker.getStartDate() != null) {
			map.put("startDate", worker.getStartDate().toString());
		}

		if (worker.getEndDate() != null) {
			map.put("endDate", worker.getEndDate().toString());
		} else {
			map.put("endDate", null);
		}
		if (worker.getCoordinates() != null) {
			Map<String, Object> coords = new HashMap<>();
			coords.put("x", worker.getCoordinates().getX());
			coords.put("y", worker.getCoordinates().getY());
			map.put("coordinates", coords);
		}

		if (worker.getOrganization() != null) {
			Map<String, Object> org = new HashMap<>();
			org.put("annualTurnover", worker.getOrganization().getAnnualTurnover());
			org.put("type",
					worker.getOrganization().getType() != null ? worker.getOrganization().getType().name() : null);

			if (worker.getOrganization().getOfficialAddress() != null) {
				Map<String, Object> addr = new HashMap<>();
				addr.put("street", worker.getOrganization().getOfficialAddress().getStreet());
				org.put("address", addr);
			}
			map.put("organization", org);
		}
		return map;
	}

	/**
	 * Converts Firestore Document to Worker object.
	 * Restores dates from ISO strings.
	 *
	 * @param doc Firestore document
	 * @return Worker object or null if conversion fails
	 */
	private Worker convertDocumentToWorker(DocumentSnapshot doc) {
		try {
			Worker worker = new Worker();
			worker.setUuid(doc.getString("uuid"));
			worker.setName(doc.getString("name"));

			Double salary = doc.getDouble("salary");
			if (salary != null) {
				worker.setSalary(salary.floatValue());
			}

			String statusStr = doc.getString("status");
			if (statusStr != null) {
				worker.setStatus(Status.valueOf(statusStr));
			}

			String startDateStr = doc.getString("startDate");
			if (startDateStr != null) {
				worker.setStartDate(LocalDate.parse(startDateStr));
			}

			String endDateStr = doc.getString("endDate");
			if (endDateStr != null) {
				worker.setEndDate(LocalDate.parse(endDateStr));
			} else {
				worker.setEndDate(null);
			}

			Map<String, Object> coords = (Map<String, Object>) doc.get("coordinates");
			if (coords != null) {
				Coordinates coordinates = new Coordinates();
				Long x = (Long) coords.get("x");
				if (x != null) {
					coordinates.setX(x);
				}
				Double y = (Double) coords.get("y");
				if (y != null) {
					coordinates.setY(y.floatValue());
				}
				worker.setCoordinates(coordinates);
			}

			Map<String, Object> org = (Map<String, Object>) doc.get("organization");
			if (org != null) {
				Organization organization = new Organization();
				Long annualTurnover = (Long) org.get("annualTurnover");
				if (annualTurnover != null) {
					organization.setAnnualTurnover(annualTurnover.intValue());
				}
				String orgTypeStr = (String) org.get("type");
				if (orgTypeStr != null) {
					organization.setType(OrganizationType.valueOf(orgTypeStr));
				}
				Map<String, Object> addr = (Map<String, Object>) org.get("address");
				if (addr != null) {
					Address address = new Address();
					address.setStreet((String) addr.get("street"));
					organization.setOfficialAddress(address);
				}
				worker.setOrganization(organization);
			}

			return worker;
		} catch (Exception e) {
			logger.error("Failed to convert document to Worker: {}", e.getMessage(), e);
			return null;
		}
	}
}
