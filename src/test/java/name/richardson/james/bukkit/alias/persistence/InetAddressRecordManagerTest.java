package name.richardson.james.bukkit.alias.persistence;

import java.util.Collections;

import com.avaje.ebean.EbeanServer;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InetAddressRecordManagerTest {

	private EbeanServer database;
	private InetAddressRecordManager manager;

	@Test
	public void testToString()
	throws Exception {
		Assert.assertTrue("toString has not been overridden!", manager.toString().contains("InetAddressRecordManager{"));
	}

	@Test
	public void testList()
	throws Exception {
		Assert.assertEquals("Returned list is inconsistent!", Collections.EMPTY_LIST, manager.list());
	}

	@Test
	public void testExists()
	throws Exception {
		InetAddressRecord record = getMockInetAddressRecord();
		when(database.find(InetAddressRecord.class).where().ieq("address", "127.0.0.1").findUnique()).thenReturn(record);
		Assert.assertTrue("Record should exist!", manager.exists("127.0.0.1"));
		verify(database).find(InetAddressRecord.class).where().ieq("address", "127.0.0.1").findUnique();
	}

	@Test
	public void testFind()
	throws Exception {
		InetAddressRecord record = getMockInetAddressRecord();
		when(database.find(InetAddressRecord.class).where().ieq("address", "127.0.0.1").findUnique()).thenReturn(record);
		Assert.assertEquals("Record should be the same!", manager.find("127.0.0.1"));
		verify(database).find(InetAddressRecord.class).where().ieq("address", "127.0.0.1").findUnique();
	}

	@Test
	public void testCreate()
	throws Exception {
		Assert.assertNotNull("A record should be returned!", manager.create("127.0.0.1"));
		verify(database).find(InetAddressRecord.class).where().ieq("address", "127.0.0.1").findUnique();
		verify(database).save(anyObject());
	}

	private static InetAddressRecord getMockInetAddressRecord() {
		InetAddressRecord record = mock(InetAddressRecord.class);
		return record;
	}

	@Before
	public void setUp()
	throws Exception {
		database = mock(EbeanServer.class);
		manager = new InetAddressRecordManager(database);
	}


}
