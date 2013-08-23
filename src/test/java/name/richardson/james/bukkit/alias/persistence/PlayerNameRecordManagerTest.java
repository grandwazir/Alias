package name.richardson.james.bukkit.alias.persistence;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

public class PlayerNameRecordManagerTest {

	private EbeanServer database;
	private PlayerNameRecordManager manager;
	private Query<PlayerNameRecord> query;

	private static PlayerNameRecord getMockPlayerNameRecord() {
		PlayerNameRecord record = mock(PlayerNameRecord.class);
		return record;
	}

	@Before
	public void setUp()
	throws Exception {
		database = mock(EbeanServer.class);
		query = mock(Query.class);
		when(database.createQuery(PlayerNameRecord.class)).thenReturn(query);
		manager = new PlayerNameRecordManager(database);
	}

	@Test
	public void testCreate()
	throws Exception {
		manager.create("frank");
		verify(query, times(2)).findUnique();
		verify(database, times(1)).save(anyObject());
	}

	@Test
	public void testExists()
	throws Exception {
		PlayerNameRecord record = getMockPlayerNameRecord();
		when(query.findUnique()).thenReturn(record);
		Assert.assertTrue("Record should exist!", manager.exists("frank"));
	}

	@Test
	public void testFind()
	throws Exception {
		PlayerNameRecord record = getMockPlayerNameRecord();
		when(query.findUnique()).thenReturn(record);
		Assert.assertEquals("Record should be the same!", record, manager.find("frank"));
		verify(query).setParameter("playerName", "frank");
		verify(query, times(1)).findUnique();
	}

	@Test
	public void testList()
	throws Exception {
		query = mock(Query.class, RETURNS_DEEP_STUBS);
		when(database.createQuery(PlayerNameRecord.class)).thenReturn(query);
		Assert.assertNotNull("Returned list is inconsistent!", manager.list());
		verify(query, times(1)).findList();
	}

	@Test
	public void testToString()
	throws Exception {
		Assert.assertTrue("toString has not been overridden!", manager.toString().contains("PlayerNameRecordManager{"));
	}


}
