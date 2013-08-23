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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.permissions.Permissible;

import name.richardson.james.bukkit.utilities.command.AbstractCommand;
import name.richardson.james.bukkit.utilities.command.context.CommandContext;
import name.richardson.james.bukkit.utilities.formatters.*;
import name.richardson.james.bukkit.utilities.localisation.Localisation;
import name.richardson.james.bukkit.utilities.localisation.ResourceBundleByClassLocalisation;

import name.richardson.james.bukkit.alias.persistence.PlayerNameRecord;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecordManager;
import name.richardson.james.bukkit.alias.utilities.formatters.AliasCountChoiceFormatter;

public final class CheckCommand extends AbstractCommand {

	public static final String PERMISSION_ALL = "alias.check";

	private static final String NO_PERMISSION_KEY = "no-permission";
	private static final String PLAYER_ALIAS_KEY = "player-alias";
	private static final String PLAYER_NOT_KNOWN_TO_ALIAS_KEY = "player-not-known-to-alias";

	private final ColourFormatter colourFormatter = new DefaultColourFormatter();
	private final Localisation localisation = new ResourceBundleByClassLocalisation(CheckCommand.class);
	private final PlayerNameRecordManager playerNameRecordManager;
	private final TimeFormatter timeFormatter = new ApproximateTimeFormatter();
	private final ChoiceFormatter choiceFormatter = new AliasCountChoiceFormatter();

	private String playerName;
	private PlayerNameRecord playerRecord;

	public CheckCommand(PlayerNameRecordManager playerNameRecordManager) {
		this.playerNameRecordManager = playerNameRecordManager;
		this.choiceFormatter.setMessage(colourFormatter.format(localisation.getMessage("checked-aliases-header"), ColourFormatter.FormatStyle.HEADER));

	}

	@Override
	public void execute(CommandContext context) {
		if (isAuthorised(context.getCommandSender())) {
			setPlayerName(context);
			if (!setPlayerRecord(context)) return;
			Set<String> aliases = playerRecord.getAliases();
			choiceFormatter.setArguments(aliases.size(), this.playerName);
			context.getCommandSender().sendMessage(choiceFormatter.getMessage());
			context.getCommandSender().sendMessage(getAliasesAsMessages());
		} else {
			context.getCommandSender().sendMessage(colourFormatter.format(localisation.getMessage(NO_PERMISSION_KEY), ColourFormatter.FormatStyle.ERROR));
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
		Set<PlayerNameRecord> records = new HashSet<PlayerNameRecord>();
		records.addAll(playerRecord.getPlayerNameRecords());
		for (PlayerNameRecord record : records) {
			String duration = timeFormatter.getHumanReadableDuration(record.getLastSeen().getTime());
			messages.add(colourFormatter.format(localisation.getMessage(PLAYER_ALIAS_KEY), ColourFormatter.FormatStyle.INFO, record.getPlayerName(), duration));
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
			context.getCommandSender().sendMessage(colourFormatter.format(localisation.getMessage(PLAYER_NOT_KNOWN_TO_ALIAS_KEY), ColourFormatter.FormatStyle.INFO, playerName));
			return false;
		} else {
			return true;
		}
	}

}
