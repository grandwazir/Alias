package name.richardson.james.bukkit.alias;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.avaje.ebean.EbeanServer;

import name.richardson.james.bukkit.alias.Alias;
import name.richardson.james.bukkit.alias.AliasHandler;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecord;
import name.richardson.james.bukkit.utilities.command.AbstractCommand;
import name.richardson.james.bukkit.utilities.command.CommandMatchers;
import name.richardson.james.bukkit.utilities.command.CommandPermissions;
import name.richardson.james.bukkit.utilities.formatters.ChoiceFormatter;
import name.richardson.james.bukkit.utilities.matchers.OfflinePlayerMatcher;

@CommandPermissions(permissions = { "alias.check" })
@CommandMatchers(matchers = { OfflinePlayerMatcher.class })
public final class CheckCommand extends AbstractCommand {

	private final ChoiceFormatter choiceFormatter;

	private final EbeanServer database;

	private final DateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy @ K:mm a (z)");

	private final AliasHandler handler;

	private String playerName;

	public CheckCommand(final Alias plugin) {
		super();
		this.handler = plugin.getHandler();
		this.database = plugin.getDatabase();
		this.choiceFormatter = new ChoiceFormatter();
		this.choiceFormatter.setMessage("notice.check-header");
		this.choiceFormatter.setLimits(0, 1, 2);
		this.choiceFormatter.setFormats(this.getMessage("shared.choice.no-names"), this.getMessage("shared.choice.one-name"),
			this.getMessage("shared.choice.many-names"));
	}

	public void execute(final List<String> arguments, final CommandSender sender) {
		if (arguments.isEmpty()) {
			this.playerName = sender.getName();
		} else {
			this.playerName = arguments.get(0);
		}
		if (PlayerNameRecord.isPlayerKnown(this.database, this.playerName)) {
			final Collection<PlayerNameRecord> alias = this.handler.getPlayersNames(this.playerName);
			this.choiceFormatter.setArguments(alias.size(), this.playerName);
			sender.sendMessage(this.choiceFormatter.getMessage());
			if (!alias.isEmpty()) {
				for (final PlayerNameRecord record : alias) {
					final String message = this.getMessage("notice.check-list-item", record.getPlayerName(), this.dateFormatter.format(record.getLastSeen()));
					sender.sendMessage(message);
				}
			}
		} else {
			sender.sendMessage(this.getMessage("notice.no-names-found"));
		}
	}
}
