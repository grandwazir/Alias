/*******************************************************************************
 * Copyright (c) 2012 James Richardson.
 * 
 * CheckCommand.java is part of Alias.
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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import name.richardson.james.bukkit.alias.Alias;
import name.richardson.james.bukkit.alias.InetAddressRecord;
import name.richardson.james.bukkit.alias.PlayerNameRecord;
import name.richardson.james.bukkit.utilities.command.CommandArgumentException;
import name.richardson.james.bukkit.utilities.command.CommandPermissionException;
import name.richardson.james.bukkit.utilities.command.PluginCommand;
import name.richardson.james.bukkit.utilities.internals.Logger;
import name.richardson.james.bukkit.utilities.persistence.SQLStorage;

public class CheckCommand extends PluginCommand {
  
  /* The style to use for outputting all dates */
  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("k:m MMM d yyyy");

  /* The logger for this class */
  private final Logger logger = new Logger(this.getClass());
  
  /* A reference to the bukkit server */
  private final Server server;

  /* A reference to the SQL storage for the plugin */
  private final SQLStorage storage;

  /* The player who we are looking up in the database */
  private OfflinePlayer player;
  
  /* The IP address that we are looking up in the database */
  private InetAddress address;

  public CheckCommand(final Alias plugin) {
    super(plugin);
    this.storage = plugin.getSQLStorage();
    this.server = plugin.getServer();
    this.registerPermissions();
  }

  public void execute(final CommandSender sender) throws CommandArgumentException, CommandPermissionException, CommandUsageException {
    List<PlayerNameRecord> playerNames = new LinkedList<PlayerNameRecord>();
    List<InetAddressRecord> addresses = new LinkedList<InetAddressRecord>();

    if (this.player != null) {
      if (!sender.hasPermission(this.getPermission(2))) throw new CommandPermissionException(this.getMessage("checkcommand-cannot-search-by-player"), this.getPermission(2));
      addresses = this.lookupPlayerName(this.player.getName());
      for (final InetAddressRecord record : addresses) {
        playerNames.addAll(record.getPlayerNames());
      }
    } else {
      if (!sender.hasPermission(this.getPermission(3))) throw new CommandPermissionException(this.getMessage("checkcommand-cannot-search-by-address"), this.getPermission(3));
      playerNames = this.lookupIPAddress(this.address.toString());
    }

    sender.sendMessage(this.getFormattedPlayerHeader(this.player.getName(), playerNames.size()));
    for (final PlayerNameRecord record : playerNames) {
      final Date date = new Date(record.getLastSeen());
      final String lastSeenString = CheckCommand.DATE_FORMAT.format(date);
      final String[] arguments = { record.getPlayerName(), lastSeenString };
      sender.sendMessage(this.getSimpleFormattedMessage("checkcommand-list-item", arguments));
    }

  }

  public void parseArguments(final String[] arguments, final CommandSender sender) throws CommandArgumentException {
    if (arguments.length == 0) {
      throw new CommandArgumentException(this.getMessage("checkcommand-no-arguments"), this.getMessage("checkcommand-player-search-hint"));
    }
    
    player = null;
    address = null;

    for (final String argument : arguments) {
      if (argument.startsWith("p:")) {
        this.player = this.matchPlayer(argument.replace("p:", ""));
        break;
      } else if (argument.startsWith("a:")) {
        try {
          this.address = InetAddress.getByName(argument.replace("a:", ""));
        } catch (final UnknownHostException e) {
          throw new CommandArgumentException(this.getMessage("checkcommand-specify-valid-address"), this.getMessage("checkcommand-specify-valid-address"));
        }
        break;
      }
    }

  }

  private String getFormattedPlayerHeader(final String name, final int size) {
    final Object[] arguments = { size, name };
    final double[] limits = { 0, 1, 2 };
    final String[] formats = { this.getMessage("no-name").toLowerCase(), this.getMessage("one-name").toLowerCase(), this.getMessage("many-names") };
    return this.getChoiceFormattedMessage("checkcommand-header", arguments, formats, limits);
  }

  private List<PlayerNameRecord> lookupIPAddress(final String address) {
    final InetAddressRecord record = InetAddressRecord.findByAddress(this.storage, address);
    if (record != null) {
      return record.getPlayerNames();
    }
    this.logger.debug("No PlayerNameRecords found, returning empty list");
    return new LinkedList<PlayerNameRecord>();
  }

  private List<InetAddressRecord> lookupPlayerName(final String playerName) {
    final PlayerNameRecord record = PlayerNameRecord.findByName(this.storage, playerName);
    if (record != null) {
      return record.getAddresses();
    }
    this.logger.debug("No InetAddressRecords found, returning empty list");
    return new LinkedList<InetAddressRecord>();
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
    final String wildcardDescription = String.format(this.plugin.getMessage("wildcard-permission-description"), this.getName());
    // create the wildcard permission
    final Permission wildcard = new Permission(prefix + this.getName() + ".*", wildcardDescription, PermissionDefault.OP);
    wildcard.addParent(this.plugin.getRootPermission(), true);
    this.addPermission(wildcard);
    // create the base permission
    final Permission base = new Permission(prefix + this.getName(), this.getMessage("checkcommand-permission-description"), PermissionDefault.OP);
    base.addParent(this.plugin.getRootPermission(), true);
    this.addPermission(base);
    // create permission for searching by player name.
    final Permission player = new Permission(prefix + this.getName() + ".players", this.getMessage("checkcommand-permission-player-description"), PermissionDefault.OP);
    player.addParent(this.plugin.getRootPermission(), true);
    this.addPermission(player);
    // create permission for searching by ip address.
    final Permission address = new Permission(prefix + this.getName() + ".addresses", this.getMessage("checkcommand-permission-address-description"), PermissionDefault.OP);
    address.addParent(this.plugin.getRootPermission(), true);
    this.addPermission(address);
  }

}
