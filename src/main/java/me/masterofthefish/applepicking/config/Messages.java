package me.masterofthefish.applepicking.config;

import me.masterofthefish.applepicking.ApplePicking;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.nio.file.Paths;

public class Messages {

    public static void load(final ApplePicking plugin) {
        final File file = Paths.get(plugin.getDataFolder().getPath(), "messages.yml").toFile();
        if(!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        cooldownMessage = color(config.getString("wait-for-cooldown"));
        invalidAppleErrorMessage = color(config.getString("invalid-apple-error"));
        placedApple = color(config.getString("placed-apple"));
        removedApple = color(config.getString("removed-apple"));
        noPermission = color(config.getString("no-permission"));
    }

    private static String cooldownMessage;
    private static String invalidAppleErrorMessage;
    private static String placedApple;
    private static String removedApple;
    private static String noPermission;

    public static String cooldownMessage(final int cooldown) {
        return cooldownMessage.replace("%cooldown%", String.valueOf(cooldown));
    }

    public static String invalidAppleErrorMessage(final Player player) {
        return invalidAppleErrorMessage.replace("%player%", player.getName());
    }

    public static String placedApple(final String id) {
        return placedApple.replace("%apple-id%", id);
    }

    public static String removedApple(final String id) {
        return removedApple.replace("%apple-id%", id);
    }

    public static String noPermission() {
        return noPermission;
    }

    private static String color(final String string) {
        if(string == null) {
            return "";
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}