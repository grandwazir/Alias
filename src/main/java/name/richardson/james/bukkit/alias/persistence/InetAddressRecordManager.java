package name.richardson.james.bukkit.alias.persistence;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.avaje.ebean.EbeanServer;
import org.apache.commons.lang.Validate;

import name.richardson.james.bukkit.utilities.logging.PrefixedLogger;

public class InetAddressRecordManager {

	private static final Logger LOGGER = PrefixedLogger.getLogger(InetAddressRecordManager.class);

	private final EbeanServer database;

	public InetAddressRecordManager(EbeanServer database) {
		Validate.notNull(database, "Database can not be null!");
		this.database = database;
	}

	public InetAddressRecord create(String address) {
		InetAddressRecord record = find(address);
		if (record == null) {
			LOGGER.log(Level.FINEST, "Creating new InetAddressRecord for {0}.", address);
			record = new InetAddressRecord();
			record.updateLastSeen();
			record.setAddress(address);
			database.save(record);
			record = database.find(InetAddressRecord.class).where().eq("address", address).findUnique();
		}
		return record;
	}

	public InetAddressRecord find(String address) {
		LOGGER.log(Level.FINEST, "Attempting to find InetAddressRecord matching {0}.", address);
		InetAddressRecord record = database.find(InetAddressRecord.class).where().ieq("address", address).findUnique();
		return record;
	}

	public boolean exists(String address) {
		LOGGER.log(Level.FINEST, "Checking if InetAddressRecord matching {0} exists.", address);
		return find(address) != null;
	}

	public List<InetAddressRecord> list() {
		LOGGER.log(Level.FINEST, "Listing all InetAddressRecords.");
		return database.find(InetAddressRecord.class).findList();
	}

	@Override
	public String toString() {
		return "InetAddressRecordManager{" +
		"database=" + database +
		'}';
	}

}
