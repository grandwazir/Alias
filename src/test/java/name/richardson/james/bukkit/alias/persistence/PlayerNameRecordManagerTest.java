package name.richardson.james.bukkit.alias.persistence;

import java.util.Collections;

import com.avaje.ebean.EbeanServer;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

public class PlayerNameRecordManagerTest {

	private EbeanServer database;
	private PlayerNameRecordManager manager;

	@Test
	public void testToString()
	throws Exception {
		Assert.assertTrue("toString has not been overridden!", manager.toString().contains("PlayerNameRecordManager{"));
	}

	@Test
	public void testList()
	throws Exception {
		Assert.assertEquals("Returned list is inconsistent!", Collections.EMPTY_LIST, manager.list());
	}

	@Test
	public void testExists()
	throws Exception {
		PlayerNameRecord record = getMockPlayerNameRecord();
		when(database.find(PlayerNameRecord.class).where().ieq("name", "frank").findUnique()).thenReturn(record);
		Assert.assertTrue("Record should exist!", manager.exists("127.0.0.1"));
		verify(database).find(PlayerNameRecord.class).where().ieq("name", "frank").findUnique();
	}

	@Test
	public void testFind()
	throws Exception {
		PlayerNameRecord record = getMockPlayerNameRecord();
		when(database.find(PlayerNameRecord.class).where().ieq("name", "frank").findUnique()).thenReturn(record);
		Assert.assertEquals("Record should be the same!", manager.find("frank"));
		verify(database).find(PlayerNameRecord.class).where().ieq("name", "frank").findUnique();
	}

	@Test
	public void testCreate()
	throws Exception {
		Assert.assertNotNull("A record should be returned!", manager.create("frank"));
		verify(database).find(PlayerNameRecord.class).where().ieq("name", "frank").findUnique();
		verify(database).save(anyObject());
	}

	private static PlayerNameRecord getMockPlayerNameRecord() {
		PlayerNameRecord record = mock(PlayerNameRecord.class);
		return record;
	}

	@Before
	public void setUp()
	throws Exception {
		database = mock(EbeanServer.class);
		manager = new PlayerNameRecordManager(database);
	}


}
