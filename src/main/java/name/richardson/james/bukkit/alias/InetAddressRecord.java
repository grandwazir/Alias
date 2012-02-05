package name.richardson.james.bukkit.alias;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import name.richardson.james.bukkit.util.Database;
import name.richardson.james.bukkit.util.Logger;

import name.richardson.james.bukkit.alias.PlayerNameRecord;

import com.avaje.ebean.validation.NotNull;

@Entity
@Table(name="alias_addresses")
public class InetAddressRecord {

  private static Logger logger = new Logger(InetAddressRecord.class);

  @ManyToMany(mappedBy="addresses")
  private List<PlayerNameRecord> playerNames;
  
  @Id
  private int id;
  
  @NotNull
  private String address;
  
  @NotNull
  private long lastSeen;
  
  public static InetAddressRecord findByAddress(Database database, String address) {
    logger.debug(String.format("Attempting to return InetAddressRecord matching the address %s.", address));
    InetAddressRecord record = database.getEbeanServer().find(InetAddressRecord.class).where().eq("address", address).findUnique();
    return record;
  }
  
  public void updateLastSeen() {
    this.lastSeen = System.currentTimeMillis();
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public long getLastSeen() {
    return lastSeen;
  }

  public void setLastSeen(long lastSeen) {
    this.lastSeen = lastSeen;
  }

  @ManyToMany(targetEntity=PlayerNameRecord.class) 
  public List<PlayerNameRecord> getPlayerNames() {
    return playerNames;
  }

  public void setPlayerNames(List<PlayerNameRecord> playerNames) {
    this.playerNames = playerNames;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public static boolean isAddressKnown(Database database, String address) {
    InetAddressRecord record = database.getEbeanServer().find(InetAddressRecord.class).where().ieq("address", address).findUnique();
    if (record != null) {
      return true;
    } else {
      return false;
    }
  }

}
