package dev.traiwy.chatManager.listener;

import dev.traiwy.chatManager.IgnoreManager;
import dev.traiwy.chatManager.channels.Channel;
import dev.traiwy.chatManager.channels.ManagerChannel;
import dev.traiwy.chatManager.configuration.PluginConfiguration;
import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public final class ChatListener implements Listener {
	PluginConfiguration configuration;
	ManagerChannel channels;
	IgnoreManager ignoreManager;

	@EventHandler(ignoreCancelled = true)
	public void onChat(AsyncChatEvent event) {
		Player player = event.getPlayer();
		String message = PlainTextComponentSerializer.plainText().serialize(event.message());
		event.setCancelled(true);

		Channel channel = channels.getNowChannel(message);
		String text = channels.removeTrigger(message, channel.trigger());


		for (Player receiver : Bukkit.getOnlinePlayers()) {
			if(!ignoreManager.isIgnoring(receiver.getName(), player.getName())){
				if (channel.canReceive(player, receiver)) {
					receiver.sendMessage(channel.messageWithPrefix(player, text));
				}
			}
		}
	}
}
