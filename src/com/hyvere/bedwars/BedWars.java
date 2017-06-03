package com.hyvere.bedwars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.hyvere.bedwars.listeners.GameEndListener;
import com.hyvere.bedwars.listeners.GameStartListener;
import com.hyvere.bedwars.listeners.PlayerJoinGameListener;
import com.scarabcoder.gameapi.enums.TeamSpreadType;
import com.scarabcoder.gameapi.game.Area;
import com.scarabcoder.gameapi.game.Arena;
import com.scarabcoder.gameapi.game.ArenaSettings;
import com.scarabcoder.gameapi.game.GamePlayer;
import com.scarabcoder.gameapi.game.GameSettings;
import com.scarabcoder.gameapi.game.Team;
import com.scarabcoder.gameapi.manager.GameManager;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class BedWars extends JavaPlugin {
	
	private static BedWarsGame game;
	private static WorldEditPlugin we;
	private static Plugin plugin;
	
	
	private static List<Team> teams = new ArrayList<Team>();

	@Override
	public void onEnable(){
		
		plugin = this;
		
		we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		
		if(we == null){
			System.out.println("Soft dependency WorldEdit not found! You won't be able to set areas without WorldEdit.");
		}
		
		this.getConfig().options().copyDefaults(true);
		this.saveDefaultConfig();
		
		

		Team red = new Team(Color.RED, ChatColor.RED, "Red");
		Team green = new Team(Color.GREEN, ChatColor.GREEN, "Green");
		Team blue = new Team(Color.BLUE, ChatColor.BLUE, "Blue");
		Team gold = new Team(Color.ORANGE, ChatColor.GOLD, "Gold");
		
		teams.addAll(Arrays.asList(red, green, blue, gold));
		
		this.getCommand("bedwars").setExecutor(new BedWarsCommand());
		
		Arena arena = new Arena("bedwars");
		
		System.out.println(this.getConfig().getBoolean("enabled"));
		if(this.getConfig().getBoolean("enabled")){
			if(this.getConfig().contains("locations.lobbySpawn"))
				arena.setLobbySpawn((Location) this.getConfig().get("locations.lobbySpawn"));
			if(this.getConfig().contains("locations.spectatorSpawn"))
				arena.setSpectatorSpawn((Location)this.getConfig().get("locations.spectatorSpawn"));
			
			game = new BedWarsGame("bedwars", arena, this);
			
			game.setMessagePrefix(ChatColor.GRAY + "[" + ChatColor.DARK_AQUA + "Bed Wars" + ChatColor.GRAY + "]");
			
			GameSettings gSettings = game.getGameSettings();
			ArenaSettings aSettings = game.getArena().getArenaSettings();
			
			gSettings.setAutomaticCountdown(true);
			gSettings.setCountdownTime(30);
			gSettings.setMinimumTeamSize(1);
			gSettings.setMaximumTeamSize(4);
			gSettings.setMaximumPlayers(16);
			gSettings.setTeamSpreadType(TeamSpreadType.EVEN);
			gSettings.setUsesBungee(true);
			gSettings.setLobbyServer("hub");
			gSettings.setMinimumPlayers(2);
			gSettings.shouldSetListPlayerCount(true);
			gSettings.setAllowPvPBeforeGameStart(false);
			gSettings.setMOTD(true);
			gSettings.shouldUseTeams(true);
			gSettings.shouldLeavePlayerOnDisconnect(true);
			
			aSettings.setAllowWeatherChange(false);
			aSettings.setCanDestroy(false);
			aSettings.setCanBuild(false);
			aSettings.setAllowInventoryChange(false);
			aSettings.setAllowDurabilityChange(false);
			aSettings.setAllowFoodLevelChange(false);
			
			for(Team t : teams){
				game.getTeamManager().registerTeam(t);
			}
			
			for(Team t : game.getTeamManager().getTeams()){
				if(this.getConfig().contains("loctions." + t.getName() + ".spawn"))
					t.addTeamSpawn((Location) this.getConfig().get("locations." + t.getName() + ".spawn"));
				
				if(this.getConfig().contains("locations."+ t.getName() + ".area")){
					Area tArea = new Area((Location) this.getConfig().get("locations." + t.getName() + ".area.1"),
							(Location) this.getConfig().get("locations." + t.getName() + ".area.2"), t.getName());
					game.registerArea(tArea);
				}
			}
			
			GameManager.registerGame(game);
		}
		
		
		Bukkit.getPluginManager().registerEvents(new GameEndListener(), this);
		Bukkit.getPluginManager().registerEvents(new GameStartListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerJoinGameListener(), this);
		
		
	}
	
	
	public static void giveDefaultInventory(GamePlayer p){
		if(p.isOnline() && p.getTeam() != null){
			PlayerInventory inv = p.getOnlinePlayer().getInventory();
			inv.setItem(0, new ItemStack(Material.STONE_SWORD));
			
			ItemStack h = new ItemStack(Material.LEATHER_HELMET);
			ItemStack c = new ItemStack(Material.LEATHER_CHESTPLATE);
			ItemStack l = new ItemStack(Material.LEATHER_LEGGINGS);
			ItemStack b = new ItemStack(Material.LEATHER_BOOTS);
			
			LeatherArmorMeta m = (LeatherArmorMeta) h.getItemMeta();
			m.setColor(p.getTeam().getColor());
			
			h.setItemMeta(m);
			c.setItemMeta(m);
			l.setItemMeta(m);
			b.setItemMeta(m);
			
			inv.setHelmet(h);
			inv.setChestplate(c);
			inv.setLeggings(l);
			inv.setBoots(b);
			
		}
	}
	
	public static Team getTeam(String name){
		for(Team t : teams){
			if(t.getName().equalsIgnoreCase(name))
				return t;
				
		}
		return null;
	}
	
	public static WorldEditPlugin getWorldEdit(){
		return we;
	}
	
	public static Plugin getPlugin(){
		return plugin;
	}
	
	public static BedWarsGame getGame(){
		return game;
	}
	
}
