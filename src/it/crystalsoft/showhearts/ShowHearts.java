package it.crystalsoft.showhearts;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public final class ShowHearts extends JavaPlugin implements Listener
{
	private ResourceBundle text = null;
	private static HashMap<UUID, LivingEntity> last_aggressor = new HashMap<UUID, LivingEntity>();
	private Scoreboard scoreboard = null;

	@Override
	public void onEnable()
	{
		text = ResourceBundle.getBundle("it.crystalsoft.showhearts.Lang", Locale.getDefault());

		//Event Registration
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);

		last_aggressor.clear();

		scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

		createConfig();
		initialize();
	}
	 
	@Override
	public void onDisable()
	{
		clear();

		last_aggressor.clear();
	}

	private void initialize()
	{
		for (World w : Bukkit.getWorlds())
		{
			for (Entity e : w.getEntities())
			{
				if (e instanceof LivingEntity)
				{
					updateMobHealth((LivingEntity)e, 0, false);
				}
			}
		}
	}

	private void clear()
	{
		if (getConfig().getBoolean("head"))
		{
			for (Player p : Bukkit.getOnlinePlayers())
			{
				if (scoreboard != null)
				{
					Objective objective = scoreboard.getObjective(p.getName());

					if (objective != null)
					{
						objective.unregister();
					}
				}
			}

			for (World w : Bukkit.getWorlds())
			{
				for (Entity e : w.getEntities())
				{
					if ((e instanceof Animals) || (e instanceof NPC) || (e instanceof Monster))
					{
						if ((e.getCustomName() != null) && (e.getCustomName().isEmpty() || e.getCustomName().contains(getConfig().getString("symbol"))))
						{
							e.setCustomNameVisible(false);
							e.setCustomName("");
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();

		if (player != null)
		{
			last_aggressor.put(player.getUniqueId(), null);
		}
	}

	@EventHandler
	public void onPlayerLeft(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();

		if (player != null)
		{
			last_aggressor.remove(player.getUniqueId());
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (cmd.getName().equalsIgnoreCase("sh"))
		{
			if (args.length == 0)
			{
				sender.sendMessage(ChatColor.DARK_GREEN + String.format("%s v%s by CrystalSoft", getDescription().getName(), getDescription().getVersion()));
				sender.sendMessage(text.getString("info1"));
				sender.sendMessage(text.getString("info2"));
				sender.sendMessage(text.getString("info3"));
				sender.sendMessage(String.format(text.getString("info10"), 2, 2));
			}

			else if (args.length == 1)
			{
				clear();

				int page = 0;

				try
				{
					page = Integer.parseInt(args[0]);
				}

				catch (NumberFormatException e)
				{
					
				}

				if ((page >= 2) && (page <= 3))
				{
					sender.sendMessage(ChatColor.DARK_GREEN + String.format("%s v%s by CrystalSoft", getDescription().getName(), getDescription().getVersion()));
	
					switch (page)
					{
						case 2:
						{
							sender.sendMessage(text.getString("info4"));
							sender.sendMessage(text.getString("info5"));
							sender.sendMessage(text.getString("info6"));

							break;
						}

						case 3:
						{
							sender.sendMessage(text.getString("info7"));
							sender.sendMessage(text.getString("info8"));
							sender.sendMessage(text.getString("info9"));

							break;
						}
					}

					if (page < 3)
					{
						sender.sendMessage(String.format(text.getString("info10"), (page + 1), (page + 1)));
					}
				}

				else if (args[0].equalsIgnoreCase("clear"))
				{
					if (sender.hasPermission("showhearts.clear"))
					{
						initialize();

						sender.sendMessage(ChatColor.DARK_GREEN + "ShowHearts: " + ChatColor.GOLD + text.getString("cleanHearts"));
					}

					else
					{
						sender.sendMessage(ChatColor.DARK_RED + "ShowHearts: " + text.getString("permissionError"));
					}
				}

				else if (args[0].equalsIgnoreCase("reload"))
				{
					reloadConfig();

					sender.sendMessage(ChatColor.DARK_GREEN + "ShowHearts: " + ChatColor.GOLD + text.getString("reload"));
				}

				else
				{
					sender.sendMessage(ChatColor.DARK_RED + "ShowHearts: " + text.getString("syntax"));
				}

				initialize();
			}

			else if (args.length == 2)
			{
				clear();

				if (args[0].equalsIgnoreCase("head"))
				{
					if (sender.hasPermission("showhearts.head"))
					{
						getConfig().set("head", args[1].equalsIgnoreCase("1") ? true : false);

						sender.sendMessage(ChatColor.DARK_GREEN + "ShowHearts: " + ChatColor.GOLD + String.format(text.getString("hearts"), getConfig().getBoolean("head") ? text.getString("enabled") : text.getString("disabled")));
					}

					else
					{
						sender.sendMessage(ChatColor.DARK_RED + "ShowHearts: " + text.getString("permissionError"));
					}
				}

				else if (args[0].equalsIgnoreCase("players"))
				{
					if (sender.hasPermission("showhearts.players"))
					{
						getConfig().set("players", args[1].equalsIgnoreCase("1") ? true : false);

						sender.sendMessage(ChatColor.DARK_GREEN + "ShowHearts: " + ChatColor.GOLD + String.format(text.getString("players"), getConfig().getBoolean("players") ? text.getString("enabled") : text.getString("disabled")));
					}

					else
					{
						sender.sendMessage(ChatColor.DARK_RED + "ShowHearts: " + text.getString("permissionError"));
					}
				}

				else if (args[0].equalsIgnoreCase("monsters"))
				{
					if (sender.hasPermission("showhearts.monsters"))
					{
						getConfig().set("monsters", args[1].equalsIgnoreCase("1") ? true : false);

						sender.sendMessage(ChatColor.DARK_GREEN + "ShowHearts: " + ChatColor.GOLD + String.format(text.getString("monsters"), getConfig().getBoolean("monsters") ? text.getString("enabled") : text.getString("disabled")));
					}

					else
					{
						sender.sendMessage(ChatColor.DARK_RED + "ShowHearts: " + text.getString("permissionError"));
					}
				}

				else if (args[0].equalsIgnoreCase("animals"))
				{
					if (sender.hasPermission("showhearts.animals"))
					{
						getConfig().set("animals", args[1].equalsIgnoreCase("1") ? true : false);

						sender.sendMessage(ChatColor.DARK_GREEN + "ShowHearts: " + ChatColor.GOLD + String.format(text.getString("animals"), getConfig().getBoolean("animals") ? text.getString("enabled") : text.getString("disabled")));
					}

					else
					{
						sender.sendMessage(ChatColor.DARK_RED + "ShowHearts: " + text.getString("permissionError"));
					}
				}

				else if (args[0].equalsIgnoreCase("villagers"))
				{
					if (sender.hasPermission("showhearts.villagers"))
					{
						getConfig().set("villagers", args[1].equalsIgnoreCase("1") ? true : false);

						sender.sendMessage(ChatColor.DARK_GREEN + "ShowHearts: " + ChatColor.GOLD + String.format(text.getString("villagers"), getConfig().getBoolean("villagers") ? text.getString("enabled") : text.getString("disabled")));
					}

					else
					{
						sender.sendMessage(ChatColor.DARK_RED + "ShowHearts: " + text.getString("permissionError"));
					}
				}

				else if (args[0].equalsIgnoreCase("symbol"))
				{
					if (sender.hasPermission("showhearts.symbol"))
					{
						getConfig().set("symbol", args[1]);

						sender.sendMessage(ChatColor.DARK_GREEN + "ShowHearts: " + ChatColor.GOLD + String.format(text.getString("symbol"), getConfig().getString("symbol")));
					}

					else
					{
						sender.sendMessage(ChatColor.DARK_RED + "ShowHearts: " + text.getString("permissionError"));
					}
				}

				else if (args[0].equalsIgnoreCase("locale"))
				{
					if (sender.hasPermission("showhearts.locale"))
					{
						Locale locale = null;

						if (args[1].equalsIgnoreCase("default"))
						{
							locale = Locale.getDefault();
						}

						else
						{
							locale = Utils.stringToLocale(args[1]);
						}

						if (locale != null)
						{
							text = ResourceBundle.getBundle("it.crystalsoft.showhearts.Lang", locale);
						}
					}

					else
					{
						sender.sendMessage(ChatColor.DARK_RED + "ShowHearts: " + text.getString("permissionError"));
					}
				}

				saveConfig();
				initialize();
			}

			return true;
		}
	
		return false; 
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{
		LivingEntity entity = event.getEntity();

		if (entity != null)
		{
			updateMobHealth(entity, 0, false);
		}
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onEntityDamage(EntityDamageEvent event)
	{
		LivingEntity damager = null;
		LivingEntity victim = null;

		if (event.getEntity() instanceof LivingEntity)
		{
			victim = (LivingEntity)event.getEntity();
		}

		if (victim != null)
		{
			if (event instanceof EntityDamageByEntityEvent)
			{
				if (((EntityDamageByEntityEvent)event).getDamager() instanceof LivingEntity)
				{
					damager = (LivingEntity)((EntityDamageByEntityEvent)event).getDamager();
				}

				else if (((EntityDamageByEntityEvent)event).getDamager() instanceof Arrow)
				{
					if (((Arrow)((EntityDamageByEntityEvent)event).getDamager()).getShooter() instanceof LivingEntity)
					{
						damager = (LivingEntity)((Arrow)((EntityDamageByEntityEvent)event).getDamager()).getShooter();
					}
				}
			}

			else if (victim.getFireTicks() <= 0)
			{
				last_aggressor.put(victim.getUniqueId(), null);
			}

			send(victim, damager, event.getFinalDamage());
		}
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onEntityHit(EntityDamageByEntityEvent event)
	{
		if (event.getEntity() instanceof Arrow)
		{
			Arrow arrow = (Arrow)event.getEntity();

			LivingEntity damager = null;
			LivingEntity victim = null;

			if (event.getEntity() instanceof LivingEntity)
			{
				victim = (LivingEntity)event.getEntity();
			}

			if (arrow.getShooter() instanceof LivingEntity)
			{
				damager = (LivingEntity)arrow.getShooter();
			}

			send(victim, damager, event.getFinalDamage());
		}
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onHeal(EntityRegainHealthEvent event)
	{
		Entity entity = event.getEntity();

		if (entity instanceof Player)
		{
			updatePlayerHealth((Player)entity, -event.getAmount());
		}

		else if (entity instanceof LivingEntity)
		{
			updateMobHealth((LivingEntity)entity, -event.getAmount(), false);
		}
	}

	private void send(LivingEntity victim, LivingEntity damager, double damage)
	{
		if (victim != null)
		{
			if ((damager == null) && (last_aggressor.get(victim.getUniqueId()) != null))
			{
				damager = last_aggressor.get(victim.getUniqueId());
			}

			last_aggressor.put(victim.getUniqueId(), damager);

			// Prevent entities bad name when kill someone
			if ((victim.getHealth() - damage) <= 0.0) // Victim is dead
			{
				LivingEntity entities[] = {damager, victim};

				for (int i = 0; i < entities.length; i++)
				{
					if (entities[i] != null)
					{
						if (entities[i] instanceof Player)
						{
							
						}
	
						else if (entities[i] instanceof LivingEntity)
						{
							if ((entities[i] instanceof Animals && getConfig().getBoolean("animals")) || (entities[i] instanceof Monster && getConfig().getBoolean("monsters")))
							{
								if ((entities[i].getCustomName() != null) && (entities[i].getCustomName().isEmpty() || entities[i].getCustomName().contains(getConfig().getString("symbol"))))
								{
									entities[i].setCustomNameVisible(false);
									entities[i].setCustomName("");
								}
							}
						}
					}
				}
			}

			else
			{
				if (getConfig().getBoolean("head"))
				{
					if (victim instanceof Player)
					{
						updatePlayerHealth((Player)victim, damage);
					}
		
					else if (victim instanceof LivingEntity)
					{
						updateMobHealth(victim, damage, true);
					}
				}
	
				else
				{
					if ((damager != null) && (damager instanceof Player))
					{
						damager.sendMessage(getHeartsArt(victim, damage));
					}
				}
			}
		}
	}

	private int getLivingEntityPercentageHealth(LivingEntity e, double damage)
	{
		return (int)((float)((float)(e.getHealth() - damage) / (float)e.getMaxHealth()) * 100);
	}

	private void updatePlayerHealth(Player p, double damage)
	{
		if (getConfig().getBoolean("head") && getConfig().getBoolean("players"))
		{
			if (scoreboard == null)
			{
				scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
			}

			if (scoreboard != null)
			{
				Objective objective = null;

				if (scoreboard.getObjective(p.getName()) == null)
				{
					objective = scoreboard.registerNewObjective(p.getName(), "dummy");
				}

				else
				{
					objective = scoreboard.getObjective(p.getName());

					if (objective != null)
					{
						String art = p.getPlayerListName() + " " + getHeartsArt(p, damage);

						objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
						objective.setDisplayName(art);
						//objective.getScore("dummy").setScore((int)p.getHealth());
						//objective.getScore(DisplaySlot.BELOW_NAME).setScore(getLivingEntityPercentageHealth(p, damage));
						objective.getScore(p.getName()).setScore(getLivingEntityPercentageHealth(p, damage));
					}
				}

				p.setScoreboard(scoreboard);
			}
		}
	}

	private void updateMobHealth(LivingEntity e, double damage, boolean display)
	{
		if (getConfig().getBoolean("head"))
		{
			if ((e instanceof Animals && getConfig().getBoolean("animals")) || (e instanceof NPC && getConfig().getBoolean("villagers")) || (e instanceof Monster && getConfig().getBoolean("monsters")))
			{
				if ((e.getCustomName() == null) || ((e.getCustomName() != null) && (e.getCustomName().isEmpty() || e.getCustomName().contains(getConfig().getString("symbol")))))
				{
					e.setCustomNameVisible(display);
					e.setCustomName(getLivingEntityPercentageHealth(e, damage) + "% " + getHeartsArt(e, damage));
				}
			}
		}
	}

	private String getHeartsArt(LivingEntity entity, double realDamage)
	{
		String hearts = (getConfig().getBoolean("head") ? "" : (ChatColor.YELLOW + text.getString("heartsOf") + " " + ChatColor.GREEN + entity.getName() + ChatColor.YELLOW + ": ")) + ChatColor.DARK_RED;
		int damage = (int)Math.floor((int)((entity.getHealth() - realDamage) / 2));

		for (int i = 0; i < damage; i++)
		{
			hearts += getConfig().getString("symbol");
		}

		int maxHealth = (int)Math.floor((int)(entity.getMaxHealth() / 2));

		if (damage < maxHealth)
		{
			hearts += ChatColor.BLACK;

			for (int i = damage; i < maxHealth; i++)
			{
				hearts += getConfig().getString("symbol");
			}
		}

		return hearts;
	}

	private void createConfig()
	{
		try
		{
			if (!getDataFolder().exists())
			{
				getDataFolder().mkdirs();
			}

			File file = new File(getDataFolder(), "config.yml");

			if (!file.exists())
			{
				getLogger().info("config.yml not found, creating!");

				saveDefaultConfig();
			}

			else
			{
				getLogger().info("config.yml found, loading!");
			}
		}

		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
