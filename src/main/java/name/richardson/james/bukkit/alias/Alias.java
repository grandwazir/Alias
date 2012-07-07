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

import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import name.richardson.james.bukkit.alias.query.CheckCommand;
import name.richardson.james.bukkit.utilities.command.CommandManager;
import name.richardson.james.bukkit.utilities.persistence.SQLStorage;
import name.richardson.james.bukkit.utilities.plugin.SkeletonPlugin;

public class Alias extends SkeletonPlugin {

  private static final List<Class<?>> databaseClasses = Alias.setDatabaseClasses();

  private static List<Class<?>> setDatabaseClasses() {
    final List<Class<?>> list = new LinkedList<Class<?>>();
    list.add(InetAddressRecord.class);
    list.add(PlayerNameRecord.class);
    return list;
  }

  private SQLStorage storage;

  public String getArtifactID() {
    return "alias";
  }

  @Override
  public List<Class<?>> getDatabaseClasses() {
    return Collections.unmodifiableList(Alias.databaseClasses);
  }

  public String getGroupID() {
    return "name.richardson.james.bukkit";
  }

  public AliasHandler getHandler(final Class<?> parentClass) {
    return new AliasHandler(parentClass, this);
  }

  public SQLStorage getSQLStorage() {
    return this.storage;
  }

  @Override
  public void onDisable() {
    this.getServer().getScheduler().cancelTasks(this);
    this.logger.info(String.format("%s is disabled!", this.getDescription().getName()));
  }

  @Override
  protected void registerCommands() {
    final CommandManager manager = new CommandManager(this);
    this.getCommand("as").setExecutor(manager);
    manager.addCommand(new CheckCommand(this));
  }

  @Override
  protected void registerEvents() {
    this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
  }

  @Override
  protected void setupPersistence() throws SQLException {
    try {
      this.storage = new SQLStorage(this, this.getDatabaseClasses());
    } catch (final Exception e) {
      e.printStackTrace();
      throw new SQLException();
    }
  }

}
