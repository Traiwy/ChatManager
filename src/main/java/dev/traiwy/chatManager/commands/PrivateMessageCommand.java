package dev.traiwy.chatManager.commands;

import dev.traiwy.chatManager.configuration.PluginConfiguration;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class PrivateMessageCommand implements TabExecutor {
	PluginConfiguration configuration;
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
		if(!(sender instanceof Player pSender)) return false;

		Player receiver = Bukkit.getPlayer(args[0]);

		if(receiver == null){
			pSender.sendMessage("Игрок не найден");
			return true;
		}

		String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

		if(message.isBlank()){
			pSender.sendMessage("Вы не указали сообщение");
			return true;
		}

		if(!receiver.isOnline()){
			pSender.sendMessage("Игрок оффлайн");
			return true;
		}

		receiver.sendMessage(configuration.privat().formatReceiver()
				.replace("{sender}", pSender.getName())
				.replace("{message}", message));

		pSender.sendMessage(configuration.privat().formatSender()
				.replace("{receiver}", receiver.getName())
				.replace("{message}", message));
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
		if(args.length != 1) return List.of();

		String typed = args[0].toLowerCase(Locale.ROOT);
		return  Bukkit.getOnlinePlayers().stream()
				.map(Player::getName)
				.filter(name -> name.toLowerCase(Locale.ROOT).startsWith(typed))
				.sorted()
				.toList();
	}
}
