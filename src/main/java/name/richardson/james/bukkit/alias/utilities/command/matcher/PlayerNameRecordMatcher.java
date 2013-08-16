/*
 * Copyright (c) 2013 James Richardson.
 *
 * PlayerNameRecordMatcher.java is part of Alias.
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

package name.richardson.james.bukkit.alias.utilities.command.matcher;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import name.richardson.james.bukkit.utilities.command.matcher.Matcher;

import name.richardson.james.bukkit.alias.persistence.PlayerNameRecord;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecordManager;

public class PlayerNameRecordMatcher implements Matcher {

	public static int MINIMUM_ARGUMENT_LENGTH = 3;

	private final PlayerNameRecordManager playerNameRecordManager;

	public PlayerNameRecordMatcher(PlayerNameRecordManager playerNameRecordManager) {
		this.playerNameRecordManager = playerNameRecordManager;
	}

	@Override
	public Set<String> matches(String argument) {
		if (argument.length() < MINIMUM_ARGUMENT_LENGTH) return Collections.emptySet();
		TreeSet<String> results = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		argument = argument.toLowerCase(Locale.ENGLISH);
		for (PlayerNameRecord playerRecord : playerNameRecordManager.list(argument)) {
			if (results.size() == Matcher.MAX_MATCHES) break;
			results.add(playerRecord.getPlayerName());
		}
		return results;
	}

}
