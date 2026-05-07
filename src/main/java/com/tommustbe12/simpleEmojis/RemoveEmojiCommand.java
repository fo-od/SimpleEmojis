package com.tommustbe12.simpleEmojis;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class RemoveEmojiCommand implements BasicCommand {

    private final SimpleEmojis plugin;

    public RemoveEmojiCommand(SimpleEmojis plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(@NonNull CommandSourceStack source, String[] args) {
        if (args.length < 1) {
            source.getSender().sendRichMessage(SimpleEmojis.getPrefix() + "<red>Usage: /removeemoji <placeholder>");
            return;
        }

        String key = args[0];
        if (!plugin.getEmojiMap().containsKey(key)) {
            source.getSender().sendRichMessage(SimpleEmojis.getPrefix() + "<red>Emoji placeholder not found.");
            return;
        }

        plugin.getEmojiMap().remove(key);
        plugin.getConfig().set("emojis." + key, null);
        plugin.saveConfig();

        source.getSender().sendRichMessage(SimpleEmojis.getPrefix() + "<yellow>Removed emoji: " + key);
    }

    @Override
    public @NonNull Collection<String> suggest(@NonNull CommandSourceStack source, String @NonNull [] args) {
        if (args.length == 1) {
            String typed = args[0].toLowerCase();
            List<String> suggestions = new ArrayList<>();
            for (Map.Entry<String, String> entry : plugin.getEmojiMap().entrySet()) {
                String key = entry.getKey();
                String emoji = entry.getValue();
                if (key.toLowerCase().startsWith(typed)) {
                    suggestions.add(key + " → " + emoji); // visual suggestion
                }
            }
            return suggestions;
        }

        return Collections.emptyList();
    }

    @Override
    public @Nullable String permission() {
        return "simpleemojis.remove";
    }
}
