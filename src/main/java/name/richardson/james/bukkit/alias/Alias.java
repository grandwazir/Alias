/*
 * Copyright (c) 2013 James Richardson.
 *
 * Alias.java is part of Alias.
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.config.ServerConfig;

import name.richardson.james.bukkit.utilities.command.AbstractCommand;
import name.richardson.james.bukkit.utilities.command.Command;
import name.richardson.james.bukkit.utilities.command.HelpCommand;
import name.richardson.james.bukkit.utilities.command.invoker.CommandInvoker;
import name.richardson.james.bukkit.utilities.command.invoker.FallthroughCommandInvoker;
import name.richardson.james.bukkit.utilities.command.matcher.Matcher;
import name.richardson.james.bukkit.utilities.logging.PluginLoggerFactory;
import name.richardson.james.bukkit.utilities.persistence.configuration.PluginConfiguration;
import name.richardson.james.bukkit.utilities.persistence.configuration.SimplePluginConfiguration;
import name.richardson.james.bukkit.utilities.persistence.database.DatabaseLoader;
import name.richardson.james.bukkit.utilities.persistence.database.DatabaseLoaderFactory;
import name.richardson.james.bukkit.utilities.persistence.database.SimpleDatabaseConfiguration;
import name.richardson.james.bukkit.utilities.updater.MavenPluginUpdater;
import name.richardson.james.bukkit.utilities.updater.PluginUpdater;

import name.richardson.james.bukkit.alias.persistence.InetAddressRecord;
import name.richardson.james.bukkit.alias.persistence.InetAddressRecordManager;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecord;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecordManager;
import name.richardson.james.bukkit.alias.utilities.command.matcher.PlayerNameRecordMatcher;

public class Alias extends JavaPlugin {

	private static final String CONFIG_NAME = "config.yml";
	private static final String DATABASE_CONFIG_NAME = "database.yml";

	private PluginConfiguration configuration;
	private EbeanServer database;
	private InetAddressRecordManager inetAddressRecordManager;
	private PlayerNameRecordManager playerNameRecordManager;
	private final Logger logger = PluginLoggerFactory.getLogger(Alias.class);

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
			this.loadConfiguration();
			this.loadDatabase();
			this.registerCommands();
			this.registerListeners();
			this.setupMetrics();
			this.updatePlugin();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void setupMetrics()
	throws IOException {
		if (configuration.isCollectingStats()) {
			new MetricsListener(this);
		}
	}

	private void loadConfiguration()
	throws IOException {
		final File file = new File(this.getDataFolder().getPath() + File.separatorChar + CONFIG_NAME);
		final InputStream defaults = this.getResource(CONFIG_NAME);
		this.configuration = new SimplePluginConfiguration(file, defaults);
		this.logger.setLevel(configuration.getLogLevel());
	}

	private void loadDatabase()
	throws IOException {
		ServerConfig serverConfig = new ServerConfig();
		getServer().configureDbConfig(serverConfig);
		serverConfig.setClasses(Arrays.asList(PlayerNameRecord.class, InetAddressRecord.class));
		final File file = new File(this.getDataFolder().getPath() + File.separatorChar + DATABASE_CONFIG_NAME);
		final InputStream defaults = this.getResource(DATABASE_CONFIG_NAME);
		final SimpleDatabaseConfiguration configuration = new SimpleDatabaseConfiguration(file, defaults, this.getName(), serverConfig);
		final DatabaseLoader loader = DatabaseLoaderFactory.getDatabaseLoader(configuration);
		loader.initalise();
		this.database = loader.getEbeanServer();
		this.playerNameRecordManager = new PlayerNameRecordManager(this.getDatabase());
		this.inetAddressRecordManager = new InetAddressRecordManager(this.getDatabase());
	}

	public EbeanServer getDatabase() {
		return database;
	}

	private void registerCommands() {
		// create argument matchers
		Matcher playerNameRecordMatcher = new PlayerNameRecordMatcher(getPlayerNameRecordManager());
		// create the commands
		Set<Command> commands = new HashSet<Command>();
		AbstractCommand command = new CheckCommand(getPlayerNameRecordManager());
		command.addMatcher(playerNameRecordMatcher);
		commands.add(command);
		command = new DeleteCommand(getPlayerNameRecordManager());
		command.addMatcher(playerNameRecordMatcher);
		command.addMatcher(playerNameRecordMatcher);
		commands.add(command);
		// create the invoker
		command = new HelpCommand("as", commands);
		CommandInvoker invoker = new FallthroughCommandInvoker(command);
		invoker.addCommands(commands);
		// bind invoker to plugin command
		getCommand("as").setExecutor(invoker);
	}

	private void updatePlugin() {
		if (!configuration.getAutomaticUpdaterState().equals(PluginUpdater.State.OFF)) {
			PluginUpdater updater = new MavenPluginUpdater("alias", "name.richardson.james.bukkit", getDescription(), configuration.getAutomaticUpdaterBranch(), configuration.getAutomaticUpdaterState());
			this.getServer().getScheduler().runTaskAsynchronously(this, updater);
			new name.richardson.james.bukkit.utilities.updater.PlayerNotifier(this, this.getServer().getPluginManager(), updater);
		}
	}

	private void registerListeners() {
		new PlayerListener(this, getServer().getPluginManager(), getPlayerNameRecordManager(), getInetAddressRecordManager());
	}

}
