/*******************************************************************************
 * Copyright (c) 2012 James Richardson.
 * 
 * Alias.java is part of Alias.
 * 
 * Alias is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Alias is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Alias. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package name.richardson.james.bukkit.alias;

import java.util.LinkedList;
import java.util.List;

import name.richardson.james.bukkit.alias.persistence.InetAddressRecord;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecord;
import name.richardson.james.bukkit.alias.query.CheckCommand;
import name.richardson.james.bukkit.alias.query.DeleteCommand;
import name.richardson.james.bukkit.utilities.command.CommandManager;
import name.richardson.james.bukkit.utilities.plugin.AbstractPlugin;
import name.richardson.james.bukkit.utilities.plugin.PluginPermissions;

@PluginPermissions(permissions = { "alias" })
public class Alias extends AbstractPlugin {

	/** The handler for this plugin */
	private AliasHandler handler;

	/**
	 * Gets the artifact id.
	 * 
	 * @return the artifact id
	 */
	public String getArtifactID() {
		return "alias";
	}

	/**
	 * Gets the database classes.
	 * 
	 * @return the database classes
	 */
	@Override
	public List<Class<?>> getDatabaseClasses() {
		final List<Class<?>> list = new LinkedList<Class<?>>();
		list.add(InetAddressRecord.class);
		list.add(PlayerNameRecord.class);
		return list;
	}

	/**
	 * Gets the group id.
	 * 
	 * @return the group id
	 */
	@Override
	public String getGroupID() {
		return "name.richardson.james.bukkit";
	}

	public AliasHandler getHandler() {
		if (this.handler == null) {
			this.handler = new AliasHandler(this);
		}
		return this.handler;
	}

	public String getVersion() {
		return this.getDescription().getVersion();
	}

	@Override
	public void onEnable() {
		try {
			this.loadConfiguration();
			this.loadDatabase();
			this.registerCommands();
			this.registerListeners();
			this.setupMetrics();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void registerCommands() {
		final CommandManager manager = new CommandManager("as");
		manager.addCommand(new CheckCommand(this));
		manager.addCommand(new DeleteCommand(this));
	}

	private void registerListeners() {
		new PlayerListener(this);
	}

}
