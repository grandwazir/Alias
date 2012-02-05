package name.richardson.james.bukkit.alias;

import java.io.IOException;

import name.richardson.james.bukkit.util.Plugin;
import name.richardson.james.bukkit.util.configuration.AbstractConfiguration;

public class AliasConfiguration extends AbstractConfiguration {
  
  public final static String FILE_NAME = "config.yml";

  public AliasConfiguration(final Plugin plugin) throws IOException {
    super(plugin, AliasConfiguration.FILE_NAME);
  }
  
  public boolean isDebugging() {
    return this.configuration.getBoolean("debugging");
  }

}
