package com.tommustbe12.simpleEmojis;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessagePostProcessEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class SimpleEmojis extends JavaPlugin implements Listener {

    private static final String PREFIX = "<gray>[<white>Simple<yellow>Emojis<gray>]</gray> ";
    private final Map<String, String> emojiMap = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadEmojiMap();
        DiscordSRV.api.subscribe(this);
        Bukkit.getPluginManager().registerEvents(this, this);

        registerCommand("simpleemojis", new SimpleEmojiCommand(this));
        registerCommand("addemoji", new AddEmojiCommand(this));
        registerCommand("removeemoji", new RemoveEmojiCommand(this));

        getLogger().info("§fSimple§eEmojis §fEnabled!");
    }

    @Override
    public void onDisable() {
        DiscordSRV.api.unsubscribe(this);
    }

    public void loadEmojiMap() {
        emojiMap.clear();
        FileConfiguration config = getConfig();
        if (config.isConfigurationSection("emojis")) {
            for (String key : config.getConfigurationSection("emojis").getKeys(false)) {
                String value = config.getString("emojis." + key);
                if (value != null) {
                    emojiMap.put(key, value);
                } else {
                    getLogger().warning("Invalid emoji mapping for key: " + key);
                }
            }
        }
    }

    public void addEmoji(String placeholder, String emoji) {
        emojiMap.put(placeholder, emoji);
        getConfig().set("emojis." + placeholder, emoji);
        saveConfig();
    }

    public Map<String, String> getEmojiMap() {
        return emojiMap;
    }

    public static String getPrefix() {
        return PREFIX;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncChatEvent event) {
        Component newMessage = event.message();

        for (Map.Entry<String, String> entry : this.getEmojiMap().entrySet()) {
            newMessage = newMessage.replaceText(TextReplacementConfig.builder().matchLiteral(entry.getKey()).replacement(entry.getValue()).build());
        }

        event.message(newMessage);
    }

    @Subscribe
    public void onDiscordMessage(DiscordGuildMessagePostProcessEvent event) {
        github.scarsz.discordsrv.dependencies.kyori.adventure.text.Component newMessage = event.getMinecraftMessage();

        for (Map.Entry<String, String> entry : this.getEmojiMap().entrySet()) {
            newMessage = newMessage.replaceText(github.scarsz.discordsrv.dependencies.kyori.adventure.text.TextReplacementConfig.builder().matchLiteral(entry.getKey()).replacement(entry.getValue()).build());
        }

        event.setMinecraftMessage(newMessage);
    }
}
