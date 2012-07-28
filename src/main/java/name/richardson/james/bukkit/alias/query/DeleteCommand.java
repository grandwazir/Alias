/*******************************************************************************
 * Copyright (c) 2012 James Richardson.
 * 
 * DeleteCommand.java is part of Alias.
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
package name.richardson.james.bukkit.alias.query;

import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import name.richardson.james.bukkit.alias.Alias;
import name.richardson.james.bukkit.alias.AliasHandler;
import name.richardson.james.bukkit.utilities.command.CommandArgumentException;
import name.richardson.james.bukkit.utilities.command.CommandPermissionException;
import name.richardson.james.bukkit.utilities.command.CommandUsageException;
import name.richardson.james.bukkit.utilities.command.PluginCommand;
import name.richardson.james.bukkit.utilities.internals.Logger;

public class DeleteCommand extends PluginCommand {
  
  /* The logger for this class */
  private final Logger logger = new Logger(this.getClass());
  
  /* A reference to the bukkit server */
  private final Server server;

  /* The player who we are looking up in the database */
  private OfflinePlayer player;
  
  private AliasHandler handler;

  private OfflinePlayer alias;
  
 
  public DeleteCommand(final Alias plugin) {
    super(plugin);
    this.handler = plugin.getHandler(this.getClass());
    this.server = plugin.getServer();
    this.registerPermissions();
  }

  public void execute(final CommandSender sender) throws CommandArgumentException, CommandPermissionException, CommandUsageException {
    String[] arguments = {player.getName(), alias.getName(), sender.getName()};
    handler.deassociatePlayer(player.getName(), alias.getName());
    logger.info(this.getSimpleFormattedMessage("log-success", arguments));
    String[] arguments1 = {player.getName(), alias.getName()};
    sender.sendMessage(this.getSimpleFormattedMessage("success", arguments1));
  }

  public void parseArguments(final String[] arguments, final CommandSender sender) throws CommandArgumentException {
    if (arguments.length != 2) {
      throw new CommandArgumentException(this.getMessage("no-arguments"), this.getMessage("player-search-hint"));
    }
    
    player = null;
    alias = null;

    this.player = this.matchPlayer(arguments[0]);
    this.alias = this.matchPlayer(arguments[1]);

  }

  private OfflinePlayer matchPlayer(final String name) {
    final List<Player> players = this.server.matchPlayer(name);
    if (players.isEmpty()) {
      return this.server.getOfflinePlayer(name);
    } else {
      return players.get(0);
    }
  }

  private void registerPermissions() {
    final String prefix = this.plugin.getDescription().getName().toLowerCase() + ".";
    // create the base permission
    final Permission base = new Permission(prefix + this.getName(), this.getMessage("permission-description"), PermissionDefault.OP);
    base.addParent(this.plugin.getRootPermission(), true);
    this.addPermission(base);
  }

}
