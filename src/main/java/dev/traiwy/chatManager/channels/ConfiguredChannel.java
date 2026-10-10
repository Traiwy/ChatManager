package dev.traiwy.chatManager.channels;

import dev.traiwy.chatManager.Utils.IconsUtils;
import dev.traiwy.chatManager.configuration.ChannelConfiguration;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class ConfiguredChannel implements Channel{
	ChannelConfiguration settings;


	@Override
	public String trigger() {
		return settings.trigger();
	}

	@Override
	public Component messageWithPrefix(Player sender, String message) {
		Component textWithIcons = IconsUtils.replaceIcons(message);

		return MiniMessage.miniMessage().deserialize(
				settings.format(),
				Placeholder.unparsed("player", sender.getName()),
				Placeholder.component("message", textWithIcons)
		);
	}

	@Override
	public boolean canReceive(Player sender, Player receiver) {
		double radius = settings.radius();
		if (radius == -1) return true;
		if (!sender.getWorld().equals(receiver.getWorld())) return false;

		double distanceSquared = sender.getLocation().distanceSquared(receiver.getLocation());
		return distanceSquared <= radius * radius;
	}
}
