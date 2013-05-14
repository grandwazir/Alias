package name.richardson.james.bukkit.alias.query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.avaje.ebean.EbeanServer;

import name.richardson.james.bukkit.alias.Alias;
import name.richardson.james.bukkit.alias.AliasHandler;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecord;
import name.richardson.james.bukkit.utilities.command.AbstractCommand;
import name.richardson.james.bukkit.utilities.command.CommandArgumentException;
import name.richardson.james.bukkit.utilities.command.CommandPermissionException;
import name.richardson.james.bukkit.utilities.command.CommandUsageException;
import name.richardson.james.bukkit.utilities.command.ConsoleCommand;

@ConsoleCommand
public final class DeleteCommand extends AbstractCommand {

  private final AliasHandler handler;

  private String playerName;

  private String targetName;

  private EbeanServer database;

  public DeleteCommand(final Alias plugin) {
    super(plugin);
    this.handler = plugin.getHandler();
    this.database = plugin.getDatabase();
  }

  public void execute(final CommandSender sender) throws CommandArgumentException, CommandPermissionException, CommandUsageException {
    this.handler.deassociatePlayer(this.playerName, this.targetName);
    sender.sendMessage(this.getLocalisation().getMessage(this, "dessociated-player", this.playerName, this.targetName));
  }

  public void parseArguments(final String[] arguments, final CommandSender sender) throws CommandArgumentException {
    if (arguments.length < 2) {
      throw new CommandArgumentException(this.getLocalisation().getMessage(this, "specify-player-names"), null);
    } else {
      this.playerName = arguments[0];
      this.targetName = arguments[1];
    }
  }
  
  public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments) {
    List<String> list = new ArrayList<String>();
    Set<String> temp = new HashSet<String>();
    if (arguments.length < 2) {
      for (Player player : Bukkit.getServer().getOnlinePlayers()) {
        if (arguments.length < 1) {
          temp.add(player.getName());
        } else if (player.getName().startsWith(arguments[0])) {
          temp.add(player.getName());
        }
      }
      if (arguments[0].length() >= 3) {
        temp.addAll(PlayerNameRecord.getPlayersThatStartWith(database, arguments[0]));
      }
    } else if (arguments.length < 3) {
      for (Player player : Bukkit.getServer().getOnlinePlayers()) {
        if (arguments.length < 1) {
          temp.add(player.getName());
        } else if (player.getName().startsWith(arguments[1])) {
          temp.add(player.getName());
        }
      }
      if (arguments[1].length() >= 3) {
        temp.addAll(PlayerNameRecord.getPlayersThatStartWith(database, arguments[0]));
      }
    }
    list.addAll(temp);
    return list;
  }

}
