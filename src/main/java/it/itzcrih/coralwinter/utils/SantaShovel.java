package it.itzcrih.coralwinter.utils;

import it.itzcrih.coralwinter.CoralWinter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * This code is made by
 * @author itzCrih
 */

public class SantaShovel {
    
    public void giveSantaShovel(Player player) {
        ItemStack santaShovel = createSantaShovel();

        player.getInventory().addItem(santaShovel);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', CoralWinter.getConfigLoader().getMessages().getString("commands.shovel_received")));
    }

    public ItemStack createSantaShovel() {
        String spadeTypeName = CoralWinter.getConfigLoader().getConfig().getString("santashovel.type");
        Material spadeType = Material.getMaterial(spadeTypeName);

        if (spadeType == null) {
            CoralWinter.getInstance().getLogger().severe("The material inserted in the configuration is incorrect!");
            spadeType = Material.DIAMOND_SPADE;
        }

        ItemStack santaShovel = new ItemStack(spadeType, 1);
        ItemMeta meta = santaShovel.getItemMeta();

        String displayName = CoralWinter.getConfigLoader().getConfig().getString("santashovel.display-name");
        List<String> lore = CoralWinter.getConfigLoader().getConfig().getStringList("santashovel.lore");

        meta.setDisplayName(displayName != null ? colorize(displayName) : "Santa's Shovel");
        if (lore != null && !lore.isEmpty()) {
            meta.setLore(colorize(lore));
        }

        santaShovel.setItemMeta(meta);
        return santaShovel;
    }


    public String colorize(String message) {
        return message.replace("&", "§");
    }

    public List<String> colorize(List<String> messages) {
        messages.replaceAll(this::colorize);
        return messages;
    }
}
