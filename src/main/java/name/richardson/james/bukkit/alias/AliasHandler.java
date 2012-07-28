/*******************************************************************************
 * Copyright (c) 2012 James Richardson.
 * 
 * AliasHandler.java is part of Alias.
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

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

import com.avaje.ebean.EbeanServer;

import org.bukkit.entity.Player;

import name.richardson.james.bukkit.alias.persistence.InetAddressRecord;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecord;
import name.richardson.james.bukkit.utilities.internals.Handler;
import name.richardson.james.bukkit.utilities.internals.Logger;

public class AliasHandler extends Handler implements AliasAPI {

  private final Logger logger = new Logger(this.getClass());
  
  private final EbeanServer database;
  
  public AliasHandler(final Class<?> parentClass, final Alias plugin) {
    super(parentClass);
    this.database = plugin.getDatabase();
  }

  public Set<String> getIPAddresses(final Player player) {
    return this.getIPAddresses(player.getName());
  }

  public Set<String> getIPAddresses(final String playerName) {
    final Set<String> set = new HashSet<String>();
    final PlayerNameRecord records = PlayerNameRecord.findByName(database, playerName);
    for (final InetAddressRecord record : records.getAddresses()) {
      set.add(record.getAddress());
    }
    return set;
  }

  public Set<String> getPlayersNames(final InetAddress ip) {
    final Set<String> set = new HashSet<String>();
    final InetAddressRecord records = InetAddressRecord.findByAddress(database, ip.getHostAddress());
    for (final PlayerNameRecord record : records.getPlayerNames()) {
      set.add(record.getPlayerName());
    }
    return set;
  }
  
  public void associatePlayer(String playerName, String address) {
    final PlayerNameRecord playerNameRecord = this.getPlayerNameRecord(playerName);
    final InetAddressRecord inetAddressRecord = this.getInetAddressRecord(address.toString());

    // update time stamps
    final long now = System.currentTimeMillis();
    playerNameRecord.setLastSeen(now);
    inetAddressRecord.setLastSeen(now);
    this.logger.debug(playerNameRecord.getAddresses().toString());

    // link IP address to name
    if (!playerNameRecord.getAddresses().contains(inetAddressRecord)) {
      playerNameRecord.getAddresses().add(inetAddressRecord);
      this.logger.debug(playerNameRecord.getAddresses().toString());
    }

    database.save(playerNameRecord);
    database.save(inetAddressRecord);
  }
  
  public void deassociatePlayer(String playerName, String alias) {
    final PlayerNameRecord playerRecord = this.getPlayerNameRecord(playerName);
    final PlayerNameRecord aliasRecord  = this.getPlayerNameRecord(alias);
    if (playerRecord == null || aliasRecord == null) return;
    for (InetAddressRecord record : aliasRecord.getAddresses()) {
      if (playerRecord.getAddresses().contains(record)) {
        playerRecord.getAddresses().remove(record);
      }
    }
    database.save(playerRecord);
  }
  
  private InetAddressRecord getInetAddressRecord(final String address) {
    if (!InetAddressRecord.isAddressKnown(database, address)) {
      final InetAddressRecord record = new InetAddressRecord();
      record.setAddress(address);
      record.updateLastSeen();
      database.save(record);
    }
    return InetAddressRecord.findByAddress(database, address);
  }

  private PlayerNameRecord getPlayerNameRecord(final String playerName) {
    if (!PlayerNameRecord.isPlayerKnown(database, playerName)) {
      final PlayerNameRecord record = new PlayerNameRecord();
      record.setPlayerName(playerName);
      record.updateLastSeen();
      database.save(record);
    }
    return PlayerNameRecord.findByName(database, playerName);
  }

}
