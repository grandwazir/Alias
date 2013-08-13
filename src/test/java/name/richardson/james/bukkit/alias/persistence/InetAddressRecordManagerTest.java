package name.richardson.james.bukkit.alias.persistence;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

public class InetAddressRecordManagerTest {

	private EbeanServer database;
	private InetAddressRecordManager manager;
	private Query query;

	private static InetAddressRecord getMockInetAddressRecord() {
		InetAddressRecord record = mock(InetAddressRecord.class);
		return record;
	}

	@Before
	public void setUp()
	throws Exception {
		database = mock(EbeanServer.class);
		query = mock(Query.class);
		when(database.createQuery(InetAddressRecord.class)).thenReturn(query);
		manager = new InetAddressRecordManager(database);
	}

	@Test
	public void testCreate()
	throws Exception {
		manager.create("127.0.0.1");
		verify(query, times(2)).findUnique();
		verify(database, times(1)).save(anyObject());
	}

	@Test
	public void testExists()
	throws Exception {
		InetAddressRecord record = getMockInetAddressRecord();
		when(query.findUnique()).thenReturn(record);
		Assert.assertTrue("Record should exist!", manager.exists("127.0.0.1"));
	}

	@Test
	public void testFind()
	throws Exception {
		InetAddressRecord record = getMockInetAddressRecord();
		when(query.findUnique()).thenReturn(record);
		Assert.assertEquals("Record should be the same!", record, manager.find("127.0.0.1"));
		verify(query).setParameter("address", "127.0.0.1");
		verify(query, times(1)).findUnique();
	}

	@Test
	public void testList()
	throws Exception {
		query = mock(Query.class, RETURNS_DEEP_STUBS);
		when(database.createQuery(InetAddressRecord.class)).thenReturn(query);
		Assert.assertNotNull("Returned list is inconsistent!", manager.list());
		verify(query, times(1)).findList();
	}

	@Test
	public void testToString()
	throws Exception {
		Assert.assertTrue("toString has not been overridden!", manager.toString().contains("InetAddressRecordManager{"));
	}


}
