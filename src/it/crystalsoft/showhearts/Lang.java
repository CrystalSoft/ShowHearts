package it.crystalsoft.showhearts;

import java.util.ListResourceBundle;
import org.bukkit.ChatColor;

public final class Lang extends ListResourceBundle
{
	public final Object[][] getContents()
	{
		return text;
	}

	private static final Object[][] text =
	{
		{"info1", ChatColor.GOLD + "This Plugin shows health status for all living entities (players, monsters and animals)."},
		{"info2", ChatColor.WHITE + "/sh head <0-1>" + ChatColor.AQUA + " to enable/disable health statuses above entities head (if disabled, statuses will appear in the player chat box [default])."},
		{"info3", ChatColor.WHITE + "/sh players <0-1>" + ChatColor.AQUA + " to enable/disable health statuses for players."},
		{"info4", ChatColor.WHITE + "/sh monsters <0-1>" + ChatColor.AQUA + " to enable/disable health statuses for monsters."},
		{"info5", ChatColor.WHITE + "/sh animals <0-1>" + ChatColor.AQUA + " to enable/disable health statuses for animals."},
		{"info6", ChatColor.WHITE + "/sh villagers <0-1>" + ChatColor.AQUA + " to enable/disable health statuses for villagers."},
		{"info7", ChatColor.WHITE + "/sh symbol <string>" + ChatColor.AQUA + " to set an health symbol."},
		{"info8", ChatColor.WHITE + "/sh clear" + ChatColor.AQUA + " to clean all health statuses."},
		{"info9", ChatColor.WHITE + "/sh reload" + ChatColor.AQUA + " to reload and clean Plugin configuration."},
		{"info10", ChatColor.WHITE + "/sh %d" + ChatColor.AQUA + " to go on page %d of this command list."},
		{"cleanHearts","Health status clean."},
		{"permissionError","Error, you can't set these parameters!"},
		{"reload","Configuration reloaded."},
		{"hearts","Health above entities head %s."},
		{"players","Health below players name %s."},
		{"monsters","Health above monsters head %s."},
		{"animals","Health above animals head %s."},
		{"villagers","Health above villagers head %s."},
		{"symbol","\"%s\" symbol set."},
		{"enabled","enabled"},
		{"disabled","disabled"},
		{"heartsOf","Health of"},
		{"syntax","Syntax error."}
	};
}