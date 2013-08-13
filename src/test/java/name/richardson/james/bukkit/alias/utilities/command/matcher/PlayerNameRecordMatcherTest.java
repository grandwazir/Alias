package name.richardson.james.bukkit.alias.utilities.command.matcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import name.richardson.james.bukkit.alias.persistence.InetAddressRecord;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecord;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecordManager;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerNameRecordMatcherTest {

	private int NUMBER_OF_RECORDS = 75;
	private PlayerNameRecordMatcher matcher;

	private List<PlayerNameRecord> getMockPlayerNameRecords() {
		List<PlayerNameRecord> playerNameRecordList = new ArrayList<PlayerNameRecord>();
		do {
			PlayerNameRecord playerNameRecord = mock(PlayerNameRecord.class, RETURNS_SMART_NULLS);
			when(playerNameRecord.getPlayerName()).thenReturn(RandomStringUtils.random(8));
			InetAddressRecord inetAddressRecord = mock(InetAddressRecord.class);
			when(inetAddressRecord.getAddress()).thenReturn(RandomStringUtils.randomNumeric(8));
			when(playerNameRecord.getAddresses()).thenReturn(Arrays.asList(inetAddressRecord));
			playerNameRecordList.add(playerNameRecord);
		} while (playerNameRecordList.size() != NUMBER_OF_RECORDS);
		return playerNameRecordList;
	}

	@Test
	public void testMatchesArgumentTooSmall()
	throws Exception {
		Set<String> matches = matcher.matches("");
		Assert.assertTrue("No results should be returned!", matches.isEmpty());
		matches = matcher.matches("ab");
		Assert.assertTrue("No results should be returned!", matches.isEmpty());
	}

	@Test
	public void testMatches()
	throws Exception {
		Set<String> matches = matcher.matches("abc");
		Assert.assertTrue("A maximum of 50 matches should be returned!", matches.size() == 50);
	}


	@Before
	public void setUp()
	throws Exception {
		PlayerNameRecordManager playerNameRecordManager = mock(PlayerNameRecordManager.class);
		List<PlayerNameRecord> playerNameRecords = getMockPlayerNameRecords();
		when(playerNameRecordManager.list(anyString())).thenReturn(playerNameRecords);
		matcher = new PlayerNameRecordMatcher(playerNameRecordManager);
	}



}
