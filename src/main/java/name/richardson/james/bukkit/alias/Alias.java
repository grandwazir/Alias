package name.richardson.james.bukkit.alias;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.PersistenceException;

import name.richardson.james.bukkit.alias.query.CheckCommand;
import name.richardson.james.bukkit.util.Logger;
import name.richardson.james.bukkit.util.Plugin;
import name.richardson.james.bukkit.util.command.CommandManager;
import name.richardson.james.bukkit.util.command.PlayerCommand;

public class Alias extends Plugin {

  private PlayerListener listener;
  private name.richardson.james.bukkit.alias.DatabaseHandler database;
  private AliasConfiguration configuration;
  private CommandManager cm;

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
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      if (!this.getServer().getPluginManager().isPluginEnabled(this)) return;
    }
    this.logger.info(String.format("%s is enabled.", this.getDescription().getFullName()));
    
  }

  private void registerListeners() {
    this.listener = new PlayerListener(this);
    this.getServer().getPluginManager().registerEvents(this.listener, this);
  }

  private void loadConfiguration() throws IOException {
    configuration = new AliasConfiguration(this);
    if (configuration.isDebugging()) {
      Logger.enableDebugging(this.getDescription().getName().toLowerCase());
    }
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
  
  public AliasHandler getHandler(Class<?> parentClass) {
    return new AliasHandler(parentClass, this);
  }
  
  private void registerCommands() {
    this.cm = new CommandManager(this.getDescription());
    this.getCommand("as").setExecutor(this.cm);
    cm.registerCommand("check", new CheckCommand(this));
  }
  
  @Override
  public List<Class<?>> getDatabaseClasses() {
    return DatabaseHandler.getDatabaseClasses();
  }

  public DatabaseHandler getDatabaseHandler() {
    return this.database;
  }
  
}
