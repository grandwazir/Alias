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
import java.util.LinkedList;
import java.util.List;

import com.avaje.ebean.EbeanServer;

import name.richardson.james.bukkit.alias.persistence.InetAddressRecord;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecord;
import name.richardson.james.bukkit.utilities.command.CommandManager;
import name.richardson.james.bukkit.utilities.configuration.DatabaseConfiguration;
import name.richardson.james.bukkit.utilities.persistence.SQLStorage;
import name.richardson.james.bukkit.utilities.plugin.AbstractPlugin;

public class Alias extends AbstractPlugin {

  /** The backend SQLStorage. */
  private SQLStorage storage;
  
  private AliasHandler handler;

  /**
   * Gets the artifact id.
   *
   * @return the artifact id
   */
  public String getArtifactID() {
    return "alias";
  }
  
  public EbeanServer getDatabase() {
    return storage.getEbeanServer();
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
  public String getGroupID() {
    return "name.richardson.james.bukkit";
  }

  /**
   * Gets the handler.
   *
   * @param parentClass the parent class
   * @return the handler
   */
  public AliasHandler getHandler() {
    if (this.handler == null) {
      this.handler = new AliasHandler(this);
    }
    return handler;
  }


  /**
   * Register commands.
   */
  @Override
  protected void registerCommands() {
    final CommandManager manager = new CommandManager(this);
    this.getCommand("as").setExecutor(manager);
    // manager.addCommand(new CheckCommand(this));
    // manager.addCommand(new DeleteCommand(this));
  }

  /**
   * Register events.
   */
  @Override
  protected void registerListeners() {
    this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
  }

  /**
   * Setup persistence.
   *
   * @throws SQLException the sQL exception
   */
  @Override
  protected void establishPersistence() throws SQLException {
    try {
      this.storage = new SQLStorage(this, new DatabaseConfiguration(this), this.getDatabaseClasses());
      this.storage.initalise();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
