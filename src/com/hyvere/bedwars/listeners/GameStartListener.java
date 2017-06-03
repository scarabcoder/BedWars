package com.hyvere.bedwars.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.hyvere.bedwars.BedWars;
import com.scarabcoder.gameapi.event.GameStartEvent;
import com.scarabcoder.gameapi.game.ArenaSettings;
import com.scarabcoder.gameapi.game.GamePlayer;

public class GameStartListener implements Listener {
	
	@EventHandler
	public void onGameStart(GameStartEvent e){
		ArenaSettings s = e.getGame().getArena().getArenaSettings();
		
		s.setCanDestroy(true);
		s.setCanBuild(true);
		s.setAllowInventoryChange(true);
		
		
		for(GamePlayer p : e.getGame().getPlayers()){
			p.getOnlinePlayer().teleport(p.getTeam().getTeamSpawns().get(0));
			p.getOnlinePlayer().getInventory().clear();
			BedWars.giveDefaultInventory(p);
		}
		
		
		
	}
	
}
