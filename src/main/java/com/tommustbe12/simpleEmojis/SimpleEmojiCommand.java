package com.tommustbe12.simpleEmojis;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class SimpleEmojiCommand implements BasicCommand {

    private final SimpleEmojis plugin;

    public SimpleEmojiCommand(SimpleEmojis plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(@NonNull CommandSourceStack source, String[] args) {
        if (args.length == 0) {
            source.getSender().sendRichMessage(SimpleEmojis.getPrefix() + "<red>Usage: /simpleemojis <list|reload>");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "list":
                source.getSender().sendRichMessage(SimpleEmojis.getPrefix() + "<yellow>Current Emojis:");
                source.getSender().sendRichMessage(SimpleEmojis.getPrefix() + "<gray>These can be used by typing the placeholder in chat.");
                plugin.getEmojiMap().forEach((key, emoji) ->
                        source.getSender().sendRichMessage("<gray>  " + key + " -> " + emoji ));
                break;

            case "reload":
                plugin.reloadConfig();
                plugin.loadEmojiMap();
                source.getSender().sendRichMessage(SimpleEmojis.getPrefix() + "<green>Config reloaded.");
                break;

            case "help":
                source.getSender().sendRichMessage("<yellow>SimpleEmojis Help:");
                source.getSender().sendRichMessage("<gold>/simpleemojis list <gray>- Lists all emojis");
                source.getSender().sendRichMessage("<gold>/simpleemojis reload <gray>- Reloads the emoji configuration");
                source.getSender().sendRichMessage("<gold>/simpleemojis help <gray>- Shows this help message");
                source.getSender().sendRichMessage("<gold>/addemoji <placeholder> <emoji> <gray>- Add a new emoji with placeholder (e.g. :heart:) and emoji (e.g. ❤) to the config.");
                source.getSender().sendRichMessage("<gold>/removeemoji <placeholder> <gray>- Choose an emoji from the config to remove.");
                break;

            default:
                source.getSender().sendRichMessage(SimpleEmojis.getPrefix() + "<red>Unknown subcommand.");
        }
    }

    @Override
    public @NonNull Collection<String> suggest(@NonNull CommandSourceStack source, String @NonNull [] args) {
        if (args.length == 1) return Arrays.asList("list", "reload", "help");

        return Collections.emptyList();
    }
}
