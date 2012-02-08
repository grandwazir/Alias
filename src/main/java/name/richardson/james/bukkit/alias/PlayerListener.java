/*******************************************************************************
 * Copyright (c) 2012 James Richardson.
 * 
 * PlayerListener.java is part of Alias.
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

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import name.richardson.james.bukkit.util.Database;
import name.richardson.james.bukkit.util.Logger;

public class PlayerListener implements Listener {

  private final Logger logger = new Logger(PlayerListener.class);
  private final Database database;

  public PlayerListener(final Alias alias) {
    this.database = alias.getDatabaseHandler();
  }

  private InetAddressRecord getInetAddressRecord(final String address) {
    if (!InetAddressRecord.isAddressKnown(this.database, address)) {
      final InetAddressRecord record = new InetAddressRecord();
      record.setAddress(address);
      record.updateLastSeen();
      this.database.save(record);
    }
    return InetAddressRecord.findByAddress(this.database, address);
  }

  private PlayerNameRecord getPlayerNameRecord(final String playerName) {
    if (!PlayerNameRecord.isPlayerKnown(this.database, playerName)) {
      final PlayerNameRecord record = new PlayerNameRecord();
      record.setPlayerName(playerName);
      record.updateLastSeen();
      this.database.save(record);
    }
    return PlayerNameRecord.findByName(this.database, playerName);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerLogin(final PlayerJoinEvent event) {
    final String playerName = event.getPlayer().getName();
    final String address = event.getPlayer().getAddress().getAddress().getHostAddress();
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

    this.database.save(playerNameRecord);
    this.database.save(inetAddressRecord);

  }

}
