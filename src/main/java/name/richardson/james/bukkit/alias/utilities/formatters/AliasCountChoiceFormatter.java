package name.richardson.james.bukkit.alias.utilities.formatters;

import name.richardson.james.bukkit.utilities.formatters.AbstractChoiceFormatter;
import name.richardson.james.bukkit.utilities.formatters.ChoiceFormatter;

public class AliasCountChoiceFormatter extends AbstractChoiceFormatter {

	public AliasCountChoiceFormatter() {
		this.setLimits(1,2);
		this.setFormats("one-alias", "many-aliases");
	}

}
