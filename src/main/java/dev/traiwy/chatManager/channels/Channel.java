package dev.traiwy.chatManager.channels;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface Channel {
	String trigger();
	Component messageWithPrefix(Player sender, String message);
	boolean canReceive(Player sender, Player receiver);
}
