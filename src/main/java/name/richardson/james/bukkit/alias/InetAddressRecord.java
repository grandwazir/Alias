package name.richardson.james.bukkit.alias;

import java.net.InetAddress;
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

import name.richardson.james.bukkit.alias.PlayerNameRecord;

import com.avaje.ebean.validation.NotNull;

@Entity
@Table(name="alias_addresses")
public class InetAddressRecord {

  private static Logger logger = new Logger(InetAddressRecord.class);

  @ManyToMany(cascade=CascadeType.ALL)
  private List<PlayerNameRecord> playerNames;
  
  @Id
  private int id;
  
  @NotNull
  private String address;
  
  @NotNull
  private long lastSeen;
  
  public static InetAddressRecord findByAddress(Database database, String address, boolean create) {
    logger.debug(String.format("Attempting to return InetAddressRecord matching the address %s.", address));
    InetAddressRecord record = database.getEbeanServer().find(InetAddressRecord.class).where().ieq("address", address).findUnique();
    if (record == null && create == true) {
      logger.debug(String.format("Creating new InetAddressRecord for address %s.", address.toString()));
      record = new InetAddressRecord();
      record.setAddress(address.toString());
      record.updateLastSeen();
      database.save(record);
    }
    return record;
  }
  
  public void updateLastSeen() {
    this.setLastSeen(System.currentTimeMillis());
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
  @JoinTable (name="alias_players_addresses", 
      joinColumns= @JoinColumn(name="player_id", referencedColumnName="id"),
      inverseJoinColumns=@JoinColumn(name="address_id", referencedColumnName="id")
  )
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
  
}
