package edu.itmo.piikt.server.history;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.models.Address;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Singleton storage for unique addresses
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public enum HistoryAddress {
	INSTANCE;
	private static final AppLogger logger = new AppLogger(HistoryAddress.class);
	/** List of unique addresses*/
	private final List<Address> list = new ArrayList<>();

	/**
	 * Adds an address to the collection if it doesn't already exist
	 * @param address address to add
	 */
	public void add(Address address) {
		try (Context ignored = Context.newId()) {
			logger.debug("Adding address: street={}", address.getStreet());
			boolean flag = true;
			for (Address address1 : list) {
				if (Objects.equals(address1.getStreet(), address.getStreet())) {
					flag = false;
					break;
				}
			}
			if (flag)
				list.add(address);
			logger.debug("Collection size: {}", list.size());
		} catch (Exception e) {
			logger.error("Error adding address: {}", e);
			throw new RuntimeException(e);
		}
	}
}
