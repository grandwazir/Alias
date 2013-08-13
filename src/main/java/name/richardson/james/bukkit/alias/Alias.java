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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import name.richardson.james.bukkit.utilities.command.AbstractCommand;
import name.richardson.james.bukkit.utilities.command.Command;
import name.richardson.james.bukkit.utilities.command.HelpCommand;
import name.richardson.james.bukkit.utilities.command.invoker.CommandInvoker;
import name.richardson.james.bukkit.utilities.command.invoker.FallthroughCommandInvoker;
import name.richardson.james.bukkit.utilities.command.matcher.Matcher;
import name.richardson.james.bukkit.utilities.permissions.Permissions;
import name.richardson.james.bukkit.utilities.persistence.configuration.PluginConfiguration;
import name.richardson.james.bukkit.utilities.persistence.configuration.SimplePluginConfiguration;
import name.richardson.james.bukkit.utilities.plugin.AbstractDatabasePlugin;
import name.richardson.james.bukkit.utilities.plugin.AbstractPlugin;

import name.richardson.james.bukkit.alias.persistence.InetAddressRecord;
import name.richardson.james.bukkit.alias.persistence.InetAddressRecordManager;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecord;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecordManager;
import name.richardson.james.bukkit.alias.utilities.command.matcher.PlayerNameRecordMatcher;

@Permissions(permissions = {Alias.PLUGIN_PERMISSION})
public class Alias extends AbstractDatabasePlugin {

	public static final String PLUGIN_PERMISSION = "alias";
	private PluginConfiguration configuration;
	private InetAddressRecordManager inetAddressRecordManager;
	private PlayerNameRecordManager playerNameRecordManager;

	public String getArtifactID() {
		return "alias";
	}

	@Override
	public List<Class<?>> getDatabaseClasses() {
		final List<Class<?>> list = new LinkedList<Class<?>>();
		list.add(InetAddressRecord.class);
		list.add(PlayerNameRecord.class);
		return list;
	}

	public InetAddressRecordManager getInetAddressRecordManager() {
		return inetAddressRecordManager;
	}

	public PlayerNameRecordManager getPlayerNameRecordManager() {
		return playerNameRecordManager;
	}

	@Override
	public void onEnable() {
		try {
			super.onEnable();
			this.loadConfiguration();
			this.loadDatabase();
			this.registerCommands();
			this.registerListeners();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	protected void loadDatabase()
	throws IOException {
		super.loadDatabase();
		this.playerNameRecordManager = new PlayerNameRecordManager(this.getDatabase());
		this.inetAddressRecordManager = new InetAddressRecordManager(this.getDatabase());

	}

	private void loadConfiguration()
	throws IOException {
		final File file = new File(this.getDataFolder().getPath() + File.separatorChar + AbstractPlugin.CONFIG_NAME);
		final InputStream defaults = this.getResource(CONFIG_NAME);
		this.configuration = new SimplePluginConfiguration(file, defaults);
	}

	private void registerCommands() {
		// create argument matchers
		Matcher playerNameRecordMatcher = new PlayerNameRecordMatcher(getPlayerNameRecordManager());
		// create the commands
		Set<Command> commands = new HashSet<Command>();
		AbstractCommand command = new CheckCommand(getPermissionManager(), getPlayerNameRecordManager());
		command.addMatcher(playerNameRecordMatcher);
		commands.add(command);
		command = new DeleteCommand(getPermissionManager(), getPlayerNameRecordManager());
		command.addMatcher(playerNameRecordMatcher);
		command.addMatcher(playerNameRecordMatcher);
		commands.add(command);
		// create the invoker
		command = new HelpCommand(getPermissionManager(), "as", getDescription(), commands);
		CommandInvoker invoker = new FallthroughCommandInvoker(command);
		invoker.addCommands(commands);
		// bind invoker to plugin command
		getCommand("as").setExecutor(invoker);
	}

	private void registerListeners() {
		new PlayerListener(this, getServer().getPluginManager(), getPlayerNameRecordManager(), getInetAddressRecordManager());
	}

}
