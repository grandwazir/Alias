package name.richardson.james.bukkit.alias;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.SQLitePlatform;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import name.richardson.james.bukkit.alias.persistence.InetAddressRecord;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecord;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA. User: james Date: 13/08/13 Time: 17:09 To change this template use File | Settings | File Templates.
 */
public class AliasTest {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	private Alias plugin;
	private PluginCommand pluginCommand;
	private Server server;

	@Test
	public void testOnEnable()
	throws Exception {
		plugin.onEnable();
	}

	@Test
	public void testGetDatabaseClasses()
	throws Exception {
		Assert.assertTrue("Database classes should include PlayerNameRecord!", plugin.getDatabaseClasses().contains(PlayerNameRecord.class));
		Assert.assertTrue("Database classes should include InetAddressRecord!", plugin.getDatabaseClasses().contains(InetAddressRecord.class));
	}

	@Test
	public void testGetArtifactID()
	throws Exception {
		Assert.assertEquals("alias", plugin.getArtifactID());
	}

	@Before
	public void setUp()
	throws Exception {
		plugin = new Alias();
		PluginDescriptionFile pluginDescriptionFile = new PluginDescriptionFile("Test", "1.0", null);
		server = mock(Server.class);
		PluginManager pluginManager = mock(PluginManager.class);
		BukkitScheduler scheduler = mock(BukkitScheduler.class);
		when(server.getLogger()).thenReturn(Logger.getAnonymousLogger());
		when(server.getPluginManager()).thenReturn(pluginManager);
		when(server.getScheduler()).thenReturn(scheduler);
		// Stub out configure db with bukkit defaults
		// this is a horrible hack but required at the moment
		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation)
			throws Throwable {
				Object[] args = invocation.getArguments();
				ServerConfig serverConfig = (ServerConfig) args[0];
				serverConfig.getDataSourceConfig().setIsolationLevel(8);
				serverConfig.getDataSourceConfig().setUsername("bukkit");
				serverConfig.getDataSourceConfig().setDriver("org.sqlite.JDBC");
				serverConfig.getDataSourceConfig().setPassword("walrus");
				serverConfig.getDataSourceConfig().setUrl("jdbc:sqlite:{DIR}{NAME}.db");
				serverConfig.setDatabasePlatform(new SQLitePlatform());
				serverConfig.getDatabasePlatform().getDbDdlSyntax().setIdentity("");
				return null;
			}
		}).when(server).configureDbConfig(Matchers.<ServerConfig>anyObject());
		// Get an instance of PluginCommand
		Class pluginClass = PluginCommand.class;
		Constructor constructor = pluginClass.getDeclaredConstructor(String.class, Plugin.class);
		constructor.setAccessible(true);
		pluginCommand = (PluginCommand) constructor.newInstance("jchat", plugin);
		when(server.getPluginCommand(anyString())).thenReturn(pluginCommand);
		// Set server - required for permission setting
		Field field = Bukkit.class.getDeclaredField("server");
		field.setAccessible(true);
		field.set(null, server);
		// Initalize the plugin
		Class<?> clazz = plugin.getClass().getSuperclass().getSuperclass().getSuperclass();
		Method method = clazz.getDeclaredMethod("initialize", PluginLoader.class, Server.class, PluginDescriptionFile.class, File.class, File.class, ClassLoader.class);
		method.setAccessible(true);
		method.invoke(plugin, null, server, pluginDescriptionFile, temporaryFolder.newFolder("test"), null, getClass().getClassLoader());
	}

	@After
	public void tearDown() throws Exception {
		Field field = Bukkit.class.getDeclaredField("server");
		field.setAccessible(true);
		field.set(null, null);
	}

}
