package dev.traiwy.chatManager.commands.admin;

import dev.traiwy.chatManager.ChatManager;
import dev.traiwy.chatManager.configuration.PluginConfiguration;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class ReloadCommand implements TabExecutor {
	ChatManager plugin;
	PluginConfiguration configuration;

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) throws RuntimeException {
		if(!(sender instanceof Player p)) return false;

		if(!p.hasPermission(configuration.permissionReload())) {
			sender.sendMessage("У вас нет прав");
			return true;
		}

		if(args.length > 1){
			sender.sendMessage(" You need to specify 1 file");
			return true;
		}

		String name = args.length == 0 ? "config" : args[0];

		if (!name.matches("[a-zA-Z0-9_-]+")) {
			sender.sendMessage("\n" + "Use letters, numbers, _ and -");
			return true;
		}

		try {
			plugin.reloadSettings(name + ".json");
			sender.sendMessage("Config loaded: " + name + ".json");
		} catch (IOException | RuntimeException ex) {
			throw new RuntimeException(ex);
		}

		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
		if(args.length != 1) return List.of();

		String typed = args[0].toLowerCase(Locale.ROOT);
		try {
			return getConfigNames().stream()
					.filter(name -> name.toLowerCase(Locale.ROOT)
							.startsWith(typed))
					.toList();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private List<String> getConfigNames() throws IOException {
		Path folder = plugin.getDataFolder().toPath();

		try (var files = Files.list(folder)) {
			return files
					.filter(Files::isRegularFile)
					.map(path -> path.getFileName().toString())
					.filter(name -> name.endsWith(".json"))
					.map(name -> name.substring(
							0, name.length() - ".json".length()
					))
					.sorted()
					.toList();
		}
	}
}
