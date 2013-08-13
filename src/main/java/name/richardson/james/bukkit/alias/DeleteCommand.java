package name.richardson.james.bukkit.alias;

import name.richardson.james.bukkit.utilities.command.AbstractCommand;
import name.richardson.james.bukkit.utilities.command.context.CommandContext;
import name.richardson.james.bukkit.utilities.formatters.colours.ColourScheme;
import name.richardson.james.bukkit.utilities.permissions.PermissionManager;
import name.richardson.james.bukkit.utilities.permissions.Permissions;

import name.richardson.james.bukkit.alias.persistence.PlayerNameRecord;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecordManager;

@Permissions(permissions = {DeleteCommand.PERMISSION_ALL})
public final class DeleteCommand extends AbstractCommand {

	public static final String PERMISSION_ALL = "alias.delete";

	private final PlayerNameRecordManager playerNameRecordManager;

	private String playerName;
	private String targetName;
	private PlayerNameRecord playerRecord;
	private PlayerNameRecord targetRecord;

	public DeleteCommand(PermissionManager permissionManager, PlayerNameRecordManager playerNameRecordManager) {
		super(permissionManager);
		this.playerNameRecordManager = playerNameRecordManager;
	}

	@Override
	public void execute(CommandContext context) {
		if (isAuthorised(context.getCommandSender())) {
			if (!setPlayerNames(context)) return;
			if (!setPlayerRecords(context)) return;
			targetRecord.removeAssociation(playerRecord);
			context.getCommandSender().sendMessage(getColouredMessage(ColourScheme.Style.INFO, "disassociated-player", playerName, targetName));
		} else {
			context.getCommandSender().sendMessage(getColouredMessage(ColourScheme.Style.ERROR, "no-permission"));
		}
	}


	private boolean setPlayerNames(CommandContext context) {
		playerName = null;
		if (context.has(0) && context.has(1)) {
			playerName = context.getString(0);
			targetName = context.getString(1);
			return true;
		} else {
			context.getCommandSender().sendMessage(getColouredMessage(ColourScheme.Style.ERROR, "must-specify-player-names"));
			return false;
		}
	}

	private boolean setPlayerRecords(CommandContext context) {
		playerRecord = playerNameRecordManager.find(playerName);
		targetRecord = playerNameRecordManager.find(targetName);
		if (playerRecord == null || targetRecord == null) {
			context.getCommandSender().sendMessage(getColouredMessage(ColourScheme.Style.WARNING, "players-not-known-to-alias"));
			return false;
		} else {
			return true;
		}
	}

}
