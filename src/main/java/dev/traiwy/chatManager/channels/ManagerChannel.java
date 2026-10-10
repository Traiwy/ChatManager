package dev.traiwy.chatManager.channels;

import dev.traiwy.chatManager.configuration.PluginConfiguration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class ManagerChannel {
	Map<String, Channel> channels = new HashMap<>();

	public ManagerChannel(PluginConfiguration configuration) {
		configuration.channels().forEach((id, settings) -> {
			channels.put(id, new ConfiguredChannel(settings));
		});
	}

	public Channel getNowChannel(String message) {
		Channel defaultChannel = null;
		Channel selectedChannel = null;

		for (Channel channel : channels.values()) {
			String trigger = channel.trigger();

			if (trigger.isEmpty()) {
				defaultChannel = channel;
				continue;
			}

			if (message.startsWith(trigger)) {
				if (selectedChannel == null || trigger.length() > selectedChannel.trigger().length())
					selectedChannel = channel;
			}
		}

		if (selectedChannel != null) {
			return selectedChannel;
		}

		if (defaultChannel == null) throw new IllegalStateException("Channel with an empty trigger is not configured.");


		return defaultChannel;
	}


	public String removeTrigger(String message, String trigger) {
		if (message.startsWith(trigger)) {
			return message.substring(trigger.length());
		}

		return message;
	}
}
