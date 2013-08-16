/*
 * Copyright (c) 2013 James Richardson.
 *
 * InetAddressRecordManager.java is part of Alias.
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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
import org.apache.commons.lang.Validate;

import name.richardson.james.bukkit.utilities.logging.PluginLoggerFactory;

public class InetAddressRecordManager {

	private final Logger logger = PluginLoggerFactory.getLogger(InetAddressRecordManager.class);
	private final EbeanServer database;

	public InetAddressRecordManager(EbeanServer database) {
		Validate.notNull(database, "Database can not be null!");
		this.database = database;
	}

	public InetAddressRecord create(String address) {
		InetAddressRecord record = find(address);
		if (record == null) {
			logger.log(Level.FINER, "Creating new InetAddressRecord for {0}.", address);
			record = new InetAddressRecord();
			record.setAddress(address);
			save(record);
			record = find(address);
		}
		return record;
	}

	public InetAddressRecord find(String address) {
		logger.log(Level.FINER, "Attempting to find InetAddressRecord matching {0}.", address);
		Query<InetAddressRecord> query = database.createQuery(InetAddressRecord.class);
		query.setParameter("address", address);
		return query.findUnique();
	}

	public boolean exists(String address) {
		logger.log(Level.FINER, "Checking if InetAddressRecord matching {0} exists.", address);
		return find(address) != null;
	}

	public List<InetAddressRecord> list() {
		logger.log(Level.FINER, "Listing all InetAddressRecords.");
		Query<InetAddressRecord> query = database.createQuery(InetAddressRecord.class);
		return query.findList();
	}

	public void save(InetAddressRecord inetAddressRecord) {
		logger.log(Level.FINER, "Saving {0}", inetAddressRecord.toString());
		database.save(inetAddressRecord);
	}

	@Override
	public String toString() {
		return "InetAddressRecordManager{" +
		"database=" + database +
		'}';
	}

}
