/*
 * Copyright (c) 2013 James Richardson.
 *
 * PlayerNameRecordManager.java is part of Alias.
 *
 * Alias is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Alias is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Alias. If not, see <http://www.gnu.org/licenses/>.
 */

package name.richardson.james.bukkit.alias.persistence;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
import org.apache.commons.lang.Validate;

import name.richardson.james.bukkit.utilities.logging.PluginLoggerFactory;
import name.richardson.james.bukkit.utilities.logging.PrefixedLogger;

public class PlayerNameRecordManager {

	private final Logger logger = PluginLoggerFactory.getLogger(PlayerNameRecordManager.class);
	private final EbeanServer database;

	public PlayerNameRecordManager(EbeanServer database) {
		Validate.notNull(database, "Database can not be null!");
		this.database = database;
	}

	public PlayerNameRecord create(String playerName) {
		PlayerNameRecord record = find(playerName);
		if (record == null) {
			logger.log(Level.FINER, "Creating new PlayerNameRecord for {0}.", playerName);
			record = new PlayerNameRecord();
			record.setPlayerName(playerName);
			record.setLastSeen(new Timestamp(System.currentTimeMillis()));
			save(record);
			record = find(playerName);
		}
		return record;
	}

	public boolean exists(String playerName) {
		logger.log(Level.FINER, "Checking if PlayerNameRecord matching {0} exists.", playerName);
		return find(playerName) != null;
	}

	public PlayerNameRecord find(String playerName) {
		logger.log(Level.FINER, "Attempting to find PlayerNameRecord matching {0}.", playerName);
		Query<PlayerNameRecord> query = database.createQuery(PlayerNameRecord.class);
		query.where().ieq("playerName", playerName);
		return query.findUnique();
	}

	public List<PlayerNameRecord> list() {
		return list("");
	}

	public List<PlayerNameRecord> list(String playerName) {
		logger.log(Level.FINER, "Listing all PlayerNameRecords starting with '{0}'.", playerName);
		Query<PlayerNameRecord> query = database.createQuery(PlayerNameRecord.class);
		query.where().istartsWith("playerName", playerName);
		return query.findList();
	}

	public void save(PlayerNameRecord playerNameRecord) {
		logger.log(Level.FINER, "Saving {0}", playerNameRecord.toString());
		database.save(playerNameRecord);
	}

	@Override
	public String toString() {
		return "PlayerNameRecordManager{" +
		"database=" + database +
		'}';
	}

}
