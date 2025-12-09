package it.itzcrih.coralwinter.listeners;

import it.itzcrih.coralwinter.CoralWinter;
import it.itzcrih.coralwinter.utils.ChatUtils;
import it.itzcrih.coralwinter.utils.SantaShovel;
import it.itzcrih.coralwinter.utils.SnowUtils;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

/**
 * <a href="https://github.com/itzCrih/CoralWinter">CoralWinter</a>
 * This code is made by
 * @author itzCrih
 */

public class PlayerListener implements Listener {
    SantaShovel santaShovel = new SantaShovel();

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        if (CoralWinter.getConfigLoader().getConfig().getBoolean("world_protection.disable_player_drop")) {
            event.setCancelled(true);
        }
        if (CoralWinter.getConfigLoader().getConfig().getBoolean("santashovel.disable_drop") &&
                santaShovel.isSantaShovel(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
        if (CoralWinter.getConfigLoader().getConfig().getBoolean("world_protection.disable_player_pickup")) {
            event.setCancelled(true);
        }
        if (CoralWinter.getConfigLoader().getConfig().getBoolean("santashovel.disable_pickup") &&
        santaShovel.isSantaShovel(event.getItem().getItemStack())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)) return;
        if (event.getCurrentItem() != null &&
                santaShovel.isSantaShovel(event.getCurrentItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)) return;
        ItemStack item = event.getOldCursor();
        if (santaShovel.isSantaShovel(item)) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (CoralWinter.getConfigLoader().getConfig().getBoolean("santashovel.give_on_join")) {
            santaShovel.giveSantaShovel(player);
        }

        // Questo codice invierà solo a me un messaggio in chat per informarmi che state usando il mio plugin. Per favore non rimuoverlo.
        if (player.getName().equals("itz_Crih") || player.getName().equals("TeoChillato")) {
            player.sendMessage(ChatUtils.colorize(
                    "&b&l====[ CoralWinter Notice ]====\n" +
                            "&7Questo server sta usando &bCoralWinter&7!\n" +
                            "\n" +
                            "&3" + CoralWinter.getInstance().getDescription().getName() +
                            " &7&o v" + CoralWinter.getInstance().getDescription().getVersion() + "\n" +
                            "&7Autore: &3" + CoralWinter.getInstance().getDescription().getAuthors() + "\n" +
                            "&b&l==============================="
            ));
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!CoralWinter.getConfigLoader().getConfig().getBoolean("world_protection.disable_block_placement")) return;

        Player player = event.getPlayer();
        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (SnowUtils.canBreakSnowBlock(player, block)) {
            SnowUtils.giveSnowball(player);
            block.setType(Material.AIR);
            event.setCancelled(true);

            Bukkit.getScheduler().runTaskLater(CoralWinter.getInstance(), () -> {
                if (block.getType() == Material.AIR) {
                    block.setType(Material.SNOW);
                }
            }, 7 * 20);
        }

        if (!CoralWinter.getConfigLoader().getConfig().getBoolean("world_protection.disable_block_breaking")) return;
        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Snowball) {
            Player damaged = (Player) event.getEntity();
            Snowball snowball = (Snowball) event.getDamager();

            if (snowball.getShooter() instanceof Player) {
                Player damager = (Player) snowball.getShooter();

                if (!(damaged.getInventory().getItemInHand().getType() == Material.SNOW_BALL ||
                        damaged.getInventory().getItemInHand().getType() == Material.DIAMOND_SPADE)) {
                    event.setCancelled(true);
                } else {
                    damager.getWorld().playSound(damager.getLocation(), Sound.DIG_SNOW, 1.0f, 2.0f);
                    damager.getWorld().playSound(damager.getLocation(), Sound.ARROW_HIT, 1.0f, 1.3f);

                    damaged.getWorld().playSound(damaged.getLocation(), Sound.STEP_SNOW, 1.0f, 1.3f);
                    damaged.getWorld().spigot().playEffect(damaged.getLocation(), Effect.SNOW_SHOVEL, 26, 0, 0.2F, 0.5F, 0.2F, 0.2F, 12, 387);
                    damaged.getWorld().spigot().playEffect(damaged.getLocation(), Effect.FLAME, 26, 0, 0.2F, 0.5F, 0.2F, 0.2F, 12, 387);

                    if (CoralWinter.getConfigLoader().getConfig().getBoolean("snowball.snowball_knockback.enabled")) {
                        double knockbackMultiplier = CoralWinter.getConfigLoader().getConfig().getDouble("snowball.snowball_knockback.horizontal");
                        double verticalBoost = CoralWinter.getConfigLoader().getConfig().getDouble("snowball.snowball_knockback.vertical");

                        Vector knockback = damager.getLocation().getDirection().multiply(knockbackMultiplier);
                        knockback.setY(knockback.getY() + verticalBoost);

                        damaged.setVelocity(knockback);
                    }
                }
            }
        } else if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());

        if (CoralWinter.getConfigLoader().getConfig().getBoolean("santashovel.when_holding_enable_effects")) {
            if (newItem != null && newItem.hasItemMeta() && newItem.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&' ,CoralWinter.getConfigLoader().getConfig().getString("santashovel.display_name")))) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2, false, false));
                player.setAllowFlight(true);
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
            } else {
                player.removePotionEffect(PotionEffectType.SPEED);
                if (player.getGameMode() != GameMode.CREATIVE) {
                    player.setAllowFlight(false);
                    player.setFlying(false);
                }
            }
        }
    }

    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInHand();

        if (CoralWinter.getConfigLoader().getConfig().getBoolean("santashovel.right_click_ability_enabled")) {
            if (item != null && item.hasItemMeta() && item.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', CoralWinter.getConfigLoader().getConfig().getString("santashovel.display_name")))) {
                if (event.getAction().toString().contains("RIGHT_CLICK")) {
                    UUID uuid = player.getUniqueId();
                    long currentTime = System.currentTimeMillis();
                    if (cooldowns.containsKey(uuid) && currentTime < cooldowns.get(uuid)) {
                        long timeLeft = (cooldowns.get(uuid) - currentTime) / 1000;
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cWait " + timeLeft + " seconds before using this ability again!"));
                        return;
                    }

                    cooldowns.put(uuid, currentTime + 50000);

                    Arrow arrow = player.launchProjectile(Arrow.class);
                    arrow.setVelocity(player.getLocation().getDirection().multiply(2));
                    player.playSound(player.getLocation(), Sound.SHOOT_ARROW, 1, 1);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Location arrowLocation = arrow.getLocation();

                            arrowLocation.getWorld().playEffect(arrowLocation, Effect.EXPLOSION_HUGE, 1);
                            arrowLocation.getWorld().playSound(arrowLocation, Sound.EXPLODE, 2, 1);

                            double radius = 5.0;
                            for (Player nearbyPlayer : Bukkit.getOnlinePlayers()) {
                                if (!nearbyPlayer.equals(player) && nearbyPlayer.getLocation().distance(arrowLocation) <= radius) {
                                    Vector direction = nearbyPlayer.getLocation().toVector().subtract(arrowLocation.toVector()).normalize();

                                    double knockbackMultiplier = 3.0;
                                    double verticalBoost = 1.5;
                                    Vector knockback = direction.multiply(knockbackMultiplier).setY(verticalBoost);

                                    nearbyPlayer.setVelocity(knockback);
                                }
                            }
                        }
                    }.runTaskLater(CoralWinter.getInstance(), 30L);
                }
            }
        }
    }
}
