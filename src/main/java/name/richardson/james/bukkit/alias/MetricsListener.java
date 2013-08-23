package name.richardson.james.bukkit.alias;

import java.io.IOException;

import org.bukkit.plugin.PluginManager;

import org.mcstats.Metrics;

import name.richardson.james.bukkit.utilities.listener.AbstractListener;

import name.richardson.james.bukkit.alias.persistence.InetAddressRecordManager;
import name.richardson.james.bukkit.alias.persistence.PlayerNameRecordManager;

public class MetricsListener {

	private final Metrics metrics;

	public MetricsListener(Alias alias)
	throws IOException {
		this.metrics = new Metrics(alias);
		this.metrics.start();
	}

}
