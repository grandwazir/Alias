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
@Table(name="alias_addresses")
public class InetAddressRecord {

  @ManyToMany(cascade=CascadeType.ALL)
  private List<PlayerNameRecord> names;
  
  @Id
  @NotNull
  private InetAddress address;
  
  @NotNull
  private long lastSeen;
  
  public static InetAddressRecord findByAddress(InetAddress address2, boolean b) {
    // TODO Auto-generated method stub
    return null;
  }
  
  public void updateLastSeen() {
    this.lastSeen = System.currentTimeMillis();
  }
  
}
