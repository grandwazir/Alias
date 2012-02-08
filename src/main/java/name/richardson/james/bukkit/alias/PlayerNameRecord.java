/*******************************************************************************
 * Copyright (c) 2012 James Richardson.
 * 
 * PlayerNameRecord.java is part of Alias.
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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotNull;

import name.richardson.james.bukkit.util.Database;
import name.richardson.james.bukkit.util.Logger;

@Entity
@Table(name = "alias_players")
public class PlayerNameRecord {

  private static Logger logger = new Logger(PlayerNameRecord.class);

  public static PlayerNameRecord findByName(final Database database, final String playerName) {
    logger.debug(String.format("Attempting to return PlayerNameRecord matching the name %s.", playerName));
    final PlayerNameRecord record = database.getEbeanServer().find(PlayerNameRecord.class).where().ieq("playerName", playerName).findUnique();
    return record;
  }

  public static boolean isPlayerKnown(final Database database, final String playerName) {
    final PlayerNameRecord record = database.getEbeanServer().find(PlayerNameRecord.class).where().ieq("playerName", playerName).findUnique();
    if (record != null) {
      return true;
    } else {
      return false;
    }
  }

  @Id
  private int id;

  @NotNull
  private String playerName;

  @NotNull
  private long lastSeen;

  public List<InetAddressRecord> addresses;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "alias_players_addresses")
  public List<InetAddressRecord> getAddresses() {
    return this.addresses;
  }

  public int getId() {
    return this.id;
  }

  public long getLastSeen() {
    return this.lastSeen;
  }

  public String getPlayerName() {
    return this.playerName;
  }

  public void setAddresses(final List<InetAddressRecord> addresses) {
    this.addresses = addresses;
  }

  public void setId(final int id) {
    this.id = id;
  }

  public void setLastSeen(final long lastSeen) {
    this.lastSeen = lastSeen;
  }

  public void setPlayerName(final String playerName) {
    this.playerName = playerName;
  }

  public void updateLastSeen() {
    this.lastSeen = System.currentTimeMillis();
  }

}
