/*
 * Copyright (c) 2013 James Richardson.
 *
 * DeleteCommand.java is part of Alias.
 *
 * Alias is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Alias is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Alias. If not, see <http://www.gnu.org/licenses/>.
 */

package name.richardson.james.bukkit.alias;

import org.bukkit.permissions.Permissible;

import name.richardson.james.bukkit.utilities.command.AbstractCommand;
import name.richardson.james.bukkit.utilities.command.context.CommandContext;
import name.richardson.james.bukkit.utilities.formatters.ColourFormatter;
import name.richardson.james.bukkit.utilities.formatters.DefaultColourFormatter;
import name.richardson.james.bukkit.utilities.localisation.Localisation;
import name.richardson.james.bukkit.utilities.localisation.ResourceBundleByClassLocalisation;

import name.richardson.james.bukkit.alias.persistence.PlayerNameRecord;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecordManager;

public final class DeleteCommand extends AbstractCommand {

	public static final String PERMISSION_ALL = "alias.delete";

	private final PlayerNameRecordManager playerNameRecordManager;
	private final ColourFormatter colourFormatter = new DefaultColourFormatter();
	private final Localisation localisation = new ResourceBundleByClassLocalisation(DeleteCommand.class);

	private String playerName;
	private String targetName;
	private PlayerNameRecord playerRecord;
	private PlayerNameRecord targetRecord;

	public DeleteCommand(PlayerNameRecordManager playerNameRecordManager) {
		this.playerNameRecordManager = playerNameRecordManager;
	}

	@Override
	public void execute(CommandContext context) {
		if (isAuthorised(context.getCommandSender())) {
			if (!setPlayerNames(context)) return;
			if (!setPlayerRecords(context)) return;
			targetRecord.removeAssociation(playerRecord);
			context.getCommandSender().sendMessage(colourFormatter.format(localisation.getMessage("disassociated-player"), ColourFormatter.FormatStyle.INFO, playerName, targetName));
		} else {
			context.getCommandSender().sendMessage(colourFormatter.format(localisation.getMessage("no-permission"), ColourFormatter.FormatStyle.ERROR));
		}
	}

	@Override
	public boolean isAuthorised(Permissible permissible) {
		if (permissible.hasPermission(PERMISSION_ALL)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean setPlayerNames(CommandContext context) {
		playerName = null;
		if (context.has(0) && context.has(1)) {
			playerName = context.getString(0);
			targetName = context.getString(1);
			return true;
		} else {
			context.getCommandSender().sendMessage(colourFormatter.format(localisation.getMessage("must-specify-player-names"), ColourFormatter.FormatStyle.ERROR, playerName));
			return false;
		}
	}

	private boolean setPlayerRecords(CommandContext context) {
		playerRecord = playerNameRecordManager.find(playerName);
		targetRecord = playerNameRecordManager.find(targetName);
		if (playerRecord == null || targetRecord == null) {
			context.getCommandSender().sendMessage(colourFormatter.format(localisation.getMessage("player-not-known-to-alias"), ColourFormatter.FormatStyle.INFO, playerName));
			return false;
		} else {
			return true;
		}
	}

}
