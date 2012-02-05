package name.richardson.james.bukkit.alias;

import java.util.LinkedList;
import java.util.List;

import name.richardson.james.bukkit.util.Handler;

public class AliasHandler extends Handler implements AliasAPI {

  private final Alias plugin;

  public AliasHandler(Class<?> parentClass, Alias plugin) {
    super(parentClass);
    this.plugin = plugin;
  }

  public List<PlayerNameRecord> lookupIPAddress(String address) {
    InetAddressRecord record = InetAddressRecord.findByAddress(this.plugin.getDatabaseHandler(), address);
    if (record != null) return record.getPlayerNames();
    logger.debug("No PlayerNameRecords found, returning empty list");
    return new LinkedList<PlayerNameRecord>();
  }

  public List<InetAddressRecord> lookupPlayerName(String playerName) {
    PlayerNameRecord record = PlayerNameRecord.findByName(this.plugin.getDatabaseHandler(), playerName);
    if (record != null) return record.getAddresses();
    logger.debug("No InetAddressRecords found, returning empty list");
    return new LinkedList<InetAddressRecord>();
  }

}
