package it.itzcrih.coralwinter.utils;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SnowUtils {

    public static boolean canBreakSnowBlock(Player player, Block block) {
        Material heldItem = player.getInventory().getItemInHand().getType();
        if (heldItem == Material.DIAMOND_SPADE) {
            return block.getType() == Material.SNOW_BLOCK || block.getType() == Material.SNOW;
        }
        return false;
    }

    public static void giveSnowball(Player player) {
        player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1.0f, 1.0f);
        player.getInventory().addItem(new ItemStack(Material.SNOW_BALL, 1));
    }
}
