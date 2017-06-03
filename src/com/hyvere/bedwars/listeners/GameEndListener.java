package com.hyvere.bedwars.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.scarabcoder.gameapi.event.GameEndEvent;
import com.scarabcoder.gameapi.game.ArenaSettings;

public class GameEndListener implements Listener {
	
	@EventHandler
	public void onGameEnd(GameEndEvent e){
		ArenaSettings s = e.getGame().getArena().getArenaSettings();
		
		s.setCanDestroy(false);
		s.setCanBuild(false);
		s.setAllowInventoryChange(false);
	}
	
	
}
