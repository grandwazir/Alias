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
import java.util.ArrayList;
import java.util.List;

import com.avaje.ebean.EbeanServer;

import org.bukkit.entity.Player;

import name.richardson.james.bukkit.alias.persistence.InetAddressRecord;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecord;
import name.richardson.james.bukkit.utilities.logging.Logger;

public final class AliasHandler {

  private final EbeanServer database;

  private final Logger logger;

  public AliasHandler(final Alias plugin) {
    this.logger = plugin.getCustomLogger();
    this.database = plugin.getDatabase();
  }

  public void associatePlayer(final String playerName, final String address) {
    this.logger.debug(this, "associate-player", playerName, address);
    final PlayerNameRecord playerNameRecord = PlayerNameRecord.findByName(this.database, playerName);
    final InetAddressRecord inetAddressRecord = InetAddressRecord.findByAddress(this.database, address);
    playerNameRecord.updateLastSeen();
    inetAddressRecord.updateLastSeen();

    // link IP address to name
    if (!playerNameRecord.getAddresses().contains(inetAddressRecord)) {
      playerNameRecord.getAddresses().add(inetAddressRecord);
    }

    this.database.save(playerNameRecord);
  }

  public void deassociatePlayer(final String playerName, final String alias) {
    this.logger.debug(this, "deassociate-player", playerName, alias);
    if (PlayerNameRecord.isPlayerKnown(this.database, playerName) && PlayerNameRecord.isPlayerKnown(this.database, alias)) {
      return;
    }
    final PlayerNameRecord playerRecord = PlayerNameRecord.findByName(this.database, playerName);
    final PlayerNameRecord aliasRecord = PlayerNameRecord.findByName(this.database, alias);
    for (final InetAddressRecord record : aliasRecord.getAddresses()) {
      if (playerRecord.getAddresses().contains(record)) {
        playerRecord.getAddresses().remove(record);
      }
    }
    this.database.save(playerRecord);
  }

  public List<InetAddressRecord> getIPAddresses(final Player player) {
    return this.getIPAddresses(player.getName());
  }

  public List<InetAddressRecord> getIPAddresses(final String playerName) {
    final PlayerNameRecord record = PlayerNameRecord.findByName(this.database, playerName);
    return record.getAddresses();
  }

  public List<PlayerNameRecord> getPlayersNames(final InetAddress ip) {
    final InetAddressRecord record = InetAddressRecord.findByAddress(this.database, ip.getHostAddress());
    return record.getPlayerNames();
  }

  public List<PlayerNameRecord> getPlayersNames(final String playerName) {
    final PlayerNameRecord record = PlayerNameRecord.findByName(this.database, playerName);
    final List<PlayerNameRecord> records = new ArrayList<PlayerNameRecord>();
    for (final InetAddressRecord address : record.getAddresses()) {
      records.addAll(address.getPlayerNames());
    }
    return records;
  }

}
