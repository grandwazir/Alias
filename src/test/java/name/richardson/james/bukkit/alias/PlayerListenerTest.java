package name.richardson.james.bukkit.alias;

import java.net.InetAddress;

import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import name.richardson.james.bukkit.alias.persistence.InetAddressRecord;
import name.richardson.james.bukkit.alias.persistence.InetAddressRecordManager;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecord;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecordManager;

import static org.mockito.Mockito.*;

public class PlayerListenerTest {

	private InetAddressRecordManager inetAddressRecordManager;
	private PlayerListener listener;
	private PlayerNameRecordManager playerNameRecordManager;

	@Test
	public void testOnPlayerLogin()
	throws Exception {
		AsyncPlayerPreLoginEvent event = new AsyncPlayerPreLoginEvent("grandwazir", InetAddress.getByName("127.0.0.1"));
		listener.onPlayerLogin(event);
		verify(playerNameRecordManager, times(1)).create("grandwazir");
		verify(inetAddressRecordManager, times(1)).create("127.0.0.1");
		verify(playerNameRecordManager, times(1)).save(Matchers.<PlayerNameRecord>any());
		verify(inetAddressRecordManager, times(1)).save(Matchers.<InetAddressRecord>any());
	}

	@Before
	public void setUp()
	throws Exception {
		Plugin plugin = mock(Plugin.class);
		PluginManager pluginManager = mock(PluginManager.class);
		playerNameRecordManager = mock(PlayerNameRecordManager.class, RETURNS_MOCKS);
		inetAddressRecordManager = mock(InetAddressRecordManager.class, RETURNS_MOCKS);
		listener = new PlayerListener(plugin, pluginManager, playerNameRecordManager, inetAddressRecordManager);
	}

}
