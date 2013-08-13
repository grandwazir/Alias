package name.richardson.james.bukkit.alias.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class PlayerNameRecordTest {

	private static final int NUMBER_OF_ALIASES = 5;

	private PlayerNameRecord record;

	private static List<InetAddressRecord> getMockInetAddressRecords() {
		List<InetAddressRecord> inetAddressRecords = new ArrayList<InetAddressRecord>();
		do {
			PlayerNameRecord playerNameRecord = mock(PlayerNameRecord.class, RETURNS_SMART_NULLS);
			when(playerNameRecord.getPlayerName()).thenReturn(RandomStringUtils.random(8));
			InetAddressRecord inetAddressRecord = mock(InetAddressRecord.class);
			when(inetAddressRecord.getAddress()).thenReturn(RandomStringUtils.randomNumeric(8));
			when(inetAddressRecord.getPlayerNames()).thenReturn(Arrays.asList(playerNameRecord));
			inetAddressRecords.add(inetAddressRecord);
		} while (inetAddressRecords.size() != NUMBER_OF_ALIASES);
		return inetAddressRecords;
	}

	private PlayerNameRecord getMockPlayerNameRecord() {
		PlayerNameRecord playerNameRecord = mock(PlayerNameRecord.class, RETURNS_SMART_NULLS);
		when(playerNameRecord.getPlayerName()).thenReturn(RandomStringUtils.random(8));
		InetAddressRecord inetAddressRecord = mock(InetAddressRecord.class);
		when(inetAddressRecord.getAddress()).thenReturn(RandomStringUtils.randomNumeric(8));
		when(playerNameRecord.getAddresses()).thenReturn(Arrays.asList(inetAddressRecord));
		when(inetAddressRecord.getPlayerNames()).thenReturn(Arrays.asList(record));
		return playerNameRecord;
	}

	@Before
	public void setUp()
	throws Exception {
		record = new PlayerNameRecord();
	}

	@Test
	public void testCreateAssociation()
	throws Exception {
		PlayerNameRecord playerNameRecord = getMockPlayerNameRecord();
		record.createAssociation(playerNameRecord);
		Assert.assertTrue("Association has not been created!", record.getAddresses().contains(playerNameRecord.getAddresses().get(0)));
	}

	@Test
	public void testGetAliases()
	throws Exception {
		record.setAddresses(getMockInetAddressRecords());
		Assert.assertEquals("Number of aliases is inconsistent!", NUMBER_OF_ALIASES, record.getAliases().size());
	}

	@Test
	public void testRemoveAssociation()
	throws Exception {
		PlayerNameRecord playerNameRecord = getMockPlayerNameRecord();
		PlayerNameRecord anotherPlayerNameRecord = getMockPlayerNameRecord();
		record.createAssociation(playerNameRecord);
		record.createAssociation(anotherPlayerNameRecord);
		record.removeAssociation(playerNameRecord);
		Assert.assertFalse("Association has not been removed!", record.getAddresses().contains(playerNameRecord.getAddresses().get(0)));
		Assert.assertTrue("Wrong association has been removed!", record.getAddresses().contains(anotherPlayerNameRecord.getAddresses().get(0)));

	}

}
