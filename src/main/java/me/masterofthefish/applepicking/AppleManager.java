package me.masterofthefish.applepicking;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AppleManager {

    private final ApplePicking plugin;
    private final Map<String, AppleItem> appleItems = new HashMap<>();

    public AppleManager(final ApplePicking plugin) {
        this.plugin = plugin;
    }

    public AppleItem getAppleItem(final String id) {
        return appleItems.get(id);
    }

    public void setAppleItem(final String id, final AppleItem appleItem) {
        appleItems.put(id, appleItem);
    }

    public void removeAppleItem(final String id) {
        removeAppleItem(id);
    }

    @Nullable
    public AppleItem getAppleItem(final Block block) {
        final NamespacedKey key = plugin.getAppleKey();
        if(block.getState() instanceof Skull) {
            final Skull skull = (Skull) block.getState();
            final PersistentDataContainer skullData = skull.getPersistentDataContainer();
            final String id = skullData.get(key, PersistentDataType.STRING);
            return appleItems.get(id);
        }
        return null;
    }

    public Set<String> getAllIds() {
        return Collections.unmodifiableSet(appleItems.keySet());
    }

}