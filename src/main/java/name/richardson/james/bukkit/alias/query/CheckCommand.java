package name.richardson.james.bukkit.alias.query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.avaje.ebean.EbeanServer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import name.richardson.james.bukkit.alias.Alias;
import name.richardson.james.bukkit.alias.AliasHandler;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecord;
import name.richardson.james.bukkit.utilities.command.AbstractCommand;
import name.richardson.james.bukkit.utilities.command.CommandArgumentException;
import name.richardson.james.bukkit.utilities.command.CommandPermissionException;
import name.richardson.james.bukkit.utilities.command.CommandUsageException;
import name.richardson.james.bukkit.utilities.command.ConsoleCommand;
import name.richardson.james.bukkit.utilities.formatters.ChoiceFormatter;

@ConsoleCommand
public final class CheckCommand extends AbstractCommand {

  private final ChoiceFormatter choiceFormatter;

  private final EbeanServer database;

  private final DateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy @ K:mm a (z)");

  private final AliasHandler handler;

  private String playerName;

  public CheckCommand(final Alias plugin) {
    super(plugin);
    this.handler = plugin.getHandler();
    this.database = plugin.getDatabase();
    this.choiceFormatter = new ChoiceFormatter(this.getLocalisation());
    this.choiceFormatter.setMessage(this, "header");
    this.choiceFormatter.setLimits(0, 1, 2);
    this.choiceFormatter.setFormats(this.getLocalisation().getMessage(this, "no-names"), this.getLocalisation().getMessage(this, "one-name"), this.getLocalisation().getMessage(this, "many-names"));
  }

  public void execute(final CommandSender sender) throws CommandArgumentException, CommandPermissionException, CommandUsageException {
    if (PlayerNameRecord.isPlayerKnown(this.database, this.playerName)) {
      final Collection<PlayerNameRecord> alias = this.handler.getPlayersNames(this.playerName);
      this.choiceFormatter.setArguments(alias.size(), this.playerName);
      sender.sendMessage(this.choiceFormatter.getMessage());
      if (!alias.isEmpty()) {
        for (final PlayerNameRecord record : alias) {
          final String message = this.getLocalisation().getMessage(this, "list-item", record.getPlayerName(), this.dateFormatter.format(record.getLastSeen()));
          sender.sendMessage(message);
        }
      }
    } else {
      sender.sendMessage(this.getLocalisation().getMessage(this, "no-names-found"));
    }
  }

  public void parseArguments(final String[] arguments, final CommandSender sender) throws CommandArgumentException {
    if (arguments.length == 0) {
      this.playerName = sender.getName();
    } else {
      this.playerName = arguments[0];
    }
  }
  
  public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments) {
    List<String> list = new ArrayList<String>();
    Set<String> temp = new HashSet<String>();
    if (arguments.length <= 1) {
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
    }
    list.addAll(temp);
    return list;
  }

}
