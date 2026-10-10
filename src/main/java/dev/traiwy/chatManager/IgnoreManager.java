package dev.traiwy.chatManager;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Accessors(fluent = true)
public final class IgnoreManager {
	Map<String, Set<String>> ignoredPlayers = new HashMap<>();

	public void add(String playerName, String otherPlayerName){
		Set<String> ignored = ignoredPlayers.computeIfAbsent(
				playerName, key -> new HashSet<>()
		);
		if(ignored.contains(otherPlayerName)){
			remove(playerName, otherPlayerName);
			return;
		}
		ignored.add(otherPlayerName);
	}

	public boolean isIgnoring(String playerName, String otherPlayerName){
		Set<String> ignored = ignoredPlayers.get(playerName);
		return ignored != null && ignored.contains(otherPlayerName);
	}

	public void remove(String playerName, String otherPlayerName){
		Set<String> ignored = ignoredPlayers.get(playerName);
		if(ignored == null) return;
		ignored.remove(otherPlayerName);
		if(ignored.isEmpty()) ignoredPlayers.remove(playerName);
	}

}
