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

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.PersistenceException;

import name.richardson.james.bukkit.alias.query.CheckCommand;
import name.richardson.james.bukkit.util.Logger;
import name.richardson.james.bukkit.util.Plugin;
import name.richardson.james.bukkit.util.command.CommandManager;

public class Alias extends Plugin {

  private PlayerListener listener;
  private name.richardson.james.bukkit.alias.DatabaseHandler database;
  private AliasConfiguration configuration;
  private CommandManager cm;

  @Override
  public List<Class<?>> getDatabaseClasses() {
    return DatabaseHandler.getDatabaseClasses();
  }

  public DatabaseHandler getDatabaseHandler() {
    return this.database;
  }

  public AliasHandler getHandler(final Class<?> parentClass) {
    return new AliasHandler(parentClass, this);
  }

  private void loadConfiguration() throws IOException {
    this.configuration = new AliasConfiguration(this);
    if (this.configuration.isDebugging()) {
      Logger.enableDebugging(this.getDescription().getName().toLowerCase());
    }
  }

  public void onDisable() {
    this.getServer().getScheduler().cancelTasks(this);
    this.logger.info(String.format("%s is disabled!", this.getDescription().getName()));
  }

  public void onEnable() {

    try {
      this.logger.setPrefix("[Alias] ");
      this.loadConfiguration();
      this.setupDatabase();
      // load the worlds
      this.registerListeners();
      this.setPermission();
      this.registerCommands();
    } catch (final IOException exception) {
      this.logger.severe("Unable to load configuration!");
      exception.printStackTrace();
    } catch (final SQLException e) {
      e.printStackTrace();
    } finally {
      if (!this.getServer().getPluginManager().isPluginEnabled(this)) {
        return;
      }
    }
    this.logger.info(String.format("%s is enabled.", this.getDescription().getFullName()));

  }

  private void registerCommands() {
    this.cm = new CommandManager(this.getDescription());
    this.getCommand("as").setExecutor(this.cm);
    this.cm.registerCommand("check", new CheckCommand(this));
  }

  private void registerListeners() {
    this.listener = new PlayerListener(this);
    this.getServer().getPluginManager().registerEvents(this.listener, this);
  }

  private void setupDatabase() throws SQLException {
    try {
      this.getDatabase().find(PlayerNameRecord.class).findRowCount();
    } catch (final PersistenceException ex) {
      this.logger.warning("No database schema found. Generating a new one.");
      this.installDDL();
    }
    this.database = new DatabaseHandler(this.getDatabase());
  }

}
