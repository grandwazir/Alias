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

import org.bukkit.entity.Player;

import name.richardson.james.bukkit.util.Handler;

public class AliasHandler extends Handler implements AliasAPI {

  private final Alias plugin;

  public AliasHandler(final Class<?> parentClass, final Alias plugin) {
    super(parentClass);
    this.plugin = plugin;
  }

  public Set<String> getIPAddresses(final Player player) {
    return this.getIPAddresses(player.getName());
  }

  public Set<String> getIPAddresses(final String playerName) {
    final Set<String> set = new HashSet<String>();
    final PlayerNameRecord records = PlayerNameRecord.findByName(this.plugin.getDatabaseHandler(), playerName);
    for (final InetAddressRecord record : records.getAddresses()) {
      set.add(record.getAddress());
    }
    return set;
  }

  public Set<String> getPlayersNames(final InetAddress ip) {
    final Set<String> set = new HashSet<String>();
    final InetAddressRecord records = InetAddressRecord.findByAddress(this.plugin.getDatabaseHandler(), ip.getHostAddress());
    for (final PlayerNameRecord record : records.getPlayerNames()) {
      set.add(record.getPlayerName());
    }
    return set;
  }

}
