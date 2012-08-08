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
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
    if (!PlayerNameRecord.isPlayerKnown(this.database, playerName) || !PlayerNameRecord.isPlayerKnown(this.database, alias)) return;
    this.logger.debug(this, "deassociate-player", playerName, alias);
    final PlayerNameRecord playerRecord = PlayerNameRecord.findByName(this.database, playerName);
    final PlayerNameRecord aliasRecord = PlayerNameRecord.findByName(this.database, alias);
    System.out.print(playerRecord.getAddresses().size());
    for (final InetAddressRecord address : playerRecord.getAddresses()) {
      if (address.getPlayerNames().contains(aliasRecord)) {
        aliasRecord.getAddresses().remove(address);
      }
    }
    database.save(aliasRecord);
  }

  public Collection<InetAddressRecord> getIPAddresses(final Player player) {
    return this.getIPAddresses(player.getName());
  }

  public Collection<InetAddressRecord> getIPAddresses(final String playerName) {
    final PlayerNameRecord record = PlayerNameRecord.findByName(this.database, playerName);
    return new LinkedHashSet<InetAddressRecord>(record.getAddresses());
  }

  public Collection<PlayerNameRecord> getPlayersNames(final InetAddress ip) {
    final InetAddressRecord record = InetAddressRecord.findByAddress(this.database, ip.getHostAddress());
    return new LinkedHashSet<PlayerNameRecord>(record.getPlayerNames());
  }

  public Collection<PlayerNameRecord> getPlayersNames(final String playerName) {
    final PlayerNameRecord record = PlayerNameRecord.findByName(this.database, playerName);
    final Set<PlayerNameRecord> records = new LinkedHashSet<PlayerNameRecord>();
    for (final InetAddressRecord address : record.getAddresses()) {
      records.addAll(address.getPlayerNames());
    }
    return records;
  }

}
