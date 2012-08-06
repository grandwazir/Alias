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

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.validation.NotNull;

@Entity
@Table(name = "alias_addresses")
public class InetAddressRecord {

  public static InetAddressRecord findByAddress(final EbeanServer database, final String address) {
    InetAddressRecord record = database.find(InetAddressRecord.class).where().eq("address", address).findUnique();
    if (record == null) {
      record = new InetAddressRecord();
      record.setAddress(address);
      database.save(record);
      record = database.find(InetAddressRecord.class).where().eq("address", address).findUnique();
    }
    return record;
  }

  public static boolean isAddressKnown(final EbeanServer database, final String address) {
    final InetAddressRecord record = database.find(InetAddressRecord.class).where().ieq("address", address).findUnique();
    if (record != null) {
      return true;
    } else {
      return false;
    }
  }

  @NotNull
  private String address;

  @Id
  private int id;

  @Temporal(TemporalType.TIMESTAMP)
  private Timestamp lastSeen;

  @ManyToMany(mappedBy = "addresses")
  private List<PlayerNameRecord> playerNames;

  public String getAddress() {
    return this.address;
  }

  public int getId() {
    return this.id;
  }

  public Timestamp getLastSeen() {
    return this.lastSeen;
  }

  @ManyToMany(targetEntity = PlayerNameRecord.class, cascade = CascadeType.PERSIST)
  public List<PlayerNameRecord> getPlayerNames() {
    if (this.playerNames == null) {
      return Collections.emptyList();
    }
    return this.playerNames;
  }

  public void setAddress(final String address) {
    this.address = address;
  }

  public void setId(final int id) {
    this.id = id;
  }

  public void setLastSeen(final Timestamp lastSeen) {
    this.lastSeen = lastSeen;
  }

  public void setPlayerNames(final List<PlayerNameRecord> playerNames) {
    this.playerNames = playerNames;
  }

  public void updateLastSeen() {
    this.lastSeen = new Timestamp(System.currentTimeMillis());
  }

}
