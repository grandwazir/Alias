package name.richardson.james.bukkit.alias;

import java.net.InetAddress;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotNull;

@Entity
@Table(name="alias_players")
public class PlayerNameRecord {

  @ManyToMany(cascade=CascadeType.ALL)
  private List<InetAddressRecord> addresses;
  
  @Id
  @NotNull
  private String playerName;
  
  @NotNull
  private long lastSeen;

  public static PlayerNameRecord findByName(String playerName, boolean create) {
    return new PlayerNameRecord();
  }
  
  public List<InetAddressRecord> getInetAddresses() {
    return addresses;
  }

  public void updateLastSeen() {
    this.lastSeen = System.currentTimeMillis();
  }
  
}
