package name.richardson.james.bukkit.alias;

import java.net.InetAddress;

import name.richardson.james.bukkit.util.Database;
import name.richardson.james.bukkit.util.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

  private final Logger logger = new Logger(PlayerListener.class);
  private Database database;
  
  public PlayerListener(Alias alias) {
    this.database = alias.getDatabaseHandler();
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerLogin(PlayerJoinEvent event) {
    String playerName = event.getPlayer().getName();
    String address = event.getPlayer().getAddress().getAddress().getHostAddress();
    PlayerNameRecord playerNameRecord = this.getPlayerNameRecord(playerName);
    InetAddressRecord inetAddressRecord = this.getInetAddressRecord(address.toString()); 
  
    // update time stamps
    long now = System.currentTimeMillis();
    playerNameRecord.setLastSeen(now);
    inetAddressRecord.setLastSeen(now);
    logger.debug(playerNameRecord.getAddresses().toString());
    
    // link IP address to name
    if (!playerNameRecord.getAddresses().contains(inetAddressRecord)) {
      playerNameRecord.getAddresses().add(inetAddressRecord);
      logger.debug(playerNameRecord.getAddresses().toString());
    }
    
    database.save(playerNameRecord);
    database.save(inetAddressRecord);
    
  }

  private InetAddressRecord getInetAddressRecord(String address) {
    if (!InetAddressRecord.isAddressKnown(database, address)) {
      InetAddressRecord record = new InetAddressRecord();
      record.setAddress(address);
      record.updateLastSeen();
      database.save(record);
    }
    return InetAddressRecord.findByAddress(database, address);
  }

  private PlayerNameRecord getPlayerNameRecord(String playerName) {
    if (!PlayerNameRecord.isPlayerKnown(database, playerName)) {
      PlayerNameRecord record = new PlayerNameRecord();
      record.setPlayerName(playerName);
      record.updateLastSeen();
      database.save(record);
    }
    return PlayerNameRecord.findByName(database, playerName);
  }
  
}
