package name.richardson.james.bukkit.alias;

import java.net.InetAddress;

import name.richardson.james.bukkit.util.Database;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

  private Database database;
  
  public PlayerListener(Alias alias) {
    this.database = alias.getDatabaseHandler();
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerLogin(PlayerJoinEvent event) {
    final String playerName = event.getPlayer().getName();
    final InetAddress address = event.getPlayer().getAddress().getAddress();
    final PlayerNameRecord playerNameRecord = PlayerNameRecord.findByName(database, playerName, true);
    final InetAddressRecord inetAddressRecord = InetAddressRecord.findByAddress(database, address, true);
    playerNameRecord.updateLastSeen();
    inetAddressRecord.updateLastSeen();
    playerNameRecord.getAddresses().add(inetAddressRecord);
    database.save(playerNameRecord);
  }
  
}
