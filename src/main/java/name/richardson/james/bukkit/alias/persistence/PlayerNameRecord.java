/*
 * Copyright (c) 2013 James Richardson.
 *
 * PlayerNameRecord.java is part of Alias.
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

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

import com.avaje.ebean.annotation.CacheStrategy;
import com.avaje.ebean.validation.NotNull;

@Entity
@Table(name = "alias_players")
public class PlayerNameRecord {

	private List<InetAddressRecord> addresses;

	@Id
	private int id;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Timestamp lastSeen;

	@NotNull
	private String playerName;

	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "alias_players_addresses")
	public List<InetAddressRecord> getAddresses() {
		if (this.addresses == null) {
			return new ArrayList<InetAddressRecord>();
		} else {
			return this.addresses;
		}
	}

	public void setAddresses(final List<InetAddressRecord> addresses) {
		this.addresses = addresses;
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public Timestamp getLastSeen() {
		return this.lastSeen;
	}

	public void setLastSeen(final Timestamp lastSeen) {
		this.lastSeen = lastSeen;
	}

	public String getPlayerName() {
		return this.playerName;
	}

	public void setPlayerName(final String playerName) {
		this.playerName = playerName;
	}

	public Set<String> getAliases() {
		final Set<String> aliases = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		for (InetAddressRecord inetAddressRecord : getAddresses()) {
			for (PlayerNameRecord playerNameRecord : inetAddressRecord.getPlayerNames()) {
				aliases.add(playerNameRecord.getPlayerName());
			}
		}
		return aliases;
	}

	public List<PlayerNameRecord> getPlayerNameRecords() {
		final List<PlayerNameRecord> aliases = new ArrayList<PlayerNameRecord>();
		for (InetAddressRecord inetAddressRecord : getAddresses()) {
			for (PlayerNameRecord playerNameRecord : inetAddressRecord.getPlayerNames()) {
				aliases.add(playerNameRecord);
			}
		}
		return aliases;
	}

	public void createAssociation(PlayerNameRecord playerNameRecord) {
		List<InetAddressRecord> addressRecords = getAddresses();
		for (InetAddressRecord inetAddressRecord : playerNameRecord.getAddresses()) {
			addressRecords.add(inetAddressRecord);
		}
		setAddresses(addressRecords);
	}

	public void removeAssociation(PlayerNameRecord playerNameRecord) {
		for (InetAddressRecord inetAddressRecord : playerNameRecord.getAddresses()) {
			if (inetAddressRecord.getPlayerNames().contains(this)) {
				getAddresses().remove(inetAddressRecord);
			}
		}
	}


}
