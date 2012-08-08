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
package name.richardson.james.bukkit.alias.persistence;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.validation.NotNull;

@Entity
@Table(name = "alias_players")
public class PlayerNameRecord {

  public static PlayerNameRecord findByName(final EbeanServer database, final String playerName) {
    PlayerNameRecord record = database.find(PlayerNameRecord.class).where().ieq("playerName", playerName).findUnique();
    if (record == null) {
      record = new PlayerNameRecord();
      record.setPlayerName(playerName);
      database.save(record);
      record = database.find(PlayerNameRecord.class).where().ieq("playerName", playerName).findUnique();
    }
    return record;
  }

  public static boolean isPlayerKnown(final EbeanServer database, final String playerName) {
    final PlayerNameRecord record = database.find(PlayerNameRecord.class).where().ieq("playerName", playerName).findUnique();
    if (record != null) {
      return true;
    } else {
      return false;
    }
  }

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
      return Collections.emptyList();
    }
    return this.addresses;
  }

  public int getId() {
    return this.id;
  }

  public Timestamp getLastSeen() {
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

  public void setLastSeen(final Timestamp lastSeen) {
    this.lastSeen = lastSeen;
  }

  public void setPlayerName(final String playerName) {
    this.playerName = playerName;
  }

  public void updateLastSeen() {
    this.lastSeen = new Timestamp(System.currentTimeMillis());
  }

}
