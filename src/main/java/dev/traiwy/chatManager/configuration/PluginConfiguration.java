package dev.traiwy.chatManager.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class PluginConfiguration {
	String permissionReload = "chat.admin.reload";
	Map<String, ChannelConfiguration> channels = new HashMap<>(Map.of(
			"local", new ChannelConfiguration("local", "", 10, "L {player} : {message}", "")
	));
	PrivateMessageConfiguration privat = new PrivateMessageConfiguration(true,
			"[я -> {receiver}] : {message}",
			"[{sender} -> я] : {message}");
}
