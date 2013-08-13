package name.richardson.james.bukkit.alias;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import name.richardson.james.bukkit.utilities.command.AbstractCommand;
import name.richardson.james.bukkit.utilities.command.context.CommandContext;
import name.richardson.james.bukkit.utilities.formatters.colours.ColourScheme;
import name.richardson.james.bukkit.utilities.formatters.localisation.LocalisedChoiceFormatter;
import name.richardson.james.bukkit.utilities.permissions.PermissionManager;
import name.richardson.james.bukkit.utilities.permissions.Permissions;

import name.richardson.james.bukkit.alias.persistence.PlayerNameRecord;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecordManager;

@Permissions(permissions = {CheckCommand.PERMISSION_ALL})
public final class CheckCommand extends AbstractCommand {

	public static final String PERMISSION_ALL = "alias.check";

	private final DateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy @ K:mm a (z)");
	private final PlayerNameRecordManager playerNameRecordManager;
	private final LocalisedChoiceFormatter choiceFormatter;

	private String playerName;
	private PlayerNameRecord playerRecord;

	public CheckCommand(PermissionManager permissionManager, PlayerNameRecordManager playerNameRecordManager) {
		super(permissionManager);
		this.playerNameRecordManager = playerNameRecordManager;
		this.choiceFormatter = new LocalisedChoiceFormatter();
		this.choiceFormatter.setMessage("checked-aliases-header");
		this.choiceFormatter.setFormats("one-alias", "many-aliases");
		this.choiceFormatter.setLimits(1,2);
	}

	@Override
	public void execute(CommandContext context) {
		if (isAuthorised(context.getCommandSender())) {
			setPlayerName(context);
			if (!setPlayerRecord(context)) return;
			Set<String> aliases = playerRecord.getAliases();
			choiceFormatter.setArguments(aliases.size(), this.playerName);
			context.getCommandSender().sendMessage(choiceFormatter.getColouredMessage(ColourScheme.Style.HEADER));
			context.getCommandSender().sendMessage(getAliasesAsMessages());
		} else {
			context.getCommandSender().sendMessage(getColouredMessage(ColourScheme.Style.ERROR, "no-permission"));
		}
	}

	private String[] getAliasesAsMessages() {
		List<String> messages = new ArrayList<String>();
		for (PlayerNameRecord record : playerRecord.getPlayerNameRecords()) {
			messages.add(getColouredMessage(ColourScheme.Style.INFO, "player-alias", record.getPlayerName(), record.getLastSeen()));
		}
		return messages.toArray(new String[messages.size()]);
	}

	private void setPlayerName(CommandContext context) {
		playerName = null;
		if (context.has(0)) {
			playerName = context.getString(0);
		} else {
			playerName = context.getCommandSender().getName();
		}
	}

	private boolean setPlayerRecord(CommandContext context) {
		playerRecord = playerNameRecordManager.find(playerName);
		if (playerRecord == null) {
			context.getCommandSender().sendMessage(getColouredMessage(ColourScheme.Style.INFO, "player-not-known-to-alias", playerName));
			return false;
		} else {
			return true;
		}
	}

}
