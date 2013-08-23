package name.richardson.james.bukkit.alias;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import org.junit.Before;
import org.junit.Test;

import name.richardson.james.bukkit.utilities.command.context.CommandContext;
import name.richardson.james.bukkit.utilities.permissions.PermissionManager;

import name.richardson.james.bukkit.alias.persistence.PlayerNameRecord;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecordManager;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CheckCommandTest {

	private CheckCommand command;
	private CommandContext commandContext;
	private CommandSender commandSender;
	private PlayerNameRecordManager playerNameRecordManager;

	@Test
	public void testExecuteNoPlayerRecord()
	throws Exception {
		command.execute(commandContext);
		verify(playerNameRecordManager).find("frank");
		verify(commandSender).sendMessage("§a§bfrank§a has no known aliases.");
	}

	@Test
	public void testNoPermission() {
		when(commandSender.hasPermission(anyString())).thenReturn(false);
		command.execute(commandContext);
		verify(commandSender).sendMessage("§cYou are not allowed to do that.");
	}

	@Test
	public void testExecute() {
		PlayerNameRecord playerNameRecord = mock(PlayerNameRecord.class);
		when(playerNameRecord.getPlayerNameRecords()).thenReturn(Arrays.asList(playerNameRecord));
		when(playerNameRecordManager.find("joe")).thenReturn(playerNameRecord);
		when(commandContext.has(anyInt())).thenReturn(true);
		when(commandContext.getString(anyInt())).thenReturn("joe");
		command.execute(commandContext);
		verify(playerNameRecordManager).find("joe");
		verify(commandSender).sendMessage("§d§bjoe§d has §bone known alias§d.");
		verify(commandSender).sendMessage(any(String[].class));
	}

	@Before
	public void setUp()
	throws Exception {
		playerNameRecordManager = mock(PlayerNameRecordManager.class);
		command = new CheckCommand(playerNameRecordManager);
		commandSender = mock(CommandSender.class);
		commandContext = mock(CommandContext.class);
		when(commandContext.getCommandSender()).thenReturn(commandSender);
		when(commandSender.getName()).thenReturn("frank");
		when(commandSender.hasPermission(anyString())).thenReturn(true);
	}

}
