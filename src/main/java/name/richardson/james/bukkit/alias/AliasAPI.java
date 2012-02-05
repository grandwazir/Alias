package name.richardson.james.bukkit.alias;

import java.net.InetAddress;
import java.util.List;
import java.util.Set;

public interface AliasAPI {
  
  public Set<String> getPlayersNames(InetAddress ip);
  
  public Set<String> getIPAddresses(String playerName);
  
  
}
