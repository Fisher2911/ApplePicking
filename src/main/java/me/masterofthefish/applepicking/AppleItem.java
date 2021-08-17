package me.masterofthefish.applepicking;

import me.masterofthefish.applepicking.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class AppleItem {

    private final Map<Location, Cooldown> cooldowns = new HashMap<>();
    private final String id;
    private final ItemStack skullItem;
    private final int cooldownSeconds;
    private final ItemStack dropItem;
    private final int minDrops;
    private final int maxDrops;

    public AppleItem(final String id, final ItemStack skullItem, final int cooldownSeconds, final ItemStack dropItem, final int minDrops, final int maxDrops) {
        this.id = id;
        this.skullItem = skullItem;
        this.cooldownSeconds = cooldownSeconds;
        this.dropItem = dropItem;
        this.minDrops = minDrops;
        this.maxDrops = maxDrops;
    }

    public void dropItems(final Location appleLocation, final Player player) {
        if(hasCooldown(appleLocation, player)) {
            player.sendMessage(Messages.cooldownMessage(cooldownSeconds - getPlayerCooldown(appleLocation, player)));
            return;
        }

        final Location blockUnder = appleLocation.getBlock().getLocation().subtract(0, 1, 0);
        final World world = blockUnder.getWorld();
        if(world == null) {
            Bukkit.getConsoleSender().sendMessage(Messages.invalidAppleErrorMessage(player));
            return;
        }
        final ItemStack clone = dropItem.clone();
        clone.setAmount(ThreadLocalRandom.current().nextInt(maxDrops + 1 - minDrops) + minDrops);
        world.dropItemNaturally(blockUnder, clone);
        final Cooldown cooldown = cooldowns.get(appleLocation);
        cooldown.setPlayerCooldown(player);
    }

    private boolean hasCooldown(final Location location, final Player player) {
        return getPlayerCooldown(location, player) < cooldownSeconds;
    }

    public int getPlayerCooldown(final Location location, final Player player) {
        Cooldown cooldown = cooldowns.get(location);
        if(cooldown == null) {
            cooldown = new Cooldown();
            cooldowns.put(location, cooldown);
        }
        return cooldown.getPlayerCooldown(player);
    }

    public Set<Location> getLocations() {
        return cooldowns.keySet();
    }

    public void addLocation(final Location location) {
        cooldowns.put(location, new Cooldown());
    }

    public void removeLocation(final Location location) {
        cooldowns.remove(location);
    }

    private class Cooldown {
        private final Map<UUID, LocalDateTime> playerCooldowns = new HashMap<>();

        public Cooldown() {
        }

        public int getPlayerCooldown(final Player player) {
            final UUID uuid = player.getUniqueId();
            final LocalDateTime time = playerCooldowns.get(uuid);
            if(time == null) {
                return cooldownSeconds;
            }
            final LocalDateTime now = LocalDateTime.now();
            return (int) Duration.between(time, now).getSeconds();
        }

        public void setPlayerCooldown(final Player player) {
            playerCooldowns.put(player.getUniqueId(), LocalDateTime.now());
        }
    }

    public String getId() {
        return id;
    }

    public ItemStack getSkullItem() {
        return skullItem;
    }
}