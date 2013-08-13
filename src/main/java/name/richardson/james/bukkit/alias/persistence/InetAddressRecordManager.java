package name.richardson.james.bukkit.alias.persistence;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
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
			LOGGER.log(Level.FINER, "Creating new InetAddressRecord for {0}.", address);
			record = new InetAddressRecord();
			record.setAddress(address);
			save(record);
			record = find(address);
		}
		return record;
	}

	public InetAddressRecord find(String address) {
		LOGGER.log(Level.FINER, "Attempting to find InetAddressRecord matching {0}.", address);
		Query<InetAddressRecord> query = database.createQuery(InetAddressRecord.class);
		query.setParameter("address", address);
		return query.findUnique();
	}

	public boolean exists(String address) {
		LOGGER.log(Level.FINER, "Checking if InetAddressRecord matching {0} exists.", address);
		return find(address) != null;
	}

	public List<InetAddressRecord> list() {
		LOGGER.log(Level.FINER, "Listing all InetAddressRecords.");
		Query<InetAddressRecord> query = database.createQuery(InetAddressRecord.class);
		return query.findList();
	}

	public void save(InetAddressRecord inetAddressRecord) {
		LOGGER.log(Level.FINER, "Saving {0}", inetAddressRecord.toString());
		database.save(inetAddressRecord);
	}

	@Override
	public String toString() {
		return "InetAddressRecordManager{" +
		"database=" + database +
		'}';
	}

}
