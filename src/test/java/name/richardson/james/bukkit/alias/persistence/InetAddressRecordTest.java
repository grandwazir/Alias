package name.richardson.james.bukkit.alias.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.RETURNS_SMART_NULLS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InetAddressRecordTest {

	private static final int NUMBER_OF_ALIASES = 5;

	private InetAddressRecord record;

	private static List<PlayerNameRecord> getMockPlayerNameRecords() {
		List<PlayerNameRecord> playerNameRecordList = new ArrayList<PlayerNameRecord>();
		do {
			PlayerNameRecord playerNameRecord = mock(PlayerNameRecord.class, RETURNS_SMART_NULLS);
			when(playerNameRecord.getPlayerName()).thenReturn(RandomStringUtils.random(8));
			InetAddressRecord inetAddressRecord = mock(InetAddressRecord.class);
			when(inetAddressRecord.getAddress()).thenReturn(RandomStringUtils.randomNumeric(8));
			when(playerNameRecord.getAddresses()).thenReturn(Arrays.asList(inetAddressRecord));
			playerNameRecordList.add(playerNameRecord);
		} while (playerNameRecordList.size() != NUMBER_OF_ALIASES);
		return playerNameRecordList;
	}


	@Test
	public void testGetAliases()
	throws Exception {
		record.setPlayerNames(getMockPlayerNameRecords());
		Assert.assertEquals("Number of aliases is inconsistent!", NUMBER_OF_ALIASES, record.getAliases().size());
	}

	@Before
	public void setUp()
	throws Exception {
		record = new InetAddressRecord();
	}
}
