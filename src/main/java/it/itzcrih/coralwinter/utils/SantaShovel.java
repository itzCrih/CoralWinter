package it.itzcrih.coralwinter.utils;

import it.itzcrih.coralwinter.CoralWinter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href="https://github.com/itzCrih/CoralWinter">CoralWinter</a>
 * This code is made by
 * @author itzCrih
 */

public class SantaShovel {
    private static final String HIDDEN_MARKER = "\u200B" + ChatColor.BLACK + "coralwinter_santa_shovel";

    public void giveSantaShovel(Player player) {
        if (hasSantaShovel(player)) {
            return;
        }
        player.getInventory().addItem(createSantaShovel());
    }

    public boolean hasSantaShovel(Player player) {
        for (ItemStack it : player.getInventory().getContents()) {
            if (isSantaShovel(it)) return true;
        }
        return false;
    }

    public boolean isSantaShovel(ItemStack item) {
        if (item == null) return false;
        if (!item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            for (String line : lore) {
                if (HIDDEN_MARKER.equals(line)) return true;
            }
        }
        return false;
    }

    public ItemStack createSantaShovel() {
        String spadeTypeName = CoralWinter.getConfigLoader().getConfig().getString("santashovel.type");
        Material spadeType = Material.getMaterial(spadeTypeName);

        if (spadeType == null) {
            CoralWinter.getInstance().getLogger().severe("[!] Invalid material");
            spadeType = Material.DIAMOND_SPADE;
        }

        ItemStack santaShovel = new ItemStack(spadeType, 1);
        ItemMeta meta = santaShovel.getItemMeta();

        String displayName = CoralWinter.getConfigLoader().getConfig().getString("santashovel.display_name");
        List<String> lore = new ArrayList<>(CoralWinter.getConfigLoader().getConfig().getStringList("santashovel.lore"));

        meta.setDisplayName(displayName != null ? ChatUtils.colorize(displayName) : "Santa's Shovel");
        if (lore != null && !lore.isEmpty()) {
            meta.setLore(ChatUtils.colorize(lore));
        } else {
            meta.setLore(new ArrayList<String>());
        }

        List<String> newLore = meta.getLore();
        newLore.add(HIDDEN_MARKER);
        meta.setLore(newLore);

        if (CoralWinter.getConfigLoader().getConfig().getBoolean("santashovel.enable_glow")) {
            meta.addEnchant(Enchantment.DURABILITY, 3, true);
        }

        meta.spigot().setUnbreakable(true);
        santaShovel.setItemMeta(meta);
        return santaShovel;
    }
}
