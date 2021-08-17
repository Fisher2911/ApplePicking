package me.masterofthefish.applepicking.commands;

import me.masterofthefish.applepicking.AppleItem;
import me.masterofthefish.applepicking.AppleManager;
import me.masterofthefish.applepicking.ApplePicking;
import me.masterofthefish.applepicking.config.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppleCommand implements CommandExecutor, TabCompleter {

    private final ApplePicking plugin;
    private final AppleManager appleManager;

    public AppleCommand(final ApplePicking plugin) {
        this.plugin = plugin;
        this.appleManager = plugin.getAppleManager();
    }

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command cmd, @NotNull final String label, @NotNull final String[] args) {
        if(!(sender instanceof Player)) {
            return true;
        }
        final Player player = (Player) sender;
        if(!player.hasPermission("applepicking.admin")) {
            player.sendMessage(Messages.noPermission());
            return true;
        }
        if(args.length != 1) {
            player.sendMessage(ChatColor.RED + "Invalid Usage, please use /ap (apple_id)");
            return true;
        }
        final String arg = args[0];
        final AppleItem appleItem = appleManager.getAppleItem(arg);

        if(appleItem == null) {
            player.sendMessage(ChatColor.RED + "Invalid Apple Id");
            return true;
        }
        player.getInventory().addItem(appleItem.getSkullItem().clone());
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command cmd, @NotNull final String label, @NotNull final String[] args) {
        if(!(sender instanceof Player)) {
            return null;
        }
        final Player player = (Player) sender;
        final List<String> tabCompletions = new ArrayList<>();
        if(!player.hasPermission("applepicking.admin")) {
            return null;
        }
        if(args.length != 1) {
            return null;
        }
        final String arg = args[0];
        for(final String appleId : appleManager.getAllIds()) {
            if(appleId.toLowerCase().startsWith(arg.toLowerCase(Locale.ROOT))) {
                tabCompletions.add(appleId);
            }
        }
        return tabCompletions;
    }
}
