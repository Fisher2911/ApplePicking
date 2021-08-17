package me.masterofthefish.applepicking;

import me.masterofthefish.applepicking.commands.AppleCommand;
import me.masterofthefish.applepicking.config.AppleConfig;
import me.masterofthefish.applepicking.config.Messages;
import me.masterofthefish.applepicking.listeners.BlockBreakListener;
import me.masterofthefish.applepicking.listeners.BlockPlaceListener;
import me.masterofthefish.applepicking.listeners.ClickListener;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ApplePicking extends JavaPlugin {

    private AppleManager appleManager;
    private AppleConfig config;
    private final NamespacedKey appleKey = new NamespacedKey(this, "apple");

    @Override
    public void onEnable() {
        appleManager = new AppleManager(this);
        config = new AppleConfig(this);
        config.load();
        registerListeners();
        registerCommands();
        Messages.load(this);
    }

    private void registerListeners() {
        List.of(new ClickListener(this), new BlockPlaceListener(this),
                new BlockBreakListener(this)).forEach(listener ->
                getServer().getPluginManager().registerEvents(listener, this));
    }

    private void registerCommands() {
        final AppleCommand appleCommand = new AppleCommand(this);
        getCommand("applepicking").setExecutor(appleCommand);
    }

    public AppleManager getAppleManager() {
        return appleManager;
    }

    public NamespacedKey getAppleKey() {
        return appleKey;
    }
}