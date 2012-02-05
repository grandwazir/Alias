package name.richardson.james.bukkit.alias.query;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import name.richardson.james.bukkit.alias.Alias;
import name.richardson.james.bukkit.alias.AliasHandler;
import name.richardson.james.bukkit.alias.DatabaseHandler;
import name.richardson.james.bukkit.alias.InetAddressRecord;
import name.richardson.james.bukkit.alias.PlayerNameRecord;
import name.richardson.james.bukkit.util.Plugin;
import name.richardson.james.bukkit.util.command.CommandArgumentException;
import name.richardson.james.bukkit.util.command.CommandPermissionException;
import name.richardson.james.bukkit.util.command.CommandUsageException;
import name.richardson.james.bukkit.util.command.PlayerCommand;

public class CheckCommand extends PlayerCommand {

  public static final String NAME = "check";
  public static final String DESCRIPTION = "Check the database for associated records.";
  public static final String PERMISSION_DESCRIPTION = "Allow users to check the database for associated records.";
  public static final String USAGE = "p:[player] | a:[address]";
  
  public static final Permission PERMISSION = new Permission("alias.check", CheckCommand.PERMISSION_DESCRIPTION, PermissionDefault.OP);
  
  private DatabaseHandler database;
  
  public CheckCommand(Alias plugin) {
    super(plugin, CheckCommand.NAME, CheckCommand.DESCRIPTION, CheckCommand.USAGE, CheckCommand.PERMISSION_DESCRIPTION, PERMISSION);
    this.database = plugin.getDatabaseHandler();
  }

  @Override
  public void execute(CommandSender sender, Map<String, Object> arguments) throws CommandArgumentException, CommandPermissionException, CommandUsageException {
    
    if (arguments.containsKey("player")) {
      final String playerName = (String) arguments.get("player");
      List<InetAddressRecord> records = this.lookupPlayerName(playerName);
      sender.sendMessage(String.format(ChatColor.LIGHT_PURPLE + "%s has used %d IP addresses:", playerName, records.size()));
      final DateFormat dateFormat = new SimpleDateFormat("k:m MMM d y");
      for (InetAddressRecord record : records) {
        final Date date = new Date(record.getLastSeen());
        final String lastSeenString = dateFormat.format(date);
        sender.sendMessage(String.format(ChatColor.YELLOW + "- %s (%s)", record.getAddress(), lastSeenString));
      }
    } else if (arguments.containsKey("address")) {
      final InetAddress address = (InetAddress) arguments.get("address");
      List<PlayerNameRecord> records = this.lookupIPAddress(address.getHostAddress());
      sender.sendMessage(String.format(ChatColor.LIGHT_PURPLE + "%s has %d associated player names:", address, records.size()));
      final DateFormat dateFormat = new SimpleDateFormat("k:m MMM d y/as ch");
      for (PlayerNameRecord record : records) {
        final Date date = new Date(record.getLastSeen());
        final String lastSeenString = dateFormat.format(date);
        sender.sendMessage(String.format(ChatColor.YELLOW + "- %s (%s)", record.getPlayerName(), lastSeenString));
      }
    } else {
      throw new CommandArgumentException("You must specify a player name or an address!", "If the player is online, you can type part of the name.");
    }
    
  }
  
  public Map<String, Object> parseArguments(final List<String> arguments) throws CommandArgumentException {
    final Map<String, Object> map = new HashMap<String, Object>();
  
    for (String argument : arguments) {
      if (argument.startsWith("p:")) {
        map.put("player", argument.replace("p:", ""));
        break;
      } else if (argument.startsWith("a:")) {
        try {
          final InetAddress address = InetAddress.getByName(argument.replace("a:", ""));
          map.put("address", address);
        } catch (UnknownHostException e) {
          throw new CommandArgumentException("You must specify a valid IP address!", "Can be either a hostname or an actual address.");
        }
        break;
      }
    }
    
    
    if (map.isEmpty()) throw new CommandArgumentException("You must specify a player name or an address!", "If the player is online, you can type part of the name.");
    return map;
  }
  
  private List<PlayerNameRecord> lookupIPAddress(String address) {
    InetAddressRecord record = InetAddressRecord.findByAddress(database, address);
    if (record != null) return record.getPlayerNames();
    logger.debug("No PlayerNameRecords found, returning empty list");
    return new LinkedList<PlayerNameRecord>();
  }

  private List<InetAddressRecord> lookupPlayerName(String playerName) {
    PlayerNameRecord record = PlayerNameRecord.findByName(database, playerName);
    if (record != null) return record.getAddresses();
    logger.debug("No InetAddressRecords found, returning empty list");
    return new LinkedList<InetAddressRecord>();
  }

}
