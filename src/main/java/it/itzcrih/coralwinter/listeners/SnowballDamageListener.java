package it.itzcrih.coralwinter.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * This code is made by
 * @author itzCrih
 */

public class SnowballDamageListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damaged = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();

            if (!isPVPAllowed(damaged, damager)) {
                event.setCancelled(true);
            }
        }
    }

    private boolean isPVPAllowed(Player damaged, Player damager) {
        Material heldItem = damager.getItemInHand().getType();
        boolean isSnowball = (heldItem == Material.SNOW_BALL);

        Material damagedHeldItem = damaged.getItemInHand().getType();
        boolean isHoldingTool = (damagedHeldItem == Material.DIAMOND_SPADE || damagedHeldItem == Material.SNOW_BALL);

        return isSnowball && isHoldingTool;
    }
}
