package dev.traiwy.chatManager.Utils;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@UtilityClass
public class IconsUtils {
	public Component replaceIcons(String message) {
		return Component.text(message).replaceText(builder -> builder
				.match("%([a-z0-9_]+)%")
				.replacement((match, original) -> {
					String name = match.group(1);

					return MiniMessage.miniMessage().deserialize("<sprite:items:item/" + name + ">");
				})
		);
	}
}
