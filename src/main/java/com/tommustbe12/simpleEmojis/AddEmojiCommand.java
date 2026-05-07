package com.tommustbe12.simpleEmojis;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

public class AddEmojiCommand implements BasicCommand {

    private final SimpleEmojis plugin;

    public AddEmojiCommand(SimpleEmojis plugin) {
        this.plugin = plugin;
    }

    private boolean isEmoji(String input) {
        return input.codePoints().count() == 1;
    }

    @Override
    public void execute(@NonNull CommandSourceStack source, String[] args) {
        if (args.length < 2) {
            source.getSender().sendRichMessage(SimpleEmojis.getPrefix() + "<red>Usage: /addemoji <placeholder> <emoji>");
            return;
        }

        String key = args[0];
        String emoji = args[1];

        if (!isEmoji(emoji)) {
            source.getSender().sendRichMessage(SimpleEmojis.getPrefix() + "<red>Second argument must be a single emoji character.");
            return;
        }

        plugin.addEmoji(key, emoji);
        source.getSender().sendRichMessage(SimpleEmojis.getPrefix() + "<green>Added emoji: " + key + " -> " + emoji);
    }

    @Override
    public @NonNull Collection<String> suggest(@NonNull CommandSourceStack source, String @NonNull [] args) {
        if (args.length == 1) return Collections.singletonList(":example:");
        if (args.length == 2) return Collections.singletonList("😀");

        return Collections.emptyList();
    }

    @Override
    public @Nullable String permission() {
        return "simpleemojis.add";
    }
}
