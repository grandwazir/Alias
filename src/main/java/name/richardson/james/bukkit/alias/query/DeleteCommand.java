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

  private final AliasHandler handler;

  private String playerName;

  private String targetName;

  public DeleteCommand(final Alias plugin) {
    super(plugin);
    this.handler = plugin.getHandler();
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

}
