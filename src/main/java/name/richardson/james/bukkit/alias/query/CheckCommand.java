package name.richardson.james.bukkit.alias.query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

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

  private final DateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy @ K:mm a, (z)");
  
  private final ChoiceFormatter choiceFormatter;
  
  private String playerName;
  
  private AliasHandler handler;

  private EbeanServer database;
  
  public CheckCommand(Alias plugin) {
    super(plugin, false);
    this.handler = plugin.getHandler();
    this.database = plugin.getDatabase();
    this.choiceFormatter = new ChoiceFormatter(this.getLocalisation());
    this.choiceFormatter.setMessage(this, "header");
    this.choiceFormatter.setLimits(0,1,2);
    this.choiceFormatter.setFormats(
      this.getLocalisation().getMessage(this, "no-names"),
      this.getLocalisation().getMessage(this, "one-name"),
      this.getLocalisation().getMessage(this, "many-names")
    );
  }

  public void execute(CommandSender sender) throws CommandArgumentException, CommandPermissionException, CommandUsageException {
    this.getLogger().info(this, "used", sender.getName(), playerName);
    if (PlayerNameRecord.isPlayerKnown(database, playerName)) {
      final List<PlayerNameRecord> alias = handler.getPlayersNames(playerName);
      if (!alias.isEmpty()) {
        this.choiceFormatter.setArguments(alias.size(), playerName);
        sender.sendMessage(this.choiceFormatter.getMessage());
        for (PlayerNameRecord record : alias) {
          String message = this.getLocalisation().getMessage(this, "list-item", record.getPlayerName(), this.dateFormatter.format(record.getLastSeen()));
          sender.sendMessage(message);
        }
      } 
    } else {
      sender.sendMessage(this.getLocalisation().getMessage(this, "no-names-found"));
    }
  }

  public void parseArguments(String[] arguments, CommandSender sender) throws CommandArgumentException {
    if (arguments.length == 0) {
      this.playerName = sender.getName();
    } else {
      this.playerName = arguments[0];
    }
  }
  
  

}
