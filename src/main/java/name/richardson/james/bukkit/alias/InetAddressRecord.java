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
package name.richardson.james.bukkit.alias;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotNull;

import name.richardson.james.bukkit.util.Database;
import name.richardson.james.bukkit.util.Logger;

@Entity
@Table(name = "alias_addresses")
public class InetAddressRecord {

  private static Logger logger = new Logger(InetAddressRecord.class);

  public static InetAddressRecord findByAddress(final Database database, final String address) {
    logger.debug(String.format("Attempting to return InetAddressRecord matching the address %s.", address));
    final InetAddressRecord record = database.getEbeanServer().find(InetAddressRecord.class).where().eq("address", address).findUnique();
    return record;
  }

  public static boolean isAddressKnown(final Database database, final String address) {
    final InetAddressRecord record = database.getEbeanServer().find(InetAddressRecord.class).where().ieq("address", address).findUnique();
    if (record != null) {
      return true;
    } else {
      return false;
    }
  }

  @ManyToMany(mappedBy = "addresses")
  private List<PlayerNameRecord> playerNames;

  @Id
  private int id;

  @NotNull
  private String address;

  @NotNull
  private long lastSeen;

  public String getAddress() {
    return this.address;
  }

  public int getId() {
    return this.id;
  }

  public long getLastSeen() {
    return this.lastSeen;
  }

  @ManyToMany(targetEntity = PlayerNameRecord.class)
  public List<PlayerNameRecord> getPlayerNames() {
    return this.playerNames;
  }

  public void setAddress(final String address) {
    this.address = address;
  }

  public void setId(final int id) {
    this.id = id;
  }

  public void setLastSeen(final long lastSeen) {
    this.lastSeen = lastSeen;
  }

  public void setPlayerNames(final List<PlayerNameRecord> playerNames) {
    this.playerNames = playerNames;
  }

  public void updateLastSeen() {
    this.lastSeen = System.currentTimeMillis();
  }

}
