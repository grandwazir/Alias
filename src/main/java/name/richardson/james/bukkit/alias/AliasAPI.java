package name.richardson.james.bukkit.alias;

import java.net.InetAddress;
import java.util.Set;

public interface AliasAPI {

  public Set<String> lookupIPAddress(InetAddress ip);
  
  public Set<InetAddress> lookupPlayerName(String playerName);
  
}
