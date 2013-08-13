/*******************************************************************************
 * Copyright (c) 2012 James Richardson.
 * 
 * InetAddressRecord.java is part of Alias.
 * 
 * Alias is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Alias is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Alias. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package name.richardson.james.bukkit.alias.persistence;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

import com.avaje.ebean.annotation.CacheStrategy;
import com.avaje.ebean.validation.NotNull;

@Entity
@Table(name = "alias_addresses")
@CacheStrategy(readOnly = false, useBeanCache = true, warmingQuery ="order by id")
public class InetAddressRecord {

	@NotNull
	private String address;

	@Id
	private int id;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Timestamp lastSeen;

	@ManyToMany(mappedBy = "addresses")
	private List<PlayerNameRecord> playerNames;

	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
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

	@ManyToMany(targetEntity = PlayerNameRecord.class, cascade = CascadeType.PERSIST)
	public List<PlayerNameRecord> getPlayerNames() {
		if (this.playerNames == null) return Collections.emptyList();
		return this.playerNames;
	}

	public void setPlayerNames(final List<PlayerNameRecord> playerNames) {
		this.playerNames = playerNames;
	}

	public Set<String> getAliases() {
		final Set<String> aliases = new HashSet<String>();
		for (PlayerNameRecord playerNameRecord : getPlayerNames()) {
			for (InetAddressRecord inetAddressRecord : playerNameRecord.getAddresses()) {
				aliases.add(inetAddressRecord.getAddress());
			}
		}
		return aliases;
	}

}
