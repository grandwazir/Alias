package name.richardson.james.bukkit.alias.query;

import org.bukkit.command.CommandSender;

import name.richardson.james.bukkit.alias.Alias;
import name.richardson.james.bukkit.alias.AliasHandler;
import name.richardson.james.bukkit.utilities.command.AbstractCommand;
import name.richardson.james.bukkit.utilities.command.CommandArgumentException;
import name.richardson.james.bukkit.utilities.command.CommandPermissionException;
import name.richardson.james.bukkit.utilities.command.CommandUsageException;
import name.richardson.james.bukkit.utilities.command.ConsoleCommand;

@ConsoleCommand
public final class DeleteCommand extends AbstractCommand {

  private String playerName;
  
  private String targetName;

  private AliasHandler handler;

  public DeleteCommand(Alias plugin) {
    super(plugin, false);
    this.handler = plugin.getHandler();
  }

  public void execute(CommandSender sender) throws CommandArgumentException, CommandPermissionException, CommandUsageException {
    this.handler.deassociatePlayer(playerName, targetName);
    sender.sendMessage(this.getLocalisation().getMessage(this, "dessociated-player", playerName, targetName));
    this.getLogger().info(this, "used", playerName, targetName, sender.getName());
  }

  public void parseArguments(String[] arguments, CommandSender sender) throws CommandArgumentException {
    if (arguments.length < 2) {
      throw new CommandArgumentException(this.getLocalisation().getMessage(this, "specify-player-names"), null);
    } else {
      this.playerName = arguments[0];
      this.targetName = arguments[1];
    }
  }

}
