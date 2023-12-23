package it.itzcrih.coralwinter.listeners;

import it.itzcrih.coralwinter.CoralWinter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * This code is made by
 * @author itzCrih
 */

public class BlockProtectionListener implements Listener {

    private static final boolean blockPlaceEnabled = CoralWinter.getConfigLoader().getConfig().getBoolean("block_protection.block_place_enabled");
    private static final boolean blockBreakEnabled = CoralWinter.getConfigLoader().getConfig().getBoolean("block_protection.block_break_enabled");

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!blockPlaceEnabled) return;

        Player player = event.getPlayer();
        if (!player.hasPermission("coralwinter.bypass.blockprotection")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!blockBreakEnabled) return;

        Player player = event.getPlayer();
        if (!player.hasPermission("coralwinter.bypass.blockprotection")) {
            event.setCancelled(true);
        }
    }
}
