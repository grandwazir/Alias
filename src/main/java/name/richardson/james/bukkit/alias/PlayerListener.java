/*******************************************************************************
 * Copyright (c) 2012 James Richardson.
 * 
 * PlayerListener.java is part of Alias.
 * 
 * Alias is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Alias is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Alias. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package name.richardson.james.bukkit.alias;

import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import org.apache.commons.lang.Validate;

import name.richardson.james.bukkit.utilities.listener.AbstractListener;
import name.richardson.james.bukkit.utilities.logging.PluginLoggerFactory;

import name.richardson.james.bukkit.alias.persistence.InetAddressRecord;
import name.richardson.james.bukkit.alias.persistence.InetAddressRecordManager;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecord;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecordManager;

public class PlayerListener extends AbstractListener {

	private final Logger logger = PluginLoggerFactory.getLogger(PlayerListener.class);
	private final InetAddressRecordManager inetAddressRecordManager;
	private final PlayerNameRecordManager playerNameRecordManager;

	public PlayerListener(Plugin plugin, PluginManager pluginManager, PlayerNameRecordManager playerNameRecordManager, InetAddressRecordManager inetAddressRecordManager) {
		super(plugin, pluginManager);
		Validate.notNull(playerNameRecordManager, "PlayerNameRecordManager can not be null!");
		Validate.notNull(inetAddressRecordManager, "InetAddressRecordManager can not be null!");
		this.playerNameRecordManager = playerNameRecordManager;
		this.inetAddressRecordManager = inetAddressRecordManager;
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerLogin(final AsyncPlayerPreLoginEvent event) {
		logger.log(Level.FINE, "Received {0}", event.getClass().getSimpleName());
		PlayerNameRecord playerNameRecord = playerNameRecordManager.create(event.getName());
		InetAddressRecord inetAddressRecord = inetAddressRecordManager.create(event.getAddress().getHostAddress());
		final Timestamp now = new Timestamp(System.currentTimeMillis());
		playerNameRecord.getAddresses().add(inetAddressRecord);
		playerNameRecord.setLastSeen(now);
		inetAddressRecord.setLastSeen(now);
		playerNameRecordManager.save(playerNameRecord);
		inetAddressRecordManager.save(inetAddressRecord);
	}

}