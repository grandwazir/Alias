package name.richardson.james.bukkit.alias;

import java.net.InetAddress;
import java.util.Set;
import java.util.HashSet;

import org.bukkit.entity.Player;

import name.richardson.james.bukkit.util.Handler;

public class AliasHandler extends Handler implements AliasAPI {

  private final Alias plugin;

  public AliasHandler(Class<?> parentClass, Alias plugin) {
    super(parentClass);
    this.plugin = plugin;
  }

  public Set<String> getPlayersNames(InetAddress ip) {
    Set<String> set = new HashSet<String>();
    InetAddressRecord records = InetAddressRecord.findByAddress(plugin.getDatabaseHandler(), ip.getHostAddress());
    for (PlayerNameRecord record : records.getPlayerNames()) {
      set.add(record.getPlayerName());
    }
    return set;
  }

  public Set<String> getIPAddresses(String playerName) {
    Set<String> set = new HashSet<String>();
    PlayerNameRecord records = PlayerNameRecord.findByName(plugin.getDatabaseHandler(), playerName);
    for (InetAddressRecord record : records.getAddresses()) {
      set.add(record.getAddress());
    }
    return set;
  }
  
  public Set<String> getIPAddresses(Player player) {
    return this.getIPAddresses(player.getName());
  }


}
