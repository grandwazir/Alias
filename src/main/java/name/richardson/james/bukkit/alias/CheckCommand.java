/*
 * Copyright (c) 2013 James Richardson.
 *
 * CheckCommand.java is part of Alias.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.permissions.Permissible;

import name.richardson.james.bukkit.utilities.command.AbstractCommand;
import name.richardson.james.bukkit.utilities.command.context.CommandContext;
import name.richardson.james.bukkit.utilities.formatters.ApproximateTimeFormatter;
import name.richardson.james.bukkit.utilities.formatters.ColourFormatter;
import name.richardson.james.bukkit.utilities.formatters.DefaultColourFormatter;
import name.richardson.james.bukkit.utilities.formatters.TimeFormatter;
import name.richardson.james.bukkit.utilities.localisation.Localisation;
import name.richardson.james.bukkit.utilities.localisation.ResourceBundleByClassLocalisation;

import name.richardson.james.bukkit.alias.persistence.PlayerNameRecord;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecordManager;

public final class CheckCommand extends AbstractCommand {

	public static final String PERMISSION_ALL = "alias.check";

	private final ColourFormatter colourFormatter = new DefaultColourFormatter();
	private final Localisation localisation = new ResourceBundleByClassLocalisation(CheckCommand.class);
	private final PlayerNameRecordManager playerNameRecordManager;
	private final TimeFormatter timeFormatter = new ApproximateTimeFormatter();

	private String playerName;
	private PlayerNameRecord playerRecord;

	public CheckCommand(PlayerNameRecordManager playerNameRecordManager) {
		this.playerNameRecordManager = playerNameRecordManager;
//		this.choiceFormatter = new LocalisedChoiceFormatter();
//		this.choiceFormatter.setMessage("checked-aliases-header");
//		this.choiceFormatter.setFormats("one-alias", "many-aliases");
//		this.choiceFormatter.setLimits(1,2);
	}

	@Override
	public void execute(CommandContext context) {
		if (isAuthorised(context.getCommandSender())) {
			setPlayerName(context);
			if (!setPlayerRecord(context)) return;
			Set<String> aliases = playerRecord.getAliases();
//			choiceFormatter.setArguments(aliases.size(), this.playerName);
//			context.getCommandSender().sendMessage(choiceFormatter.getColouredMessage(ColourScheme.Style.HEADER));
			context.getCommandSender().sendMessage(getAliasesAsMessages());
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

	private String[] getAliasesAsMessages() {
		List<String> messages = new ArrayList<String>();
		for (PlayerNameRecord record : playerRecord.getPlayerNameRecords()) {
			String duration = timeFormatter.getHumanReadableDuration(record.getLastSeen().getTime());
			messages.add(colourFormatter.format(localisation.getMessage("player-alias"), ColourFormatter.FormatStyle.INFO, record.getPlayerName(), duration));
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
			context.getCommandSender().sendMessage(colourFormatter.format(localisation.getMessage("player-not-known-to-alias"), ColourFormatter.FormatStyle.INFO, playerName));
			return false;
		} else {
			return true;
		}
	}

}
