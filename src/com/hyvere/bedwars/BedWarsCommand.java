package com.hyvere.bedwars;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.scarabcoder.gameapi.game.GamePlayer;
import com.scarabcoder.gameapi.game.Team;
import com.scarabcoder.gameapi.manager.PlayerManager;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class BedWarsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2,
			String[] args) {
		
		if(sender.hasPermission("bedwars.admin")){
			if(sender instanceof Player){
				GamePlayer p = PlayerManager.getGamePlayer((Player) sender);
				if(args.length == 1){
					if(args[0].equalsIgnoreCase("enable")){
						BedWars.getPlugin().getConfig().set("enabled", true);
						BedWars.getPlugin().saveConfig();
						sender.sendMessage(ChatColor.GREEN + "BedWars enabled, restart server to open game.");
					}else if(args[0].equalsIgnoreCase("disable")){
						BedWars.getPlugin().getConfig().set("enabled", false);
						BedWars.getPlugin().saveConfig();
						sender.sendMessage(ChatColor.GREEN + "BedWars disabled, restart server to close game.");
					}
				}else if(args.length == 2){
					if(args[0].equalsIgnoreCase("setspawn")){
						Team t = BedWars.getTeam(args[1]);
						if(t != null){
							BedWars.getPlugin().getConfig().set("locations." + t.getName() + ".spawn", p.getOnlinePlayer().getLocation());
							BedWars.getPlugin().saveConfig();
							sender.sendMessage(ChatColor.GREEN + "Set " + t.getName() + " team spawn.");
						}else if(args[1].equalsIgnoreCase("spectator") || args[1].equalsIgnoreCase("lobby")){
							BedWars.getPlugin().getConfig().set("locations." + args[1] + "Spawn", p.getOnlinePlayer().getLocation());
							BedWars.getPlugin().saveConfig();
							sender.sendMessage(ChatColor.GREEN + "Set " + args[1] + " spawn.");
						}else{
							sender.sendMessage(ChatColor.RED + "Team/location type not found!");
						}
					}else if(args[0].equalsIgnoreCase("setarea")){
						if(BedWars.getWorldEdit() != null){
							Team t = BedWars.getTeam(args[1]);
							if(t != null){
								if(BedWars.getWorldEdit().getSelection(p.getOnlinePlayer()) != null){
									Selection s = BedWars.getWorldEdit().getSelection(p.getOnlinePlayer());
									BedWars.getPlugin().getConfig().set("locations." + t.getName() + ".area.1", s.getMinimumPoint());
									BedWars.getPlugin().getConfig().set("locations." + t.getName() + ".area.2", s.getMaximumPoint());
									BedWars.getPlugin().saveConfig();
									sender.sendMessage(ChatColor.GREEN + "Set " + t.getName() + " team area.");
								}else{
									sender.sendMessage(ChatColor.RED + "Please make a WorldEdit selection first.");
								}
							}else{
								sender.sendMessage(ChatColor.RED + "Team not found.");
							}
						}else{
							sender.sendMessage(ChatColor.RED + "Please install WorldEdit to set areas.");
						}
					}
				}else if(args.length == 0){
					List<String> msgs = Arrays.asList(ChatColor.DARK_GRAY + "---- " + ChatColor.GREEN + "BedWars Commands" + ChatColor.DARK_GRAY + " ----",
							"/bw <enable/disable>" + ChatColor.GRAY + ": Disables or enables the game. You should set all spawn locations before enabling.",
							"/bw setspawn <team name/spectator/lobby>" + ChatColor.GRAY + ": Set spawn for lobby, spectator, or team.",
							"/bw setarea <team name>" + ChatColor.GRAY + ": Set team area from WorldEdit selection.");
					for(String m : msgs){
						sender.sendMessage(m);
					}
				}
			}else{
				sender.sendMessage(ChatColor.RED + "Player-only command!");
			}
		}else{
			sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
		}
		
		return true;
	}

}
