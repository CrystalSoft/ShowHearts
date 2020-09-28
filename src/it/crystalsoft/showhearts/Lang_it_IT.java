package it.crystalsoft.showhearts;

import java.util.ListResourceBundle;

import org.bukkit.ChatColor;

public final class Lang_it_IT extends ListResourceBundle
{
	public final Object[][] getContents()
	{
		return text;
	}
	
	private static final Object[][] text =
	{
		{"info1", ChatColor.GOLD + "Questo Plugin mostra lo stato di salute di tutti i giocatori, mostri, animali e villageri."},
		{"info2", ChatColor.WHITE + "/sh head <0-1>" + ChatColor.AQUA + " per abilitare/disabilitare gli stati di salute in testa alle entità (se disabilitati, appariranno in chat [default])."},
		{"info3", ChatColor.WHITE + "/sh players <0-1>" + ChatColor.AQUA + " per abilitare/disabilitare gli stati di salute per i giocatori."},
		{"info4", ChatColor.WHITE + "/sh monsters <0-1>" + ChatColor.AQUA + " per abilitare/disabilitare gli stati di salute per i mostri."},
		{"info5", ChatColor.WHITE + "/sh animals <0-1>" + ChatColor.AQUA + " per abilitare/disabilitare gli stati di salute per gli animali."},
		{"info6", ChatColor.WHITE + "/sh villagers <0-1>" + ChatColor.AQUA + " per abilitare/disabilitare gli stati di salute per i villageri."},
		{"info7", ChatColor.WHITE + "/sh symbol <stringa>" + ChatColor.AQUA + " per impostare un simbolo per lo stato di salute."},
		{"info8", ChatColor.WHITE + "/sh clear" + ChatColor.AQUA + " per pulire tutti gli stati di salute generati."},
		{"info9", ChatColor.WHITE + "/sh reload" + ChatColor.AQUA + " per ricaricare e pulire la configurazione del Plugin."},
		{"info10", ChatColor.WHITE + "/sh %d" + ChatColor.AQUA + " per andare a pagina %d della lista comandi."},
		{"cleanHearts","Stati di salute puliti."},
		{"permissionError","Errore, non puoi settare questi parametri!"},
		{"reload","Configurazione ricaricata."},
		{"hearts","Salute sopra la testa delle entità %s."},
		{"players","Salute sotto il nome dei giocatori %s."},
		{"monsters","Salute sopra la testa dei mostri %s."},
		{"animals","Salute sopra la testa degli animali %s."},
		{"villagers","Salute sopra la testa dei villageri %s."},
		{"symbol","Simbolo \"%s\" impostato."},
		{"enabled","abilitata"},
		{"disabled","disabilitata"},
		{"heartsOf","Salute di"},
		{"syntax","Errore di sintassi."}
	};
}