package me.masterofthefish.applepicking.listeners;

import me.masterofthefish.applepicking.AppleItem;
import me.masterofthefish.applepicking.AppleManager;
import me.masterofthefish.applepicking.ApplePicking;
import me.masterofthefish.applepicking.config.Messages;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BlockPlaceListener implements Listener {

    final ApplePicking plugin;
    private final AppleManager appleManager;

    public BlockPlaceListener(final ApplePicking plugin) {
        this.plugin = plugin;
        this.appleManager = plugin.getAppleManager();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(final BlockPlaceEvent event) {
        if(event.isCancelled()) {
            return;
        }
        final Player player = event.getPlayer();
        final ItemStack itemPlaced = event.getItemInHand();
        final Block blockPlaced = event.getBlockPlaced();

        final ItemMeta itemMeta = itemPlaced.getItemMeta();
        if(itemMeta == null) {
            return;
        }
        final NamespacedKey key = plugin.getAppleKey();
        final PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        final String id = data.get(key, PersistentDataType.STRING);
        if(id == null) {
            return;
        }
        final AppleItem appleItem = appleManager.getAppleItem(id);

        if(appleItem == null) {
            return;
        }

        if(!player.hasPermission("applepicking.place")) {
            player.sendMessage(Messages.noPermission());
            event.setCancelled(true);
            return;
        }

         if(blockPlaced.getState() instanceof Skull) {
             final Skull skull = (Skull) blockPlaced.getState();
             final PersistentDataContainer skullData = skull.getPersistentDataContainer();
             skullData.set(key, PersistentDataType.STRING, id);
             appleItem.addLocation(blockPlaced.getLocation());
             skull.update();
             player.sendMessage(Messages.placedApple(id));
         }
    }
}