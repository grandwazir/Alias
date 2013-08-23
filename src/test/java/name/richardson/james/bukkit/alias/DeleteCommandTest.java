package name.richardson.james.bukkit.alias;

import org.bukkit.command.CommandSender;

import org.junit.Before;
import org.junit.Test;

import name.richardson.james.bukkit.utilities.command.context.CommandContext;
import name.richardson.james.bukkit.utilities.permissions.PermissionManager;

import name.richardson.james.bukkit.alias.persistence.PlayerNameRecord;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecordManager;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class DeleteCommandTest {

	private DeleteCommand command;
	private CommandContext commandContext;
	private CommandSender commandSender;
	private PlayerNameRecordManager playerNameRecordManager;

	@Test
	public void testExecuteNoPlayerNames()
	throws Exception {
		permitAccess();
		command.execute(commandContext);
		verify(commandSender).sendMessage("§cYou must specify two player names.");
	}

	@Test
	public void testExecuteNoPlayerRecords() {
		permitAccess();
		when(commandContext.has(anyInt())).thenReturn(true);
		when(commandContext.getString(anyInt())).thenReturn("frank");
		command.execute(commandContext);
		verify(playerNameRecordManager, times(2)).find("frank");
		verify(commandSender).sendMessage("§aThose players are not known to Alias.");
	}

	@Test
	public void testNoPermission() {
		command.execute(commandContext);
		verify(commandSender).sendMessage("§cYou do not have permission to delete aliases.");
	}

	@Test
	public void testExecute() {
		permitAccess();
		when(commandContext.has(anyInt())).thenReturn(true);
		when(commandContext.getString(anyInt())).thenReturn("frank");
		PlayerNameRecord playerNameRecord = mock(PlayerNameRecord.class);
		when(playerNameRecordManager.find("frank")).thenReturn(playerNameRecord);
		command.execute(commandContext);
		verify(playerNameRecordManager, times(2)).find("frank");
		verify(playerNameRecord).removeAssociation(playerNameRecord);
		verify(commandSender).sendMessage("§a§bfrank§a is no longer associated with §bfrank§a.");
	}

	private void permitAccess() {
		when(commandSender.hasPermission(anyString())).thenReturn(true);
	}

	@Before
	public void setUp()
	throws Exception {
		PermissionManager permissionManager = mock(PermissionManager.class);
		playerNameRecordManager = mock(PlayerNameRecordManager.class);
		command = new DeleteCommand(playerNameRecordManager);
		commandSender = mock(CommandSender.class);
		commandContext = mock(CommandContext.class);
		when(commandContext.getCommandSender()).thenReturn(commandSender);
	}


}

