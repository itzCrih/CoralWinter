package it.itzcrih.coralwinter.utils;

import it.itzcrih.coralwinter.CoralWinter;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * This code is made by
 * @author itzCrih
 */

public class SnowUtils {

    public static boolean canBreakSnowBlock(Player player, Block block) {
        String spadeTypeName = CoralWinter.getConfigLoader().getConfig().getString("santashovel.type");
        Material spadeType = Material.getMaterial(spadeTypeName);
        Material heldItem = player.getInventory().getItemInHand().getType();

        if (heldItem == spadeType) {
            return block.getType() == Material.SNOW_BLOCK || block.getType() == Material.SNOW;
        }
        return false;
    }

    public static void giveSnowball(Player player) {
        if (CoralWinter.getConfigLoader().getConfig().getBoolean("snowball.enable_limit")) {
            int totalSnowballs = 0;
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null && item.getType() == Material.SNOW_BALL) {
                    totalSnowballs += item.getAmount();
                }
            }

            double maxLimit = CoralWinter.getConfigLoader().getConfig().getDouble("snowball.max_limit");
            if (totalSnowballs >= maxLimit) {
                player.sendMessage(ChatUtils.colorize(CoralWinter.getConfigLoader().getMessages().getString("errors.maximum_amount")));
                return;
            }
        }

        player.playSound(player.getLocation(), Sound.DIG_SNOW, 1.0f, 1.0f);
        player.playSound(player.getLocation(),
                Sound.valueOf(CoralWinter.getConfigLoader().getConfig().getString("santashovel.sound_when_breaking")),
                1.0f, 2.0f);
        player.getWorld().spigot().playEffect(player.getLocation(), Effect.SNOWBALL_BREAK, 26, 0, 0.2F, 0.5F, 0.2F, 0.2F, 12, 387);

        ItemStack snowball = new ItemStack(Material.SNOW_BALL, 1);
        ItemMeta meta = snowball.getItemMeta();
        if (meta != null) {
            String displayName = CoralWinter.getConfigLoader().getConfig().getString("snowball.display_name");
            List<String> lore = CoralWinter.getConfigLoader().getConfig().getStringList("snowball.lore");

            meta.setDisplayName(displayName != null ? ChatUtils.colorize(displayName) : "Snowball");
            if (lore != null && !lore.isEmpty()) {
                meta.setLore(ChatUtils.colorize(lore));
            }
            if (CoralWinter.getConfigLoader().getConfig().getBoolean("snowball.enable_glow")) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
            }
        }

        snowball.setItemMeta(meta);
        player.getInventory().addItem(snowball);
    }

    // todo: Add 24/12 (xmas) event
}
