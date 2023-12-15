package it.itzcrih.coralwinter.listeners;

import it.itzcrih.coralwinter.CoralWinter;
import it.itzcrih.coralwinter.utils.SnowUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class SnowBreakListener implements Listener {

    @EventHandler
    public void onSnowBreak(BlockBreakEvent event) {
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
    }
}