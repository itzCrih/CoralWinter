package it.itzcrih.coralwinter.utils;

import it.itzcrih.coralwinter.CoralWinter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
        player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1.0f, 1.0f);
        player.getInventory().addItem(new ItemStack(Material.SNOW_BALL, 1));
    }
}
