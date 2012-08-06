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

  public List<String> getIPAddresses(final Player player) {
    return this.getIPAddresses(player.getName());
  }

  public List<String> getIPAddresses(final String playerName) {
    final List<String> list = new ArrayList<String>();
    final PlayerNameRecord records = PlayerNameRecord.findByName(database, playerName);
    for (final InetAddressRecord record : records.getAddresses()) {
      list.add(record.getAddress());
    }
    return list;
  }

  public List<String> getPlayersNames(final InetAddress ip) {
    final List<String> list = new ArrayList<String>();
    final InetAddressRecord records = InetAddressRecord.findByAddress(database, ip.getHostAddress());
    for (final PlayerNameRecord record : records.getPlayerNames()) {
      list.add(record.getPlayerName());
    }
    return list;
  }
  
  public void associatePlayer(String playerName, String address) {
    this.logger.debug(this, "associate-player", playerName, address);
    final PlayerNameRecord playerNameRecord = PlayerNameRecord.findByName(database, playerName);
    final InetAddressRecord inetAddressRecord = InetAddressRecord.findByAddress(database, address);
    final long now = System.currentTimeMillis();
    playerNameRecord.setLastSeen(now);
    inetAddressRecord.setLastSeen(now);

    // link IP address to name
    if (!playerNameRecord.getAddresses().contains(inetAddressRecord)) {
      playerNameRecord.getAddresses().add(inetAddressRecord);
    }

    database.save(playerNameRecord);
  }
  
  public void deassociatePlayer(String playerName, String alias) {
    this.logger.debug(this, "deassociate-player", playerName, alias);
    if (PlayerNameRecord.isPlayerKnown(database, playerName) && PlayerNameRecord.isPlayerKnown(database, alias)) return;
    final PlayerNameRecord playerRecord = PlayerNameRecord.findByName(database, playerName);
    final PlayerNameRecord aliasRecord  = PlayerNameRecord.findByName(database, alias);
    for (InetAddressRecord record : aliasRecord.getAddresses()) {
      if (playerRecord.getAddresses().contains(record)) {
        playerRecord.getAddresses().remove(record);
      }
    }
    database.save(playerRecord);
  }
  

}
