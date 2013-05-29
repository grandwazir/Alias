package name.richardson.james.bukkit.alias.query;

import java.util.List;

import org.bukkit.command.CommandSender;

import name.richardson.james.bukkit.alias.Alias;
import name.richardson.james.bukkit.alias.AliasHandler;
import name.richardson.james.bukkit.utilities.command.AbstractCommand;
import name.richardson.james.bukkit.utilities.command.CommandMatchers;
import name.richardson.james.bukkit.utilities.command.CommandPermissions;
import name.richardson.james.bukkit.utilities.matchers.OfflinePlayerMatcher;

@CommandPermissions(permissions = { "alias.delete" })
@CommandMatchers(matchers = { OfflinePlayerMatcher.class, OfflinePlayerMatcher.class })
public final class DeleteCommand extends AbstractCommand {

	private final AliasHandler handler;

	private String playerName;

	private String targetName;

	public DeleteCommand(final Alias plugin) {
		super();
		this.handler = plugin.getHandler();
	}

	public void execute(final List<String> arguments, final CommandSender sender) {
		if (arguments.size() < 2) {
			sender.sendMessage(this.getMessage("specify-player-names"));
		} else {
			this.playerName = arguments.get(0);
			this.targetName = arguments.get(0);
		}
		this.handler.deassociatePlayer(this.playerName, this.targetName);
		sender.sendMessage(this.getMessage("deletecommand.dessociated-player", this.playerName, this.targetName));
	}

}
