package it.itzcrih.coralwinter.utils;

import it.itzcrih.coralwinter.CoralWinter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.enchantments.Enchantment;

import java.util.List;

/**
 * This code is made by
 * @author itzCrih
 */

public class SantaShovel {
    
    public void giveSantaShovel(Player player) {
        ItemStack santaShovel = createSantaShovel();
        player.getInventory().addItem(santaShovel);
    }

    public ItemStack createSantaShovel() {
        String spadeTypeName = CoralWinter.getConfigLoader().getConfig().getString("santashovel.type");
        Material spadeType = Material.getMaterial(spadeTypeName);

        if (spadeType == null) {
            CoralWinter.getInstance().getLogger().severe("[!] Invalid material");
            CoralWinter.getInstance().getServer().getConsoleSender().sendMessage("Since the material type inserted in the configuration is incorrect, we replaced that with a diamond shovel!");
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

        if (CoralWinter.getConfigLoader().getConfig().getBoolean("santashovel.enable_glow")) {
            meta.addEnchant(Enchantment.DURABILITY, 3, true);
        }

        meta.spigot().setUnbreakable(true);
        santaShovel.setItemMeta(meta);
        return santaShovel;
    }


    public String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public List<String> colorize(List<String> messages) {
        messages.replaceAll(this::colorize);
        return messages;
    }
}
