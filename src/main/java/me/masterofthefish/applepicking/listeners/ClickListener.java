package me.masterofthefish.applepicking.listeners;

import me.masterofthefish.applepicking.AppleItem;
import me.masterofthefish.applepicking.AppleManager;
import me.masterofthefish.applepicking.ApplePicking;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ClickListener implements Listener {

    private final ApplePicking plugin;
    private final AppleManager appleManager;

    public ClickListener(final ApplePicking plugin) {
        this.plugin = plugin;
        this.appleManager = plugin.getAppleManager();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onClick(final PlayerInteractEvent event) {
        if(event.useInteractedBlock() == Event.Result.DENY || event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }
        final Player player = event.getPlayer();
        final Action action = event.getAction();
        if(action == Action.RIGHT_CLICK_BLOCK) {
            final Block clickedBlock = event.getClickedBlock();
            if(clickedBlock == null || !(clickedBlock.getState() instanceof Skull)) {
                return;
            }
            final Skull skull = (Skull) clickedBlock.getState();
            final PersistentDataContainer data = skull.getPersistentDataContainer();
            final String id = data.get(plugin.getAppleKey(), PersistentDataType.STRING);
            if(id == null) {
                return;
            }
            final AppleItem appleItem = appleManager.getAppleItem(id);

            if(appleItem == null) {
                return;
            }

            final Location location = clickedBlock.getLocation();
            appleItem.dropItems(location, player);
        }
    }
}