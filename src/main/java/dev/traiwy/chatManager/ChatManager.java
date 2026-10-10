package dev.traiwy.chatManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.traiwy.chatManager.channels.ManagerChannel;
import dev.traiwy.chatManager.commands.IgnoreCommand;
import dev.traiwy.chatManager.commands.PrivateMessageCommand;
import dev.traiwy.chatManager.commands.admin.ReloadCommand;
import dev.traiwy.chatManager.configuration.NotificationConfiguration;
import dev.traiwy.chatManager.configuration.PluginConfiguration;
import dev.traiwy.chatManager.listener.ChatListener;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ChatManager extends JavaPlugin {
	PluginConfiguration configuration;
	NotificationConfiguration notificationConfiguration;
	ManagerChannel channels;

	@Override
	public void onEnable() {
		configuration = new PluginConfiguration();
		notificationConfiguration = new NotificationConfiguration();
		try {
			configuration = loadConfiguration("config.json", PluginConfiguration.class, configuration);
			notificationConfiguration = loadConfiguration("notification.json", NotificationConfiguration.class, notificationConfiguration);
		} catch (IOException e) {
			try {
				throw new IOException(e.getMessage());
			} catch (IOException ex) {
				throw new RuntimeException(ex.getMessage());
			}
		}
		IgnoreManager ignoreManager = new IgnoreManager();
		getCommand("chat_reload").setExecutor(new ReloadCommand(this, configuration));
		getCommand("msg").setExecutor(new PrivateMessageCommand(configuration));
		getCommand("ignore").setExecutor(new IgnoreCommand(ignoreManager));
		getServer().getPluginManager().registerEvents(new ChatListener(configuration, new ManagerChannel(configuration), ignoreManager), this);
	}

	@Override
	public void onDisable() {
	}

	public <T> T loadConfiguration(String nameConfig, Class<T> type, T defaults) throws IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Path folder = getDataFolder().toPath();
		Files.createDirectories(folder);

		Path configFile = folder.resolve(nameConfig);
		if (Files.notExists(configFile)) {

			try (BufferedWriter writer = Files.newBufferedWriter(configFile, StandardCharsets.UTF_8)) {
				gson.toJson(defaults, writer);
			}

			return defaults;
		}
		try (BufferedReader reader = Files.newBufferedReader(configFile, StandardCharsets.UTF_8)) {
			T loaded = gson.fromJson(reader, type);
			return loaded;
		}
	}

	public void reloadSettings(String fileName) throws IOException {
		configuration = loadConfiguration(fileName, PluginConfiguration.class, configuration);
		channels = new ManagerChannel(configuration);
	}
}
