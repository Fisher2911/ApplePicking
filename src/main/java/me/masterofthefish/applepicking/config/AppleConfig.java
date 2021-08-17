package me.masterofthefish.applepicking.config;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.masterofthefish.applepicking.AppleItem;
import me.masterofthefish.applepicking.AppleManager;
import me.masterofthefish.applepicking.ApplePicking;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.util.UUID;

public class AppleConfig {

    private final ApplePicking plugin;
    private final AppleManager appleManager;

    public AppleConfig(final ApplePicking plugin) {
        this.plugin = plugin;
        this.appleManager = plugin.getAppleManager();
    }

    public void load() {
        plugin.saveDefaultConfig();
        final FileConfiguration config = plugin.getConfig();
        final ConfigurationSection section = config.getConfigurationSection("items");
        if(section == null) {
            return;
        }

        for(final String key : section.getKeys(false)) {
            final String base64Key = key + ".skull-id";
            final String cooldownKey = key + ".cooldown";
            final String dropKey = key + ".drops";
            final String itemTypeKey = dropKey + ".item-type";
            final String minDropsKey = dropKey + ".min-drops";
            final String maxDropsKey = dropKey + ".max-drops";

            final String base64 = section.getString(base64Key);
            final int cooldown = section.getInt(cooldownKey);
            final String itemType = section.getString(itemTypeKey);
            final int minDrops = section.getInt(minDropsKey);
            final int maxDrops = section.getInt(maxDropsKey);

            final ItemStack skullItem = getSkull(base64);
            final ItemMeta itemMeta = skullItem.getItemMeta();
            if(itemMeta == null) {
                return;
            }
            final PersistentDataContainer data = itemMeta.getPersistentDataContainer();
            data.set(plugin.getAppleKey(), PersistentDataType.STRING, key);
            skullItem.setItemMeta(itemMeta);
            try {
                final ItemStack itemStack = new ItemStack(Material.valueOf(itemType));
                final AppleItem appleItem = new AppleItem(key, skullItem, cooldown, itemStack, minDrops, maxDrops);
                appleManager.setAppleItem(key, appleItem);
            } catch (final IllegalArgumentException exception) {
                plugin.getLogger().severe("Error loading material: " + itemType + " from the config.");
            }
        }
    }

    private ItemStack getSkull(String base64) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if(meta == null) {
            return new ItemStack(Material.PLAYER_HEAD);
        }
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", base64));
        Field profileField;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        head.setItemMeta(meta);
        return head;
    }
}
