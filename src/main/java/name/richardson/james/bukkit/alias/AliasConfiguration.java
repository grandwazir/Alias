/*******************************************************************************
 * Copyright (c) 2012 James Richardson.
 * 
 * AliasConfiguration.java is part of Alias.
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
