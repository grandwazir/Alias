package name.richardson.james.bukkit.alias;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
  
  public List<InetAddressRecord> addresses;

  public static PlayerNameRecord findByName(Database database, String playerName) {
    logger.debug(String.format("Attempting to return PlayerNameRecord matching the name %s.", playerName));
    PlayerNameRecord record = database.getEbeanServer().find(PlayerNameRecord.class).where().ieq("playerName", playerName).findUnique();
    return record;
  }
  
  public static boolean isPlayerKnown(Database database, String playerName) {
    PlayerNameRecord record = database.getEbeanServer().find(PlayerNameRecord.class).where().ieq("playerName", playerName).findUnique();
    if (record != null) {
      return true;
    } else {
      return false;
    }
  }
  
  
  public String getPlayerName() {
    return this.playerName;
  }
  
  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }
  
  public void updateLastSeen() {
    this.lastSeen = System.currentTimeMillis();
  }

  @ManyToMany(cascade=CascadeType.ALL)
  @JoinTable (name="alias_players_addresses")
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
