package name.richardson.james.bukkit.alias.persistence;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.avaje.ebean.EbeanServer;
import org.apache.commons.lang.Validate;

import name.richardson.james.bukkit.utilities.logging.PrefixedLogger;

public class PlayerNameRecordManager {

	private static final Logger LOGGER = PrefixedLogger.getLogger(PlayerNameRecordManager.class);
	private final EbeanServer database;

	public PlayerNameRecordManager(EbeanServer database) {
		Validate.notNull(database, "Database can not be null!");
		this.database = database;
	}

	public PlayerNameRecord create(String playerName) {
		PlayerNameRecord record = find(playerName);
		if (record == null) {
			LOGGER.log(Level.FINER, "Creating new PlayerNameRecord for {0}.", playerName);
			record = new PlayerNameRecord();
			record.setPlayerName(playerName);
			database.save(record);
			record = find(playerName);
		}
		return record;
	}

	public boolean exists(String playerName) {
		LOGGER.log(Level.FINER, "Checking if PlayerNameRecord matching {0} exists.", playerName);
		return find(playerName) != null;
	}

	public PlayerNameRecord find(String playerName) {
		LOGGER.log(Level.FINER, "Attempting to find PlayerNameRecord matching {0}.", playerName);
		PlayerNameRecord record = database.find(PlayerNameRecord.class).where().ieq("name", playerName).findUnique();
		return record;
	}

	public List<PlayerNameRecord> list() {
		LOGGER.log(Level.FINER, "Listing all PlayerNameRecords.");
		return database.find(PlayerNameRecord.class).findList();
	}

	public List<PlayerNameRecord> list(String playerName) {
		LOGGER.log(Level.FINER, "Listing all PlayerNameRecords starting with {0}.", playerName);
		return database.find(PlayerNameRecord.class).where().istartsWith("name", playerName).findList();
	}

	public void save(PlayerNameRecord playerNameRecord) {
		LOGGER.log(Level.FINER, "Saving {0}", playerNameRecord.toString());
		database.save(playerNameRecord);
	}

	@Override
	public String toString() {
		return "PlayerNameRecordManager{" +
		"database=" + database +
		'}';
	}

}
