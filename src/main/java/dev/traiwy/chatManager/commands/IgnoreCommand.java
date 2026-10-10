package dev.traiwy.chatManager.commands;

import dev.traiwy.chatManager.IgnoreManager;
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

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class IgnoreCommand implements TabExecutor {
	IgnoreManager ignoreManager;

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
		if(!(sender instanceof Player player)) return false;

		if(args.length != 1){
			player.sendMessage("Укажи никнейм");
			return true;
		}

		Player target = Bukkit.getPlayer(args[0]);
		if(target ==  null){
			System.out.println("Игрок не найден");
			return true;
		}

		if (player.getUniqueId().equals(target.getUniqueId())) {
			player.sendMessage("Нельзя игнорировать себя");
			return true;
		}
		ignoreManager.add(player.getName(), target.getName());
		boolean ignoring = ignoreManager.isIgnoring(player.getName(), target.getName());
		player.sendMessage(ignoring ? "Теперь ты игнорируешь " + target.getName() : "Ты больше не игнорируешь " + target.getName());
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
		if(args.length != 1) return List.of();
		String typed = args[0].toLowerCase(Locale.ROOT);
		return Bukkit.getOnlinePlayers().stream()
				.map(Player::getName)
				.filter(n -> n.toLowerCase(Locale.ROOT).startsWith(typed))
				.sorted()
				.toList();
	}
}
