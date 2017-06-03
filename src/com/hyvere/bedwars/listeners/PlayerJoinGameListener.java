package com.hyvere.bedwars.listeners;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.hyvere.bedwars.BedWars;
import com.scarabcoder.gameapi.enums.GamePlayerType;
import com.scarabcoder.gameapi.enums.GameStatus;
import com.scarabcoder.gameapi.event.PlayerJoinGameEvent;
import com.scarabcoder.gameapi.game.Team;

public class PlayerJoinGameListener implements Listener {
	
	@EventHandler
	public void onPlayerJoinGame(PlayerJoinGameEvent e){
		if(BedWars.getGame().getGameStatus().equals(GameStatus.WAITING)){
			e.getPlayer().getOnlinePlayer().teleport(e.getGame().getArena().getLobbySpawn());
			Team team = BedWars.getGame().addToTeam(e.getPlayer());
			if(team != null){
				e.getGame().sendMessage(team.getChatColor() + e.getPlayer().getPlayer().getName() + ChatColor.GRAY + " joined the game.");
			}
			
		}else{
			e.getPlayer().getOnlinePlayer().teleport(e.getGame().getArena().getSpectatorSpawn());
			e.setGamePlayerType(GamePlayerType.SPECTATOR);
		}
	}
	
}
