package name.richardson.james.bukkit.alias;

import java.util.List;

public interface AliasAPI {

  public List<PlayerNameRecord> lookupIPAddress(String address);
  
  public List<InetAddressRecord> lookupPlayerName(String playerName);
  
}
