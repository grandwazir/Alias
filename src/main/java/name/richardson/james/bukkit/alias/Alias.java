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
import name.richardson.james.bukkit.utilities.plugin.SkeletonPlugin;

public class Alias extends SkeletonPlugin {

  private DatabaseHandler database;

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

  public void onDisable() {
    this.getServer().getScheduler().cancelTasks(this);
    this.logger.info(String.format("%s is disabled!", this.getDescription().getName()));
  }

  protected void registerCommands() {
    this.cm = new CommandManager(this.getDescription());
    this.getCommand("as").setExecutor(this.cm);
    this.cm.registerCommand("check", new CheckCommand(this));
  }

  protected void registerEvents() {
    this.listener = new PlayerListener(this);
    this.getServer().getPluginManager().registerEvents(this.listener, this);
  }

  protected void setupPersistence() throws SQLException {
    try {
      this.getDatabase().find(PlayerNameRecord.class).findRowCount();
    } catch (final PersistenceException ex) {
      this.logger.warning("No database schema found. Generating a new one.");
      this.installDDL();
    } 
    this.database = new DatabaseHandler(this.getDatabase());
  }

  public String getGroupID() {
   return "name.richardson.james.bukkit";
  }

  public String getArtifactID() {
    return "alias";
  }

}
