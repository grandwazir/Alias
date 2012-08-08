package name.richardson.james.bukkit.alias.query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;

import com.avaje.ebean.EbeanServer;

import org.bukkit.command.CommandSender;

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
    super(plugin, false);
    this.handler = plugin.getHandler();
    this.database = plugin.getDatabase();
    this.choiceFormatter = new ChoiceFormatter(this.getLocalisation());
    this.choiceFormatter.setMessage(this, "header");
    this.choiceFormatter.setLimits(0, 1, 2);
    this.choiceFormatter.setFormats(this.getLocalisation().getMessage(this, "no-names"), this.getLocalisation().getMessage(this, "one-name"), this.getLocalisation().getMessage(this, "many-names"));
  }

  public void execute(final CommandSender sender) throws CommandArgumentException, CommandPermissionException, CommandUsageException {
    this.getLogger().info(this, "used", sender.getName(), this.playerName);
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

}
