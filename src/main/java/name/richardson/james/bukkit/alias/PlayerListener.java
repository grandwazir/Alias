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
import org.bukkit.event.player.PlayerPreLoginEvent;

import name.richardson.james.bukkit.utilities.internals.Logger;

public class PlayerListener implements Listener {

  /** The logger assigned to this listener */
  private final Logger logger = new Logger(this.getClass());
  
  /** The handler for this listener */
  private final AliasHandler handler;
  
  public PlayerListener(final Alias alias) {
    this.handler = alias.getHandler(this.getClass());
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onPlayerLogin(final PlayerPreLoginEvent event) {
    logger.debug("Recieved " + event.getClass().getSimpleName() + ".");
    final String playerName = event.getName();
    final String address = event.getAddress().getHostAddress();
    this.handler.associatePlayer(playerName, address);
  }

}
