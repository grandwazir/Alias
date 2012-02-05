package name.richardson.james.bukkit.alias;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import name.richardson.james.bukkit.util.Database;
import name.richardson.james.bukkit.util.Logger;

import com.avaje.ebean.validation.NotNull;

@Entity
@Table(name="alias_players")
public class PlayerNameRecord {

  private static Logger logger = new Logger(PlayerNameRecord.class);
  
  @Id
  private int id;
  
  @NotNull
  private String playerName;
  
  @NotNull
  private long lastSeen;
  
  @ManyToMany(cascade=CascadeType.ALL)
  private List<InetAddressRecord> addresses;

  public static PlayerNameRecord findByName(Database database, String playerName, boolean create) {
    logger.debug(String.format("Attempting to return PlayerNameRecord matching the name %s.", playerName));
    PlayerNameRecord record = database.getEbeanServer().find(PlayerNameRecord.class).where().ieq("playerName", playerName).findUnique();
    if (record == null && create == true) {
      logger.debug(String.format("Creating new PlayerNameRecord for name %s.", playerName));
      record = new PlayerNameRecord();
      record.setPlayerName(playerName);
      record.updateLastSeen();
      database.save(record);
    }
    return record;
  }
  
  
  public String getPlayerName() {
    return this.playerName;
  }
  
  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }
  
  public void updateLastSeen() {
    this.setLastSeen(System.currentTimeMillis());
  }

  public List<InetAddressRecord> getAddresses() {
    return addresses;
  }


  public void setAddresses(List<InetAddressRecord> addresses) {
    this.addresses = addresses;
  }


  public long getLastSeen() {
    return lastSeen;
  }


  public void setLastSeen(long lastSeen) {
    this.lastSeen = lastSeen;
  }


  public int getId() {
    return id;
  }


  public void setId(int id) {
    this.id = id;
  }
  
}
