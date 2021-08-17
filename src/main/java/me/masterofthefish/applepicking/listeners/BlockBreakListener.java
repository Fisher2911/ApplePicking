package me.masterofthefish.applepicking.listeners;

import me.masterofthefish.applepicking.AppleItem;
import me.masterofthefish.applepicking.AppleManager;
import me.masterofthefish.applepicking.ApplePicking;
import me.masterofthefish.applepicking.config.Messages;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    private final ApplePicking plugin;
    private final AppleManager appleManager;

    public BlockBreakListener(final ApplePicking plugin) {
        this.plugin = plugin;
        this.appleManager = plugin.getAppleManager();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(final BlockBreakEvent event) {
        if(event.isCancelled()) {
            return;
        }
        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        final Location location = block.getLocation();
        final AppleItem appleItem = appleManager.getAppleItem(block);
        if(appleItem == null) {
            return;
        }
        if(!player.hasPermission("applepicking.break")) {
            player.sendMessage(Messages.noPermission());
            event.setCancelled(true);
            return;
        }
        appleItem.removeLocation(location);
        player.sendMessage(Messages.removedApple(appleItem.getId()));
    }
}
